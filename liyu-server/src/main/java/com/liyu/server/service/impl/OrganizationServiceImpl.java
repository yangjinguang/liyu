package com.liyu.server.service.impl;

import com.liyu.server.enums.ContactStatusEnum;
import com.liyu.server.model.OrganizationExtend;
import com.liyu.server.service.OrganizationService;
import com.liyu.server.tables.pojos.Contact;
import com.liyu.server.tables.pojos.Organization;
import com.liyu.server.tables.records.OrganizationRecord;
import com.liyu.server.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;

import static com.liyu.server.tables.Student.STUDENT;
import static com.liyu.server.tables.Grade.GRADE;
import static com.liyu.server.tables.Contact.CONTACT;
import static com.liyu.server.tables.Organization.ORGANIZATION;
import static com.liyu.server.tables.OrganizationContact.ORGANIZATION_CONTACT;

@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Resource
    private DSLContext context;

    @Override
    public Organization byId(ULong id) {
        return context.selectFrom(ORGANIZATION).where(ORGANIZATION.ID.eq(id)).fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("organization not found"))
                .into(Organization.class);
    }

    @Override
    public Integer countByTenantId(String tenantId) {
        return context.selectCount().from(ORGANIZATION).where(ORGANIZATION.TENANT_ID.eq(tenantId)).fetchOne().into(int.class);
    }

    @Override
    public List<OrganizationExtend> listByTenantId(String tenantId, Integer offset, Integer limit) {
        return context.select(
                ORGANIZATION.ID,
                ORGANIZATION.ORGANIZATION_ID,
                ORGANIZATION.NAME,
                ORGANIZATION.DESCRIPTION,
                ORGANIZATION.AVATAR,
                ORGANIZATION.ENABLED,
                ORGANIZATION.GRADE_ID,
                GRADE.NAME,
                STUDENT.ID.count(),
                ORGANIZATION.TENANT_ID,
                ORGANIZATION.CREATED_AT,
                ORGANIZATION.UPDATED_AT
        ).from(ORGANIZATION)
                .leftJoin(GRADE)
                .on(GRADE.GRADE_ID.eq(ORGANIZATION.GRADE_ID))
                .leftJoin(STUDENT)
                .on(STUDENT.ORGANIZATION_ID.eq(ORGANIZATION.ORGANIZATION_ID))
                .where(ORGANIZATION.TENANT_ID.eq(tenantId))
                .offset(offset)
                .limit(limit)
                .fetch()
                .into(OrganizationExtend.class);
    }

    @Override
    public Organization create(Organization newOrganization) {
        Timestamp currentTime = new Timestamp(new Date().getTime());
        return context.insertInto(ORGANIZATION).columns(
                ORGANIZATION.ORGANIZATION_ID,
                ORGANIZATION.NAME,
                ORGANIZATION.AVATAR,
                ORGANIZATION.GRADE_ID,
                ORGANIZATION.TENANT_ID,
                ORGANIZATION.CREATED_AT,
                ORGANIZATION.UPDATED_AT
        ).values(
                CommonUtils.UUIDGenerator(),
                newOrganization.getName(),
                newOrganization.getAvatar(),
                newOrganization.getGradeId(),
                newOrganization.getTenantId(),
                currentTime,
                currentTime
        ).returning().fetchOne().into(Organization.class);
    }

    @Override
    public Organization update(String organizationId, Organization newOrganization) {
        OrganizationRecord organizationRecord = context.selectFrom(ORGANIZATION)
                .where(ORGANIZATION.ORGANIZATION_ID.eq(organizationId))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("organization not found"));
        String name = newOrganization.getName();
        if (name != null && !name.isEmpty()) {
            organizationRecord.setName(name);
        }
        String avatar = newOrganization.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            organizationRecord.setAvatar(avatar);
        }
        String description = newOrganization.getDescription();
        if (description != null && !description.isEmpty()) {
            organizationRecord.setDescription(description);
        }

        String gradeId = newOrganization.getGradeId();
        if (gradeId != null && !gradeId.isEmpty()) {
            organizationRecord.setGradeId(gradeId);
        }
        String tenantId = newOrganization.getTenantId();
        if (tenantId != null && !tenantId.isEmpty()) {
            organizationRecord.setTenantId(newOrganization.getTenantId());
        }
        organizationRecord.update();
        return organizationRecord.into(Organization.class);
    }

    @Override
    public void delete(String organizationId) {
        context.deleteFrom(ORGANIZATION).where(ORGANIZATION.ORGANIZATION_ID.eq(organizationId)).execute();
        context.deleteFrom(ORGANIZATION_CONTACT).where(ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(organizationId)).execute();
    }

    @Override
    public void bindContact(String organizationId, String contactId) {
        Integer count = context.selectCount().from(ORGANIZATION_CONTACT).where(ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(organizationId), ORGANIZATION_CONTACT.CONTACT_ID.eq(contactId)).fetchOne().into(int.class);
        if (count <= 0) {
            context.insertInto(ORGANIZATION_CONTACT).columns(
                    ORGANIZATION_CONTACT.ORGANIZATION_ID,
                    ORGANIZATION_CONTACT.CONTACT_ID
            ).values(
                    organizationId,
                    contactId
            ).execute();
        }
    }

    @Override
    public void unbindContact(String organizationId, String contactId) {
        context.deleteFrom(ORGANIZATION_CONTACT).where(ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(organizationId), ORGANIZATION_CONTACT.CONTACT_ID.eq(contactId)).execute();
    }

    @Override
    public void clearContact(String organizationId) {
        context.deleteFrom(ORGANIZATION_CONTACT).where(ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(organizationId)).execute();
    }

    @Override
    public void bindContacts(String organizationId, List<String> contactIds) {
        for (String contactId : contactIds) {
            this.bindContact(organizationId, contactId);
        }
    }

    @Override
    public List<Contact> contacts(String organizationId) {
        return context.select(CONTACT.fields()).from(ORGANIZATION_CONTACT)
                .leftJoin(CONTACT)
                .on(CONTACT.CONTACT_ID.eq(ORGANIZATION_CONTACT.CONTACT_ID))
                .where(ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(organizationId), CONTACT.STATUS.notEqual(ContactStatusEnum.DELETED))
                .fetch()
                .into(Contact.class);
    }

    @Override
    public List<Organization> listByContactId(String contactId) {
        return context.select(ORGANIZATION.fields())
                .from(ORGANIZATION_CONTACT)
                .leftJoin(ORGANIZATION)
                .on(ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(ORGANIZATION.ORGANIZATION_ID))
                .where(ORGANIZATION_CONTACT.CONTACT_ID.eq(contactId))
                .fetch().into(Organization.class);
    }
}
