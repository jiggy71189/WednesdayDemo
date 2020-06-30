/**
 * Copyright (c) 2018, System Level Solutions (India) Pvt. Ltd.
 * 
 */

package com.wednesday.demo.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

/**
 * @author Hitesh.Kumbhar.441
 *
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {
	private static final int ERROR_CODE = 599;

	@Value("${app.base-path}")
	private String basePath;

	@Override
	public void handle(final HttpServletRequest request, final HttpServletResponse response,
			final AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if (accessDeniedException instanceof MissingCsrfTokenException
				|| accessDeniedException instanceof InvalidCsrfTokenException) {

			String ajaxHeader = ((HttpServletRequest) request).getHeader("X-Requested-With");
			if ("XMLHttpRequest".equals(ajaxHeader)) {
				response.setStatus(ERROR_CODE);
				response.sendError(ERROR_CODE, "Ajax Request Denied (Session Expired)");
				// response.sendRedirect( "/nebulink/fota/login?logout");
			} else {
				response.sendRedirect(basePath + "/login?logout");
			}
			
		} else {
			response.sendRedirect(request.getContextPath() + "/nebulink/fota/403");
		}
	}
}
