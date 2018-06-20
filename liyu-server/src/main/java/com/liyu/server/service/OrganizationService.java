package com.liyu.server.service;

import com.liyu.server.model.OrganizationExtend;
import com.liyu.server.tables.pojos.Contact;
import com.liyu.server.tables.pojos.Organization;
import org.jooq.types.ULong;

import java.util.List;

public interface OrganizationService {

    /**
     * 根据ID获取组织详情
     *
     * @param id ID
     * @return Organization
     */
    Organization byId(ULong id);

    Organization byOrganizationId(String orginaizationId);

    Integer countByTenantId(String tenantId, String searchText, String name, String gradeId);

    /**
     * 根据租户ID获取组织列表
     *
     * @param tenantId 租户ID
     * @return List<Organization>
     */
    List<OrganizationExtend> listByTenantId(String tenantId, Integer offset, Integer limit, String searchText, String name, String gradeId);

    List<Organization> miniListByTenantId(String tenantId, Integer offset, Integer limit, String searchText,String name, String gradeId);

    /**
     * 创建新组织
     *
     * @param newOrganization 组织信息
     * @return Organization
     */
    Organization create(Organization newOrganization);

    /**
     * 更新组织
     *
     * @param organizationId  组织ID
     * @param newOrganization 组织信息
     * @return Organization
     */
    Organization update(String organizationId, Organization newOrganization);

    /**
     * 删除组织
     *
     * @param organizationId 组织ID
     */
    void delete(String organizationId);

    /**
     * 绑定帐号到组织
     *
     * @param organizationId 组织ID
     * @param accountId      帐号ID
     */
    void bindContact(String organizationId, String accountId);

    /**
     * 取消帐号绑定
     *
     * @param organizationId 组织ID
     * @param accountId      帐号ID
     */
    void unbindContact(String organizationId, String accountId);

    void clearContact(String organizationId);

    void bindContacts(String organizationId, List<String> contactIds);

    /**
     * 根据组织ID获取账户列表
     *
     * @param organizationId
     * @return
     */
    List<Contact> contacts(String organizationId);

    List<Organization> listByContactId(String contactId);
}
