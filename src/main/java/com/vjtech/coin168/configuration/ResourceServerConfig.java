package com.vjtech.coin168.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * <p>Title: ResourceServerConfig</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) jimmytseng 2021</p>
 *
 * <p>Company: vjteck</p>
 *
 * @author jimmytseng
 * @version 1.0
 * @date 2021-07-12
 */
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
	
	
    public RedisConnectionFactory createOauthConnectionFactory() {
    	RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName("127.0.0.1");
        //configuration.setPassword(RedisPassword.of(oauthPwd));
        configuration.setPort(6379);
        configuration.setDatabase(0);
        
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(configuration);
        connectionFactory.setValidateConnection(true);
        connectionFactory.afterPropertiesSet();
        return connectionFactory;
    }
    
    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(createOauthConnectionFactory());
    }
    
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
    	resources.resourceId("vjteck").tokenStore(tokenStore());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // ????????????????????????
        http
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .anonymous()
                .and()
                .authorizeRequests()
                .antMatchers(getAnonymousList())
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .antMatchers("/**")
                .authenticated();
                http.headers().frameOptions().sameOrigin();
    }

    
	private String[] getAnonymousList() {
		//???????????????
		return new String[] {
				"/css/**",
		        "/fonts/**",
		        "/images/**",      
                "/swagger-resources/**",  // swagger-ui resources
                "/configuration/**",      // swagger configuration
		        "/js/**",
		        "/**.html",
		        "/v2/**",
		        "/v3/**",
		        "/login",
		        "/register",
                "/getCoin",
                "/listCoinAll",
                "/listCoinDaily",
                "/verifyCoinCode",
                "/coinDetail",
                "/listPormoteCoin",
                "/resendMail",
                "/verifyCust",
                "/activeCust",
                "/sendMessage",
                "/code"
		};
	}
	

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        //??????????????????????????????
        config.addAllowedOrigin("/*");

        //??????????????????cookie??????????????????????????????????????????cookie????????????
        config.setAllowCredentials(true);

        //??????????????????????????????
        config.addAllowedMethod("/*");
        //config.setAllowedMethods(Arrays.asList("GET", "PUT", "POST","DELETE"));
        //config.addAllowedMethod(HttpMethod.POST);

        //????????????Header
        config.addAllowedHeader("/*");
        //config.addAllowedHeader("x-firebase-auth");

        //???????????????Header??????????????????????????????????????????Header?????????
        config.addExposedHeader("/*");
        //config.addExposedHeader("Content-Type");
        //config.addExposedHeader( "X-Requested-With");
        //config.addExposedHeader("accept");
        //config.addExposedHeader("Origin");
        //config.addExposedHeader( "Access-Control-Request-Method");
        //config.addExposedHeader("Access-Control-Request-Headers");


        //????????????
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);

        //return?????????CorsFilter.
        return new CorsFilter(configSource);
    }
}