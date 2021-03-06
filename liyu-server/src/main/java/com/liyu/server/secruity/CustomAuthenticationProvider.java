package com.liyu.server.secruity;

import com.liyu.server.service.AccountService;
import com.liyu.server.service.ContactService;
import com.liyu.server.tables.pojos.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private AccountService accountService;
    @Resource
    private ContactService contactService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取认证的用户名 & 密码
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        // 认证逻辑
        Account account = accountService.getByUsernameAndPassword(name, password);
        if (account != null) {
            ArrayList<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new GrantedAuthorityImpl("AUTH_WRITE"));
            return new UsernamePasswordAuthenticationToken(account.getAccountId(), account, authorities);
        } else {
            throw new BadCredentialsException("Unauthorized");
        }
    }

    // 是否可以提供输入类型的认证服务
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}