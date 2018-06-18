package com.liyu.server.model;

import com.liyu.server.enums.ContactStatusEnum;
import com.liyu.server.enums.GenderEnum;
import com.liyu.server.tables.pojos.Contact;
import com.liyu.server.tables.pojos.Organization;
import org.jooq.types.ULong;

import java.sql.Timestamp;
import java.util.List;

public class ContactDetail extends Contact {
    private List<String> organiztionIds;
    private List<Organization> organizations;
    private String organizationTitle;

    public ContactDetail() {
    }

    public ContactDetail(Contact value) {
        super(value);
    }

    public ContactDetail(ULong id, String contactId, String accountId, String tenantId, String name, GenderEnum gender, String email, String phone, String avatar, String roleId, Boolean isAdmin, ContactStatusEnum status, Timestamp createdAt, Timestamp updatedAt, List<String> organiztionIds, List<Organization> organizations, String organizationTitle) {
        super(id, contactId, accountId, tenantId, name, gender, email, phone, avatar, roleId, isAdmin, status, createdAt, updatedAt);
        this.organiztionIds = organiztionIds;
        this.organizations = organizations;
        this.organizationTitle = organizationTitle;
    }

    public List<String> getOrganiztionIds() {
        return organiztionIds;
    }

    public void setOrganiztionIds(List<String> organiztionIds) {
        this.organiztionIds = organiztionIds;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public String getOrganizationTitle() {
        return organizationTitle;
    }

    public void setOrganizationTitle(String organizationTitle) {
        this.organizationTitle = organizationTitle;
    }
}
