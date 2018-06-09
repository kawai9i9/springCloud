package com.weilus;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
授权码模式
 http://192.168.100.3:8080/oauth/authorize?client_id=a10086&response_type=code&scope=user_info&redirect_url=http://aa.ccdd

 curl -d 'grant_type=authorization_code&client_id=a10086&scope=user_info&redirect_uri=http://aa.ccdd&code=pg4Vz2'  \
 http://a10086:aabcc@192.168.100.3:8080/oauth/token
**/

/**
密码模式
 curl -d 'grant_type=password&client_id=acme&username=admin&password=weilus' \
 http://acme:acmesecret@192.168.100.3:8080/oauth/token
**/
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.requestMatchers()
                .antMatchers("/login", "/oauth/authorize", "/oauth/token")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().exceptionHandling()
//                .accessDeniedHandler(new OAuth2AccessDeniedHandler())
//                .and()
                .formLogin()
                    .loginPage("/login")
                .permitAll();
    }
}