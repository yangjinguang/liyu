package com.liyu.server.model;

import com.liyu.server.enums.GenderEnum;
import com.liyu.server.enums.StudentStatusEnum;
import com.liyu.server.tables.pojos.Organization;
import com.liyu.server.tables.pojos.Student;
import org.jooq.types.ULong;

import java.sql.Timestamp;

public class StudentDetail extends Student {
    private Organization organization;

    public StudentDetail() {
    }

    public StudentDetail(Student value) {
        super(value);
    }

    public StudentDetail(Student value, Organization organization) {
        super(value);
        this.organization = organization;
    }

    public StudentDetail(ULong id, String studentId, String name, GenderEnum gender, String phone, String avatar, String profileId, Timestamp birthday, StudentStatusEnum status, String tenantId, String organizationId, Timestamp createdAt, Timestamp updatedAt, Organization organization) {
        super(id, studentId, name, gender, phone, avatar, profileId, birthday, status, tenantId, organizationId, createdAt, updatedAt);
        this.organization = organization;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
