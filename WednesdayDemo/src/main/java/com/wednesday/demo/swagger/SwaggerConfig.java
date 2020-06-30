
package com.wednesday.demo.swagger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.OAuthBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private static final int CODE_500 = 500;
	private static final int CODE_401 = 401;
	private static final int CODE_406 = 406;
	private static final int CODE_403 = 403;

	private static final List<ResponseMessage> OVERRIIDING_RESPONSE_MESSAGE = new ArrayList<>(
			Arrays.asList(new ResponseMessageBuilder().code(CODE_500).message("Internal Server Error").build(),
					new ResponseMessageBuilder().code(CODE_401).message("Unauthorized").build(),
					new ResponseMessageBuilder().code(CODE_406).message("Not Acceptable").build(),
					new ResponseMessageBuilder().code(CODE_403).message("Forbidden!").build()));


	@Value("${oauth.loginCustomPath}")
	private String loginCustomPath;

	@Value("${app.base-path}")
	private String appBasePath;


	private static final String AUTH_LINK = "http://localhost:9999";

	@Bean
	public Docket api() {

		return new Docket(DocumentationType.SWAGGER_2).groupName("Project Name API").select()
				.apis(RequestHandlerSelectors.any()).paths(PathSelectors.ant(appBasePath + "/api/**")).build()
				.apiInfo(apiInfo()).useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, OVERRIIDING_RESPONSE_MESSAGE)
				.securitySchemes(Arrays.asList(securityScheme())).securityContexts(Arrays.asList(securityContextAPI()));
	}

	@Bean
	public Docket actuatorAPI() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("Project Name Actuator").select()
				.apis(RequestHandlerSelectors.any())
				.build().apiInfo(apiInfo()).useDefaultResponseMessages(false)
				.globalResponseMessage(RequestMethod.GET, OVERRIIDING_RESPONSE_MESSAGE)
				.securitySchemes(Arrays.asList(securityScheme()))
				.securityContexts(Arrays.asList(securityContextActuatorAPI()));
	}


	private SecurityContext securityContextAPI() {
		return SecurityContext.builder()
				.securityReferences(Arrays.asList(new SecurityReference("spring_oauth", scopes())))
				.forPaths(PathSelectors.regex(appBasePath + "/api.*")).build();
	}

	private SecurityContext securityContextActuatorAPI() {
		return SecurityContext.builder()
				.securityReferences(Arrays.asList(new SecurityReference("spring_oauth", scopes())))
				.build();
	}

	/**
	 * @return Description return value
	 */
	private SecurityScheme securityScheme() {
		GrantType grantType = new ResourceOwnerPasswordCredentialsGrant(AUTH_LINK + loginCustomPath);
		SecurityScheme oauth = new OAuthBuilder().name("spring_oauth").grantTypes(Arrays.asList(grantType))
				.scopes(Arrays.asList(scopes())).build();
		return oauth;
	}

	/**
	 * @return Description return value
	 */
	private AuthorizationScope[] scopes() {
		AuthorizationScope[] scopes = { new AuthorizationScope("read", "read all"),
				new AuthorizationScope("trust", "trust all"), new AuthorizationScope("write", "write all") };
		return scopes;
	}

	/**
	 * @return Description return value
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Wednesday API").description("Description")
				.contact(new Contact("Jignesh Rathod","" ,"jignesh.rathod7715@gmail.com")).build();
	}
}
