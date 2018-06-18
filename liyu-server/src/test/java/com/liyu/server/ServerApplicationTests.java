package com.liyu.server;

import com.liyu.server.service.*;
import com.liyu.server.tables.pojos.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ServerApplicationTests {
    @Resource
    private TenantService tenantService;
    @Resource
    private AccountService accountService;
    @Resource
    private OrganizationService organizationService;
    @Resource
    private ContactService contactService;
    @Resource
    private GradeService gradeService;
    @Resource
    private RoleService roleService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void md5Test() {
        String str = "123456ccdd";
        String md5 = DigestUtils.md5DigestAsHex(str.getBytes());
        log.info("md5 abc:" + md5);
    }

    @Test
    public void testDataInt() {
        // 创建租户
        Tenant newTenant = new Tenant();
        newTenant.setName("test-tenant");
        newTenant.setDescription("for test");
        newTenant.setAddress("xx address");
        Tenant tenant = tenantService.create(newTenant);

//        // 创建组织
//        Organization newOrganization = new Organization();
//        newOrganization.setTenantId(tenant.getTenantId());
//        newOrganization.setName("全公司");
//        newOrganization.setDescription("全公司");
//        Organization organization = organizationService.create(newOrganization);

        // 创建帐号
        Account newAccount = new Account();
        newAccount.setUsername("root");
        newAccount.setPassword("123456");
        newAccount.setPhone("10000000000");
        newAccount.setEmail("root@ly.com");
        newAccount.setWxOpenId("");
        Account account = accountService.create(newAccount);

        // 创建联系人
        Contact newContact = new Contact();
        newContact.setAccountId(account.getAccountId());
        newContact.setName("杨");
        newContact.setEmail("yang@ly.com");
        newContact.setPhone("10000000000");
        newContact.setTenantId(tenant.getTenantId());
        Contact contact = contactService.create(newContact, account.getAccountId(), tenant.getTenantId());

        // 绑定帐号到租户
//        tenantService.bindAccount(tenant.getTenantId(), account.getAccountId());

//        // 绑定帐号到组织
//        organizationService.bindContact(organization.getOrganizationId(), contact.getContactId());

        Grade newGrade = new Grade();
        newGrade.setName("托班");
        newGrade.setLevel(0);
        gradeService.create(newGrade);

        newGrade.setName("小班");
        newGrade.setLevel(1);
        gradeService.create(newGrade);

        newGrade.setName("中班");
        newGrade.setLevel(2);
        gradeService.create(newGrade);

        newGrade.setName("大班");
        newGrade.setLevel(3);
        gradeService.create(newGrade);

        newGrade.setName("学前班");
        newGrade.setLevel(4);
        gradeService.create(newGrade);

        Role newRole = new Role();
        newRole.setRoleId("teacher-1");
        newRole.setName("老师");
        newRole.setTenantId("root");
        newRole.setLocked(true);
        roleService.create(newRole);

        newRole.setRoleId("teacher-2");
        newRole.setName("班主任老师");
        newRole.setTenantId("root");
        newRole.setLocked(true);
        roleService.create(newRole);

        newRole.setRoleId("teacher-3");
        newRole.setName("生活老师");
        newRole.setTenantId("root");
        newRole.setLocked(true);
        roleService.create(newRole);

    }

    @Test
    public void paginationTest() {
        Integer total = 110;
        Integer size = 10;
        Integer page = 1;
        int i = total % size;
        log.info("%: " + i);
        int totalPages = total / size;
        if (i > 0) {
            totalPages++;
        }
        log.info("totalPages:" + totalPages);
    }

}
