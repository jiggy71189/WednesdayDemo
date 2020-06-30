//
//package com.wednesday.demo.security;
//
//import java.io.IOException;
//import java.nio.file.attribute.UserPrincipal;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
//
//	@Value("${app.base-path}")
//	private String basePath;
//
//
//	@Override
//	public void onLogoutSuccess(final HttpServletRequest httpServletRequest,
//			final HttpServletResponse httpServletResponse, final Authentication authentication)
//			throws IOException, ServletException {
//		if (authentication != null && authentication.getDetails() != null) {
//			try {
//
//				UserPrincipal usrPrincipal = (UserPrincipal) authentication.getPrincipal();
//
//				httpServletRequest.getSession().invalidate();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//	}
//}