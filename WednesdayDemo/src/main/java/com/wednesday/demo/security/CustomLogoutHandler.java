
package com.wednesday.demo.security;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutHandler;


public class CustomLogoutHandler implements LogoutHandler {

	private static final String BEARER_AUTHENTICATION = "Bearer";
	private static final String HEADER_AUTHORIZATION = "authorization";

	@Autowired
	private TokenStore tokenStore;

	@Override
	public void logout(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) {
		String token = request.getHeader(HEADER_AUTHORIZATION);

		if (token != null && token.startsWith(BEARER_AUTHENTICATION)) {
			String accessTokenValue = token.split(" ")[1];

			OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(accessTokenValue);
			if (oAuth2AccessToken != null) {
				OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
				if (oAuth2RefreshToken != null) {
					tokenStore.removeRefreshToken(oAuth2RefreshToken);
				}
				tokenStore.removeAccessToken(oAuth2AccessToken);
			}
		}
		response.setStatus(HttpServletResponse.SC_OK);
	}


	public boolean forceLogout(final String clientId, final String userName) {
		try {
			Collection<OAuth2AccessToken> tokenCollections = tokenStore.findTokensByClientIdAndUserName(clientId,
					userName);
			Iterator<OAuth2AccessToken> oAuth2AccessTokenterator = tokenCollections.iterator();
			while (oAuth2AccessTokenterator.hasNext()) {
				OAuth2AccessToken oAuth2AccessToken = tokenStore
						.readAccessToken(oAuth2AccessTokenterator.next().getValue());
				if (oAuth2AccessToken != null) {
					OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
					if (oAuth2RefreshToken != null) {
						tokenStore.removeRefreshToken(oAuth2RefreshToken);
					}
					tokenStore.removeAccessToken(oAuth2AccessToken);

				}
			}
			return true;
		} catch (Exception ex) {

		}
		return false;
	}


	public void invalidAccessToken(final OAuth2AuthenticationDetails authDetail, final TokenStore authentication) {
		String token = authDetail.getTokenValue();

		if (token != null) {
			OAuth2AccessToken oAuth2AccessToken = authentication.readAccessToken(token);
			if (oAuth2AccessToken != null) {
				OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
				if (oAuth2RefreshToken != null) {
					authentication.removeRefreshToken(oAuth2RefreshToken);
				}
				authentication.removeAccessToken(oAuth2AccessToken);
			}
		}
	}


	@SuppressWarnings("rawtypes")
	public void invalidAccessTokenForWeb(final WebAuthenticationDetails authDetail, final TokenStore authentication,
			final HttpServletRequest request, final HttpServletResponse response) {
		String token = authDetail.getSessionId();

		if (token != null) {
			SecurityContextHolder.getContext().setAuthentication(null);
			SecurityContextHolder.clearContext();
			HttpSession hs = request.getSession();
			Enumeration e = hs.getAttributeNames();
			while (e.hasMoreElements()) {
				String attr = (String) e.nextElement();
				hs.setAttribute(attr, null);
			}
			removeCookies(request);
			hs.invalidate();
		}
	}

	public static void removeCookies(final HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				cookies[i].setMaxAge(0);
			}
		}
	}
}