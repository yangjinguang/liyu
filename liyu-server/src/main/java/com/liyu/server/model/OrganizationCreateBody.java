package com.liyu.server.model;

import com.liyu.server.tables.pojos.Organization;
import org.jooq.types.ULong;

import java.sql.Timestamp;
import java.util.List;

public class OrganizationCreateBody extends Organization {
    private List<String> contactIds;

    public OrganizationCreateBody() {
    }

    public OrganizationCreateBody(Organization value, List<String> contactIds) {
        super(value);
        this.contactIds = contactIds;
    }

    public OrganizationCreateBody(ULong id, String organizationId, String name, String description, String avatar, Boolean enabled, String gradeId, String tenantId, Timestamp createdAt, Timestamp updatedAt, List<String> contactIds) {
        super(id, organizationId, name, description, avatar, enabled, gradeId, tenantId, createdAt, updatedAt);
        this.contactIds = contactIds;
    }

    public List<String> getContactIds() {
        return contactIds;
    }

    public void setContactIds(List<String> contactIds) {
        this.contactIds = contactIds;
    }
}
