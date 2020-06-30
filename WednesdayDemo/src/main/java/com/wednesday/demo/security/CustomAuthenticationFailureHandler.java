
package com.wednesday.demo.security;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;


public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException {
		ObjectMapper objectMapper = new ObjectMapper();

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		Map<String, Object> data = new HashMap<>();
		data.put("timestamp", Calendar.getInstance().getTime());
		data.put("exception", exception.getMessage());

		response.getOutputStream().println(objectMapper.writeValueAsString(data));
	}

}
