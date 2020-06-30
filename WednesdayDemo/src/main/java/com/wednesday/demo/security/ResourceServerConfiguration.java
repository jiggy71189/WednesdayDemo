
package com.wednesday.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Value("${app.base-path}")
	private String appBasePath;


	@Override
	public void configure(final HttpSecurity http) throws Exception {

		http.anonymous().disable().requestMatchers().antMatchers(appBasePath + "/api/**").and().authorizeRequests()
				.anyRequest().authenticated().and().exceptionHandling()
				.accessDeniedHandler(new OAuth2AccessDeniedHandler());

	}

}