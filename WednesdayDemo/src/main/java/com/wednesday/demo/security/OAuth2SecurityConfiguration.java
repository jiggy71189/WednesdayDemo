package com.wednesday.demo.security;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenStoreUserApprovalHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity 
public class OAuth2SecurityConfiguration { 

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoderVal;

	/**
	 * @param auth Description param
	 * @throws Exception Description
	 */
	@Autowired
	public void globalUserDetails(final AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsServiceBean()).passwordEncoder(passwordEncoderVal);
	}

	@Bean
	public CustomLogoutHandler customLogoutHandler() {
		return new CustomLogoutHandler();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		String idForEncode = "bcrypt"; // Production
		Map<String, PasswordEncoder> encoders = new HashMap<>();
		encoders.put("bcrypt", new BCryptPasswordEncoder());
//		 encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("scrypt", new SCryptPasswordEncoder());
		// encoders.put("sha256", new StandardPasswordEncoder());

		PasswordEncoder passwordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
		return passwordEncoder;
	}

	@Bean
	public TokenStore tokenStore() {
		return new InMemoryTokenStore();
	}

	@Bean
	public JwtAccessTokenConverter jwtTokenEnhancer() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setSigningKey("Demo-Key-1");
		return converter;
	}

	@Bean
	@Autowired
	public TokenStoreUserApprovalHandler userApprovalHandler(final TokenStore tokenStore) {
		TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
		handler.setTokenStore(tokenStore);
		handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
		handler.setClientDetailsService(clientDetailsService);
		return handler;
	}

	@Bean
	@Autowired
	public ApprovalStore approvalStore(final TokenStore tokenStore) throws Exception {
		TokenApprovalStore store = new TokenApprovalStore();
		store.setTokenStore(tokenStore);
		return store;
	}

	@Bean
	public UserDetailsService userDetailsServiceBean() throws Exception {
		return new AppUserDetailsServiceDAO();
	}

	private static final int API_WEB_SECURITY_CONFIGURATION_ADAPTER = 100;

	@Configuration
	@Order(API_WEB_SECURITY_CONFIGURATION_ADAPTER)
	public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {

		@Value("${app.base-path}")
		private String appBasePath;

		@Override
		public void configure(final WebSecurity web) throws Exception {
			// web.ignoring().antMatchers("/nebulae/api/swagger.json");
			web.ignoring().antMatchers(appBasePath + "/api/add/admin");
			
		}

		@Override
		protected void configure(final HttpSecurity http) throws Exception {
			http.anonymous().disable().requestMatchers().antMatchers(appBasePath + "/api/**").and().authorizeRequests()
					.anyRequest().authenticated().and().exceptionHandling()
					.accessDeniedHandler(new OAuth2AccessDeniedHandler());
		}

		@Override
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

	}

	private static final int FORM_LOGIN_WEB_SECURITY_CONFIGURATION_ADAPTER = 101;

	@Configuration
	@Order(FORM_LOGIN_WEB_SECURITY_CONFIGURATION_ADAPTER)
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	public static class FormLoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

		@Value("${app.base-path}")
		private String appBasePath;


		@Override
		public void configure(final WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/js/**", "/ico/**", "/css/**", "/images/**", "/webjars/**");
		}

		@Override
		protected void configure(final HttpSecurity http) throws Exception {

			http.authorizeRequests().antMatchers(appBasePath + "/api/add/user")
			.access("hasRole('ROLE_ADMIN')").and().exceptionHandling()
					.accessDeniedHandler(getAccessDeniedHandler()).and().exceptionHandling();
		}

		@Bean
		public SessionRegistry sessionRegistry() {
			return new SessionRegistryImpl();
		}

		@Bean
		public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
			return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
		}

	}

	/**
	 * 
	 * @return Description
	 */
	public static AccessDeniedHandler getAccessDeniedHandler() {
		return new MyAccessDeniedHandler();
	}

	public static AuthenticationFailureHandler customAuthenticationFailureHandler() {
		return new CustomAuthenticationFailureHandler();
	}

}