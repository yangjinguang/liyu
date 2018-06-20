package com.liyu.server.service;

import com.liyu.server.enums.ContactStatusEnum;
import com.liyu.server.tables.pojos.Contact;
import org.jooq.types.ULong;

import java.util.List;

public interface ContactService {
    Integer countByTenantId(String tenantId, String searchText, String name, String phone, String organizationId);

    List<Contact> listByTenantId(String tenantId, Integer offset, Integer size, String searchText, String name, String phone, String organizationId);

    Contact create(Contact newContact, String accountId, String tenantId);

    Contact update(String contactId, Contact newContact);

    List<Contact> getByAccountId(String accountId);

    Contact getByAccountIdAndTenantId(String accountId, String tenantId);

    Contact getById(ULong id);

    Integer countByOrganizationId(String organizationId);

    List<Contact> getByOrganizationId(String organizationId);

    void bindOrganization(String contactId, String organizationId);

    void bindOrganizations(String contactId, List<String> organizationIds);

    void unbindOrganizationsAll(String contactId);

    Contact changeStatus(ULong id, ContactStatusEnum status);
}
