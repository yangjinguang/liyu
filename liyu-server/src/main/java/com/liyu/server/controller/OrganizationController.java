package com.liyu.server.controller;

import com.liyu.server.model.OrganizationCreateBody;
import com.liyu.server.model.OrganizationDetail;
import com.liyu.server.model.OrganizationExtend;
import com.liyu.server.service.AccountService;
import com.liyu.server.service.ContactService;
import com.liyu.server.service.OrganizationService;
import com.liyu.server.tables.pojos.Contact;
import com.liyu.server.tables.pojos.Organization;
import com.liyu.server.utils.APIResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jooq.types.ULong;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Api(value = "组织", description = "组织操作", tags = {"组织接口"})
@RestController
@RequestMapping(value = "/api/organizations")
public class OrganizationController {
    @Resource
    private OrganizationService organizationService;
    @Resource
    private ContactService contactService;

    @ApiOperation(value = "获取组织列表", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse list(@RequestHeader(value = "X-TENANT-ID") String tenantId,
                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        log.info("tenantId: " + tenantId);
        Integer total = organizationService.countByTenantId(tenantId);
        List<OrganizationExtend> organizationExtends = organizationService.listByTenantId(tenantId, (page - 1) * size, size);
        ArrayList<OrganizationDetail> organizationDetails = new ArrayList<>();
        for (OrganizationExtend organizationExtend : organizationExtends) {
            OrganizationDetail organizationDetail = new OrganizationDetail(organizationExtend);
            List<Contact> contacts = organizationService.contacts(organizationExtend.getOrganizationId());
            organizationDetail.setContacts(contacts);
            ArrayList<String> contactIds = new ArrayList<>();
            ArrayList<String> contactNames = new ArrayList<>();
            for (Contact contact : contacts) {
                contactIds.add(contact.getContactId());
                contactNames.add(contact.getName());
            }
            organizationDetail.setContactNameTitle(String.join(",", contactNames));
            organizationDetail.setContactIds(contactIds);
            organizationDetails.add(organizationDetail);
        }
        return APIResponse.withPagination(organizationDetails, total, page, size);
    }

    @ApiOperation(value = "获取组织详情", notes = "")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public APIResponse list(@PathVariable(value = "id", required = true) Long id) {
        Organization organization = organizationService.byId(ULong.valueOf(id));
        return APIResponse.success(organization);
    }

    @ApiOperation(value = "创建组织", notes = "")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse create(@RequestHeader(value = "X-TENANT-ID") String tenantId,
                              @RequestBody OrganizationCreateBody newOrganization) {
        newOrganization.setTenantId(tenantId);
        Organization organization = organizationService.create(newOrganization);
        organizationService.bindContacts(organization.getOrganizationId(), newOrganization.getContactIds());
        return APIResponse.success(organization);
    }


    @ApiOperation(value = "更新组织", notes = "")
    @RequestMapping(value = "/{organizationId}", method = RequestMethod.PUT)
    public APIResponse update(@PathVariable String organizationId,
                              @RequestBody OrganizationCreateBody newOrganization) {
        Organization organization = organizationService.update(organizationId, newOrganization);
        List<String> contactIds = newOrganization.getContactIds();
        if (contactIds != null && contactIds.size() > 0) {
            organizationService.clearContact(organizationId);
            organizationService.bindContacts(organizationId, contactIds);
        }
        return APIResponse.success(organization);
    }

    @ApiOperation(value = "删除组织", notes = "")
    @ApiImplicitParam(name = "organizationId", value = "组织 ID", required = true, dataType = "String", paramType = "path")
    @RequestMapping(value = "/{organizationId}", method = RequestMethod.DELETE)
    public APIResponse delete(@PathVariable String organizationId) {
        organizationService.delete(organizationId);
        return APIResponse.success("success");
    }

//    @ApiOperation(value = "绑定联系人", notes = "")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "path"),
//            @ApiImplicitParam(name = "accountIds", value = "用户ID列表", required = true, dataType = "List<String>", paramType = "body"),
//    })
//    @RequestMapping(value = "/{id}/bindAccounts", method = RequestMethod.PUT)
//    public APIResponse bindAccounts(@PathVariable Long id,
//                                    @RequestBody List<String> accountIds) {
//        Organization organization = organizationService.byId(ULong.valueOf(id));
//        for (String accountId : accountIds) {
//            log.info("bind accountId: " + accountId);
//            organizationService.bindContact(organization.getOrganizationId(), accountId);
//        }
//        return APIResponse.success("success");
//    }

    @ApiOperation(value = "取消绑定联系人", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "contactIds", value = "联系人ID列表", required = true, dataType = "List<String>", paramType = "body"),
    })
    @RequestMapping(value = "/{id}/unbindContacts", method = RequestMethod.PUT)
    public APIResponse unbindAccounts(@PathVariable Long id,
                                      @RequestBody List<String> contactIds) {
        Organization organization = organizationService.byId(ULong.valueOf(id));
        for (String contactId : contactIds) {
            log.info("unbind contactId: " + contactId);
            organizationService.unbindContact(organization.getOrganizationId(), contactId);
        }
        return APIResponse.success("success");
    }

    @ApiOperation(value = "组织绑定的联系人列表", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "deep", value = "是否获取子节点", required = false, dataType = "Boolean", paramType = "query"),
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "/{id}/contacts", method = RequestMethod.GET)
    public APIResponse list(@PathVariable(value = "id", required = true) Long id,
                            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
                            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size) {
        Organization organization = organizationService.byId(ULong.valueOf(id));
        List<Contact> contacts = organizationService.contacts(organization.getOrganizationId());
        return APIResponse.success(contacts);
    }
}
