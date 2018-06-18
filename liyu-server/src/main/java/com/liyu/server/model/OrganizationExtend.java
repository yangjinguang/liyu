package com.liyu.server.model;

import com.liyu.server.tables.pojos.Organization;
import org.jooq.types.ULong;

import java.sql.Timestamp;

public class OrganizationExtend extends Organization {
    private String gradeName;
    private Integer studentCount;

    public OrganizationExtend(OrganizationExtend value) {
        super(value);
        this.gradeName = value.gradeName;
        this.studentCount = value.studentCount;
    }

    public OrganizationExtend(
            ULong id,
            String organizationId,
            String name,
            String description,
            String avatar,
            Boolean enabled,
            String gradeId,
            String gradeName,
            Integer contactCount,
            String tenantId,
            Timestamp createdAt,
            Timestamp updatedAt) {
        super(id, organizationId, name, description, avatar, enabled, gradeId, tenantId, createdAt, updatedAt);
        this.gradeName = gradeName;
        this.studentCount = contactCount;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }
}
