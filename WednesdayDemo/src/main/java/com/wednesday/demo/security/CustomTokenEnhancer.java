///**
// * Copyright (c) 2018, System Level Solutions (India) Pvt. Ltd.
// * 
// */
//package com.wednesday.demo.security;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.TokenEnhancer;
//
//import com.wednesday.demo.model.UserPrincipal;
//
///**
// * @author Hitesh.Kumbhar.441
// *
// */
//public class CustomTokenEnhancer implements TokenEnhancer {
//
//	private static final String ROLESPLITTER = "_";
//
//
//	@Override
//	public OAuth2AccessToken enhance(final OAuth2AccessToken accessToken, final OAuth2Authentication authentication) {
//		// User user = (User) authentication.getPrincipal();
//		final Map<String, Object> additionalInfo = new HashMap<>();
//		// String subscription_token = UUID.randomUUID().toString();
//		// additionalInfo.put("subscription_token", subscription_token);
//		// additionalInfo.put("authorities", user.getAuthorities());
//		UserPrincipal principal = (UserPrincipal) authentication.getUserAuthentication().getPrincipal();
//		// additionalInfo.put("appId", principal.getTokens());
//
//		String[] roles = principal.getRoles();
////		for (String role : roles) {
////			roles[0] = role.split(ROLESPLITTER)[1];
////		}
//
//		additionalInfo.put("roles", roles);
//		additionalInfo.put("name", principal.getName());
//		additionalInfo.put("feature", principal.getFeature());
//
//		((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
//		return accessToken;
//	}
//}