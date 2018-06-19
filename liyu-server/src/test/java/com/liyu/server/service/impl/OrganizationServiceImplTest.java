package com.liyu.server.service.impl;

import com.liyu.server.model.OrganizationExtend;
import com.liyu.server.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.List;

import static org.junit.Assert.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationServiceImplTest {
    @Resource
    private OrganizationService organizationService;

    @Test
    public void listByTenantId() {
        List<OrganizationExtend> organizationExtends = organizationService.listByTenantId("1849f5ce7c634d51bae5be250a65d74f", 1, 20, null);
        log.info(organizationExtends.toString());
    }
}