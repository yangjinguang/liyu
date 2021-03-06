package com.liyu.server.service.impl;

import com.liyu.server.enums.ContactStatusEnum;
import com.liyu.server.enums.GenderEnum;
import com.liyu.server.service.ContactService;
import com.liyu.server.tables.pojos.Contact;
import com.liyu.server.tables.records.ContactRecord;
import com.liyu.server.utils.CommonUtils;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.exception.NoDataFoundException;
import org.jooq.types.ULong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;

import static com.liyu.server.tables.Contact.CONTACT;
import static com.liyu.server.tables.OrganizationContact.ORGANIZATION_CONTACT;

@Service
public class ContactServiceImpl implements ContactService {
    @Resource
    private DSLContext context;

    @Override
    public Integer countByTenantId(String tenantId, String searchText, String name, String phone, String organizationId) {
        HashSet<Condition> conditions = new HashSet<>();
        conditions.add(CONTACT.TENANT_ID.eq(tenantId));
        if (searchText != null && !searchText.isEmpty()) {
            conditions.add(CONTACT.NAME.like("%" + searchText + "%").or(CONTACT.EMAIL.like("%" + searchText + "%").or(CONTACT.PHONE.like("%" + searchText + "%"))));
        }
        if (name != null && !name.isEmpty()) {
            conditions.add(CONTACT.NAME.like("%" + name + "%"));
        }
        if (phone != null && !phone.isEmpty()) {
            conditions.add(CONTACT.PHONE.like("%" + phone + "%"));
        }
        if (organizationId != null && !organizationId.isEmpty()) {
            conditions.add(ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(organizationId));
            return context.selectCount().from(ORGANIZATION_CONTACT)
                    .leftJoin(CONTACT)
                    .on(CONTACT.CONTACT_ID.eq(ORGANIZATION_CONTACT.CONTACT_ID))
                    .where(conditions)
                    .fetchOne()
                    .into(int.class);
        } else {
            return context.selectCount().from(CONTACT)
                    .where(conditions)
                    .fetchOne()
                    .into(int.class);
        }
    }

    @Override
    public List<Contact> listByTenantId(String tenantId, Integer offset, Integer size, String searchText, String name, String phone, String organizationId) {
        HashSet<Condition> conditions = new HashSet<>();
        conditions.add(CONTACT.TENANT_ID.eq(tenantId));
        conditions.add(CONTACT.STATUS.notEqual(ContactStatusEnum.DELETED));
        if (searchText != null && !searchText.isEmpty()) {
            conditions.add(CONTACT.NAME.like("%" + searchText + "%").or(CONTACT.EMAIL.like("%" + searchText + "%").or(CONTACT.PHONE.like("%" + searchText + "%"))));
        }
        if (name != null && !name.isEmpty()) {
            conditions.add(CONTACT.NAME.like("%" + name + "%"));
        }
        if (phone != null && !phone.isEmpty()) {
            conditions.add(CONTACT.PHONE.like("%" + phone + "%"));
        }
        if (organizationId != null && !organizationId.isEmpty()) {
            conditions.add(ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(organizationId));
            return context.select(CONTACT.fields()).from(ORGANIZATION_CONTACT)
                    .leftJoin(CONTACT)
                    .on(ORGANIZATION_CONTACT.CONTACT_ID.eq(CONTACT.CONTACT_ID))
                    .where(conditions)
                    .fetch()
                    .into(Contact.class);
        } else {
            return context.selectFrom(CONTACT)
                    .where(conditions)
                    .fetch()
                    .into(Contact.class);
        }

    }

    @Override
    public Contact create(Contact newContact, String accountId, String tenantId) {
        return context.insertInto(CONTACT).columns(
                CONTACT.CONTACT_ID,
                CONTACT.ACCOUNT_ID,
                CONTACT.TENANT_ID,
                CONTACT.NAME,
                CONTACT.GENDER,
                CONTACT.AVATAR,
                CONTACT.PHONE,
                CONTACT.EMAIL,
                CONTACT.ROLE_ID
        ).values(
                CommonUtils.UUIDGenerator(),
                accountId,
                tenantId,
                newContact.getName(),
                newContact.getGender(),
                newContact.getAvatar(),
                newContact.getPhone(),
                newContact.getEmail(),
                newContact.getRoleId()
        ).returning().fetchOne().into(Contact.class);
    }

    @Override
    public Contact update(String contactId, Contact newContact) {
        ContactRecord contactRecord = context.selectFrom(CONTACT).where(CONTACT.CONTACT_ID.eq(contactId))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("联系人不存在"));
        String name = newContact.getName();
        if (name != null && !name.isEmpty()) {
            contactRecord.setName(name);
        }
        GenderEnum gender = newContact.getGender();
        if (gender != null) {
            contactRecord.setGender(gender);
        }
        String avatar = newContact.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            contactRecord.setAvatar(avatar);
        }
        String phone = newContact.getPhone();
        if (phone != null && !phone.isEmpty()) {
            contactRecord.setPhone(phone);
        }
        String email = newContact.getEmail();
        if (email != null && !email.isEmpty()) {
            contactRecord.setEmail(email);
        }
        String roleId = newContact.getRoleId();
        if (roleId != null && !roleId.isEmpty()) {
            contactRecord.setRoleId(roleId);
        }
        ContactStatusEnum status = newContact.getStatus();
        if (status != null) {
            contactRecord.setStatus(status);
        }
        contactRecord.update();
        return contactRecord.into(Contact.class);
    }

    @Override
    public List<Contact> getByAccountId(String accountId) {
        return context.selectFrom(CONTACT).where(CONTACT.ACCOUNT_ID.eq(accountId)).fetch().into(Contact.class);
    }

    @Override
    public Contact getByAccountIdAndTenantId(String accountId, String tenantId) {
        return context.selectFrom(CONTACT).where(CONTACT.ACCOUNT_ID.eq(accountId), CONTACT.TENANT_ID.eq(tenantId))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("未找到用户"))
                .into(Contact.class);
    }

    @Override
    public Contact getById(ULong id) {
        return context.selectFrom(CONTACT)
                .where(CONTACT.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("未找到此联系人"))
                .into(Contact.class);
    }

    @Override
    public Integer countByOrganizationId(String organizationId) {
        return context.selectCount().from(ORGANIZATION_CONTACT)
                .leftJoin(CONTACT)
                .on(CONTACT.CONTACT_ID.eq(ORGANIZATION_CONTACT.CONTACT_ID))
                .where(ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(organizationId), CONTACT.STATUS.notEqual(ContactStatusEnum.DELETED))
                .fetchOne()
                .into(int.class);
    }

    @Override
    public List<Contact> getByOrganizationId(String organizationId) {
        return context.select(CONTACT.fields()).from(ORGANIZATION_CONTACT)
                .leftJoin(CONTACT)
                .on(CONTACT.CONTACT_ID.eq(ORGANIZATION_CONTACT.CONTACT_ID))
                .where(ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(organizationId), CONTACT.STATUS.notEqual(ContactStatusEnum.DELETED))
                .fetch()
                .into(Contact.class);
    }


    @Override
    public void bindOrganization(String contactId, String organizationId) {
        Integer count = context.selectCount().from(ORGANIZATION_CONTACT).where(ORGANIZATION_CONTACT.CONTACT_ID.eq(contactId), ORGANIZATION_CONTACT.ORGANIZATION_ID.eq(organizationId)).fetchOne().into(int.class);
        if (count <= 0) {
            context.insertInto(ORGANIZATION_CONTACT).columns(
                    ORGANIZATION_CONTACT.CONTACT_ID,
                    ORGANIZATION_CONTACT.ORGANIZATION_ID
            ).values(
                    contactId,
                    organizationId
            ).execute();
        }
    }

    @Override
    public void bindOrganizations(String contactId, List<String> organizationIds) {
        for (String organizationId : organizationIds) {
            this.bindOrganization(contactId, organizationId);
        }
    }

    @Override
    public void unbindOrganizationsAll(String contactId) {
        context.deleteFrom(ORGANIZATION_CONTACT).where(ORGANIZATION_CONTACT.CONTACT_ID.eq(contactId)).execute();
    }

    @Override
    public Contact changeStatus(ULong id, ContactStatusEnum status) {
        ContactRecord contactRecord = context.selectFrom(CONTACT)
                .where(CONTACT.ID.eq(id))
                .fetchOptional()
                .orElseThrow(() -> new NoDataFoundException("contact not found"));
        contactRecord.setStatus(status);
        contactRecord.update();
        return contactRecord.into(Contact.class);
    }


}
