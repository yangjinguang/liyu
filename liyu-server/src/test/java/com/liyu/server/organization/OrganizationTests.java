package com.liyu.server.organization;

import com.liyu.server.service.OrganizationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationTests {
    @Resource
    private OrganizationService organizationService;

}
