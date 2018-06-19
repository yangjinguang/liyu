package com.liyu.server.service.impl;

import com.liyu.server.enums.GenderEnum;
import com.liyu.server.enums.StudentStatusEnum;
import com.liyu.server.model.StudentProfile;
import com.liyu.server.mongo.StudentProfilePropertyRepository;
import com.liyu.server.mongo.StudentProfileRepository;
import com.liyu.server.service.StudentService;
import com.liyu.server.tables.pojos.Student;
import com.liyu.server.tables.records.StudentRecord;
import com.liyu.server.utils.CommonUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;

import static com.liyu.server.tables.Student.STUDENT;

@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    private DSLContext context;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private StudentProfileRepository studentProfileRepository;
    @Resource
    private StudentProfilePropertyRepository studentProfilePropertyRepository;

    @Override
    public Integer count(String tenantId, String organizationId, String name, String phone) {
        if (tenantId == null || tenantId.isEmpty()) {
            throw new IllegalArgumentException("缺少tenantId");
        }
        HashSet<Condition> conditions = new HashSet<>();
        conditions.add(STUDENT.TENANT_ID.eq(tenantId));
        conditions.add(STUDENT.STATUS.notEqual(StudentStatusEnum.DELETED));
        if (organizationId != null && !organizationId.isEmpty()) {
            conditions.add(STUDENT.ORGANIZATION_ID.eq(organizationId));
        }
        if (name != null && !name.isEmpty()) {
            conditions.add(STUDENT.NAME.like("%" + name + "%"));
        }
        if (phone != null && !phone.isEmpty()) {
            conditions.add(STUDENT.PHONE.like("%" + phone + "%"));
        }
        return context.selectCount().from(STUDENT).where(conditions).fetchOne().into(int.class);
    }

    @Override
    public List<Student> query(String tenantId, Integer offset, Integer size, String organizationId, String name, String phone) {
        if (tenantId == null || tenantId.isEmpty()) {
            throw new IllegalArgumentException("缺少tenantId");
        }
        HashSet<Condition> conditions = new HashSet<>();
        conditions.add(STUDENT.TENANT_ID.eq(tenantId));
        conditions.add(STUDENT.STATUS.notEqual(StudentStatusEnum.DELETED));
        if (organizationId != null && !organizationId.isEmpty()) {
            conditions.add(STUDENT.ORGANIZATION_ID.eq(organizationId));
        }
        if (name != null && !name.isEmpty()) {
            conditions.add(STUDENT.NAME.like("%" + name + "%"));
        }
        if (phone != null && !phone.isEmpty()) {
            conditions.add(STUDENT.PHONE.like("%" + phone + "%"));
        }
        return context.selectFrom(STUDENT)
                .where(conditions)
                .offset(offset)
                .limit(size)
                .fetch()
                .into(Student.class);
    }

    @Override
    public Student create(Student newStudent) {
        return context.insertInto(STUDENT)
                .columns(
                        STUDENT.STUDENT_ID,
                        STUDENT.NAME,
                        STUDENT.GENDER,
                        STUDENT.PHONE,
                        STUDENT.AVATAR,
                        STUDENT.BIRTHDAY,
                        STUDENT.ORGANIZATION_ID,
                        STUDENT.PROFILE_ID,
                        STUDENT.TENANT_ID,
                        STUDENT.STATUS
                )
                .values(
                        CommonUtils.UUIDGenerator(),
                        newStudent.getName(),
                        newStudent.getGender(),
                        newStudent.getPhone(),
                        newStudent.getAvatar(),
                        newStudent.getBirthday(),
                        newStudent.getOrganizationId(),
                        newStudent.getProfileId(),
                        newStudent.getTenantId(),
                        StudentStatusEnum.NORMAL
                ).returning()
                .fetchOne()
                .into(Student.class);
    }

    @Override
    public Student update(String studentId, Student newStudent) {
        StudentRecord studentRecord = context.selectFrom(STUDENT)
                .where(STUDENT.STUDENT_ID.eq(studentId))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("未找到此学生"));
        String name = newStudent.getName();
        if (name != null && !name.isEmpty()) {
            studentRecord.setName(name);
        }
        GenderEnum gender = newStudent.getGender();
        if (gender != null) {
            studentRecord.setGender(gender);
        }
        String phone = newStudent.getPhone();
        if (phone != null && !phone.isEmpty()) {
            studentRecord.setPhone(phone);
        }
        String avatar = newStudent.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            studentRecord.setAvatar(avatar);
        }
        Timestamp birthday = newStudent.getBirthday();
        if (birthday != null) {
            studentRecord.setBirthday(birthday);
        }
        String profileId = newStudent.getProfileId();
        if (profileId != null && !profileId.isEmpty()) {
            studentRecord.setProfileId(profileId);
        }
        String organizationId = newStudent.getOrganizationId();
        if (organizationId != null && !organizationId.isEmpty()) {
            studentRecord.setOrganizationId(organizationId);
        }
        StudentStatusEnum status = newStudent.getStatus();
        if (status != null) {
            studentRecord.setStatus(status);
        }
        studentRecord.update();
        return studentRecord.into(Student.class);
    }

    @Override
    public void delete(String studentId) {
        context.deleteFrom(STUDENT).where(STUDENT.STUDENT_ID.eq(studentId)).execute();
    }

    @Override
    public StudentProfile getProfileTemplate(String tenantId) {
        return studentProfileRepository.findFirstByTenantIdAndTemplateIsTrueAndEnabledIsTrue(tenantId);
    }

    @Override
    public StudentProfile createProfileTemplate(StudentProfile newProfile) {
        StudentProfile profile = studentProfileRepository.findFirstByTenantIdAndTemplateIsTrueAndEnabledIsTrue(newProfile.getTenantId());
        if (profile != null) {
            throw new IllegalArgumentException("不能重复创建");
        }
        studentProfilePropertyRepository.save(newProfile.getProperties());
        return studentProfileRepository.save(newProfile);
    }

    @Override
    public StudentProfile updateProfileTemplate(String id, StudentProfile newProfile) {
        StudentProfile profile = studentProfileRepository.findOne(id);
        if (profile == null) {
            throw new IllegalArgumentException("模板不存在");
        }
        studentProfilePropertyRepository.save(newProfile.getProperties());
        return studentProfileRepository.save(newProfile);
    }

}
