package com.liyu.server.service.impl;

import com.liyu.server.service.GradeService;
import com.liyu.server.tables.pojos.Grade;
import com.liyu.server.tables.records.GradeRecord;
import com.liyu.server.utils.CommonUtils;
import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.liyu.server.tables.Grade.GRADE;

@Service
public class GradeServiceImpl implements GradeService {
    @Resource
    private DSLContext context;

    @Override
    public Grade create(Grade newGrade) {
        return context.insertInto(GRADE).columns(
                GRADE.GRADE_ID,
                GRADE.NAME,
                GRADE.LEVEL,
                GRADE.ENABLED
        ).values(
                CommonUtils.UUIDGenerator(),
                newGrade.getName(),
                newGrade.getLevel(),
                true
        ).returning().fetchOne().into(Grade.class);
    }

    @Override
    public List<Grade> list() {
        return context.selectFrom(GRADE)
                .orderBy(GRADE.LEVEL.desc())
                .fetch()
                .into(Grade.class);
    }

    @Override
    public Grade update(String gradeId, Grade newGrade) {
        GradeRecord gradeRecord = context.selectFrom(GRADE)
                .where(GRADE.GRADE_ID.eq(gradeId))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("未找到此年级"));
        String name = newGrade.getName();
        if (name != null && !name.isEmpty()) {
            gradeRecord.setName(name);
        }
        Integer level = newGrade.getLevel();
        if (level != null) {
            gradeRecord.setLevel(level);
        }
        Boolean enabled = newGrade.getEnabled();
        if (enabled != null) {
            gradeRecord.setEnabled(enabled);
        }
        gradeRecord.update();
        return gradeRecord.into(Grade.class);
    }

    @Override
    public void delete(String gradeId) {
        context.deleteFrom(GRADE).where(GRADE.GRADE_ID.eq(gradeId)).execute();
    }
}
