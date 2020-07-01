package com.wednesday.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Jignesh.Rathod
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	private static final String REALM = "MY_OAUTH_REALM";
	
	@Value("${oauth.loginCustomPath}")
	private String oauthLoginCustomPath;

	@Autowired
	private TokenStore tokenStore;

	@Autowired
	private UserApprovalHandler userApprovalHandler;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	@Qualifier("authenticationManagerBean")
	private AuthenticationManager authenticationManager;

	@Override
	public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {

		 clients.inMemory().withClient("wednesday").authorizedGrantTypes("password",
		 "refresh_token")
		 .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT").scopes("read", "write",
		 "trust")
		 .secret(passwordEncoder.encode("wedne$D@y")).accessTokenValiditySeconds(3600).
		 refreshTokenValiditySeconds(86400);
	}

	@Override
	public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

		endpoints.pathMapping("/oauth/token", oauthLoginCustomPath).tokenStore(tokenStore)
				.userApprovalHandler(userApprovalHandler).authenticationManager(authenticationManager);

	}

	@Override
	public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		// Below line allow to use "client_id" and "client_secret" parameters
		// values as basic authentication header
		oauthServer.realm(REALM + "/client");

		// Below line allow to add the "client_id" and "client_secret" in query
		// parameter
		oauthServer.allowFormAuthenticationForClients();
	}

	@Bean
	public DefaultAccessTokenConverter accessTokenConverter() {
		return new DefaultAccessTokenConverter();
	}
}