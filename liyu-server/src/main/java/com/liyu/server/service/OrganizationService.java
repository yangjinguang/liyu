package com.liyu.server.service;

import com.liyu.server.model.OrganizationDetail;
import com.liyu.server.model.OrganizationTree;
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

    /**
     * 根据租户ID获取组织列表
     *
     * @param tenantId 租户ID
     * @return List<Organization>
     */
    List<OrganizationDetail> listByTenantId(String tenantId);

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
     * @param id              组织ID
     * @param newOrganization 组织信息
     * @return Organization
     */
    Organization update(ULong id, Organization newOrganization);

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

    /**
     * 根据组织ID获取账户个数
     *
     * @param organizationId
     * @return
     */
    Integer contactsCount(String organizationId);

    /**
     * 根据组织ID获取账户列表
     *
     * @param organizationId
     * @param offset
     * @param size
     * @return
     */
    List<Contact> contacts(String organizationId, Integer offset, Integer size);

    List<Organization> listByContactId(String contactId);
}
