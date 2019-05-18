package com.weilus.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.sql.DataSource;

/**
 * Created by liutq on 2018/12/10.
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationServerConfig.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RedisConnectionFactory connectionFactory;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ClientDetailsService clientDetailsService;
    @Autowired
    private DataSource dataSource;
    @Autowired
    private DefaultTokenServices tokenServices;

    @Bean
    @Primary
    public DefaultTokenServices tokenServices(@Autowired JedisConnectionFactory connectionFactory){
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        tokenServices.setTokenStore(new RedisTokenStore(connectionFactory));
        tokenServices.setClientDetailsService(clientDetailsService);
        return tokenServices;
    }

    @Bean
    public UserDetailsService userDetailsService(@Autowired DataSource dataSource){
        JdbcDaoImpl dao =new JdbcDaoImpl();
        dao.setDataSource(dataSource);
        dao.setRolePrefix("ROLE_");
//        dao.setUsersByUsernameQuery("select * from example");//自定义查询用户
//        dao.setAuthoritiesByUsernameQuery("select * example ");//自定义查询用户权限
//        dao.setEnableGroups(true);
//        dao.setGroupAuthoritiesByUsernameQuery("select * example ");//自定义查询用户角色
        return dao;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.userDetailsService(userDetailsService);
        endpoints.tokenStore(new RedisTokenStore(connectionFactory));
        endpoints.tokenServices(tokenServices);
        endpoints.authorizationCodeServices(new JdbcAuthorizationCodeServices(dataSource));
        endpoints.exceptionTranslator((e)-> new ResponseEntity(e.getMessage(), HttpStatus.OK));
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.checkTokenAccess("permitAll()");
    }
}
