
package com.wednesday.demo.swagger;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriTemplate;

import com.fasterxml.classmate.TypeResolver;

import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;


@Configuration
@ConditionalOnProperty(name = "swagger.dirty.fix.enabled", havingValue = "true")
public class SwaggerDirtyFixConfig {
	// @formatter:off
	/**
	 * Because of the new Actuator implementation in Spring Boot 2, all actuator
	 * endpoints are now dynamically mapped to a single handler method:
	 * 
	 * This causes 2 issues: - Because the handler method has an @RequestBody
	 * annotated 'body' parameter, this parameter appears in all actuator endpoints
	 * as body parameter, even for GET and HEAD requests (which cannot have a
	 * request body). These endpoints cannot be executed from the Swagger UI page. -
	 * If an Actuator endpoint contains path parameters, these are not available as
	 * input fields on the Swagger UI page, because no @PathParam annotated
	 * arguments are present on the handler method.
	 *
	 * The Swagger OperationBuilderPlugin below fixes these issues in a somewhat
	 * dirty, but effective way.
	 */
	// @formatter:on
	/**
	 * @param typeResolver Description param
	 * @return Description return
	 */
	@Bean
	public OperationBuilderPlugin operationBuilderPluginForCorrectingActuatorEndpoints(
			final TypeResolver typeResolver) {
		return new OperationBuilderPluginForCorrectingActuatorEndpoints(typeResolver);
	}

	/**
	 * @author Mayur.Patel.188
	 *
	 */
	private static class OperationBuilderPluginForCorrectingActuatorEndpoints implements OperationBuilderPlugin {

		private final TypeResolver typeResolver;

		/**
		 * @param typeResolver Description param
		 */
		OperationBuilderPluginForCorrectingActuatorEndpoints(final TypeResolver typeResolver) {
			this.typeResolver = typeResolver;
		}

		@Override
		public void apply(final OperationContext context) {
			removeBodyParametersForReadMethods(context);
			addOperationParametersForPathParams(context);
		}

		@Override
		public boolean supports(final DocumentationType delimiter) {
			return true;
		}

		/**
		 * @param context Description param
		 */
		private void removeBodyParametersForReadMethods(final OperationContext context) {
			if (HttpMethod.GET.equals(context.httpMethod()) || HttpMethod.HEAD.equals(context.httpMethod())) {
				final List<Parameter> parameters = getParameters(context);
				parameters.removeIf(param -> "body".equals(param.getName()));
			}
		}

		/**
		 * @param context Description param
		 */
		private void addOperationParametersForPathParams(final OperationContext context) {
			final UriTemplate uriTemplate = new UriTemplate(context.requestMappingPattern());

			final List<Parameter> pathParams = uriTemplate.getVariableNames().stream().map(this::createPathParameter)
					.collect(Collectors.toList());

			context.operationBuilder().parameters(pathParams);
		}

		/**
		 * @param pathParam Description param
		 * @return Description return
		 */
		private Parameter createPathParameter(final String pathParam) {
			return new ParameterBuilder().name(pathParam).description(pathParam).required(true)
					.modelRef(new ModelRef("string")).type(typeResolver.resolve(String.class)).parameterType("path")
					.build();
		}

		/**
		 * @param context Description param
		 * @return Description return
		 */
		@SuppressWarnings("unchecked")
		private List<Parameter> getParameters(final OperationContext context) {
			final OperationBuilder operationBuilder = context.operationBuilder();
			try {
				Field paramField = OperationBuilder.class.getDeclaredField("parameters");
				paramField.setAccessible(true);
				return (List<Parameter>) paramField.get(operationBuilder);
			} catch (NoSuchFieldException | IllegalAccessException e) {
				throw new RuntimeException("Unable to modify parameter field!", e);
			}
		}
	}
}