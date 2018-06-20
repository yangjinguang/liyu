package com.liyu.server.controller;

import com.liyu.server.model.ContactDetail;
import com.liyu.server.model.OrganizationDetail;
import com.liyu.server.service.AccountService;
import com.liyu.server.service.ContactService;
import com.liyu.server.service.OrganizationService;
import com.liyu.server.tables.pojos.Account;
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
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Api(value = "联系人", description = "联系人操作", tags = {"联系人接口"})
@RestController
@Slf4j
@RequestMapping(value = "/api/contacts")
public class ContactController {
    @Resource
    private ContactService contactService;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private AccountService accountService;

    @ApiOperation(value = "获取联系人列表", notes = "")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = false, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页条数", required = false, dataType = "int", paramType = "query")
    })
    @RequestMapping(value = "", method = RequestMethod.GET)
    public APIResponse list(
            @RequestHeader(value = "X-TENANT-ID") String tenantId,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(value = "searchText", required = false) String searchText,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "organizationId", required = false) String organizationId) {
        Integer total = contactService.countByTenantId(tenantId, searchText, name, phone, organizationId);
        List<Contact> contacts = contactService.listByTenantId(tenantId, (page - 1) * size, size, searchText, name, phone, organizationId);
        ArrayList<ContactDetail> contactDetails = new ArrayList<>();
        for (Contact contact : contacts) {
            ContactDetail contactDetail = new ContactDetail(contact);
            List<Organization> organizations = organizationService.listByContactId(contact.getContactId());
            ArrayList<String> organizationIds = new ArrayList<>();
            ArrayList<String> organizationNames = new ArrayList<>();
            for (Organization organization : organizations) {
                organizationIds.add(organization.getOrganizationId());
                organizationNames.add(organization.getName());
            }
            contactDetail.setOrganiztionIds(organizationIds);
            contactDetail.setOrganizations(organizations);
            contactDetail.setOrganizationTitle(String.join(",", organizationNames));
            contactDetails.add(contactDetail);
        }
        return APIResponse.withPagination(contactDetails, total, page, size);
    }

    @ApiOperation(value = "创建联系人", notes = "")
    @ResponseBody
    @ApiImplicitParam(name = "newContact", value = "联系人信息", required = true, dataType = "ContactCreateBody")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public APIResponse create(
            @RequestHeader(value = "X-TENANT-ID") String tenantId,
            @RequestBody Contact newContact) {
        Account account = accountService.getByPhone(newContact.getPhone());
        if (account == null) {
            Account newAccount = new Account();
            newAccount.setPhone(newContact.getPhone());
            newAccount.setPassword("123456");
            newAccount.setEmail(newContact.getEmail());
            newAccount.setPhone(newContact.getPhone());
            newAccount.setAvatar(newContact.getAvatar());
            account = accountService.create(newAccount);
        }
        Contact contact = contactService.create(newContact, account.getAccountId(), tenantId);
        return APIResponse.success(contact);
    }

    @ApiOperation(value = "修改用户", notes = "")
    @RequestMapping(value = "/{contactId}", method = RequestMethod.PUT)
    public APIResponse update(@PathVariable String contactId,
                              @RequestBody Contact newContact) {
        Contact contact = contactService.update(contactId, newContact);
        return APIResponse.success(contact);
    }


    @ApiOperation(value = "获取当前用户信息", notes = "")
    @ResponseBody
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public APIResponse profile(HttpServletRequest request,
                               @RequestHeader(value = "X-TENANT-ID") String tenantId) {
        Principal principal = request.getUserPrincipal();
        log.info(principal.getName());
        Contact contact = contactService.getByAccountIdAndTenantId(principal.getName(), tenantId);
        return APIResponse.success(contact);
    }

    @ApiOperation(value = "获取联系人详情", notes = "")
    @ResponseBody
    @ApiImplicitParam(name = "id", value = "ID", required = true, dataType = "Long", paramType = "path")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public APIResponse detail(@PathVariable Long id) {
        Contact contact = contactService.getById(ULong.valueOf(id));
        ContactDetail contactDetail = new ContactDetail(contact);
        List<Organization> organizations = organizationService.listByContactId(contact.getContactId());
        ArrayList<String> organizationIds = new ArrayList<>();
        for (Organization organization : organizations) {
            organizationIds.add(organization.getOrganizationId());
        }
        contactDetail.setOrganiztionIds(organizationIds);
        contactDetail.setOrganizations(organizations);
        return APIResponse.success(contactDetail);
    }

}
