package com.tekpyramid.sp.configuration;

import java.util.Properties;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tekpyramid.sp.service.JwtService;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "auditorProvider")
public class JavaConfig {
	
	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.port}")
	private int port;

	@Value("${spring.mail.username}")
	private String username;

	@Value("${spring.mail.password}")
	private String password;

	@Bean
	@Lazy
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(port);
		mailSender.setUsername(username);
		mailSender.setPassword(password);

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		return mailSender;
	}

	@Bean
	@Lazy
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity ,@Autowired JwtFilter jwtFilter ,@Autowired JwtService jwtService) throws Exception {
		return httpSecurity.csrf(csrf->csrf.disable())
				.sessionManagement(cust->cust.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(
				req -> {
					try {
						req
						.requestMatchers(
								"/supportDesk/v1/login",
								"/supportDesk/v1/forgotPassword",
								"/oauth2/**",
								"/index.html",
								"/swagger-ui.html",
								"/swagger-ui/**",
							    "/v3/api-docs/**",
								"/supportDesk/v1/save").permitAll()
						.requestMatchers(
								"/supportDesk/v1/admin/**",
								"/supportDesk/comment/v1/deleteComment",
								"/supportDesk/comment/v1/getAll",
								"/supportDesk/ticket/v1/delete").hasRole("ADMIN") 
						.requestMatchers("/supportDesk/v1/getAll/**").hasAnyRole("ADMIN","SUPPORT")
						.anyRequest()
						.authenticated();
					} catch (Exception e) {
						e.printStackTrace();
					}
				})
				.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class)
				.httpBasic(Customizer.withDefaults())
			.oauth2Client(Customizer.withDefaults())
			.oauth2Login(oauth2 -> oauth2
				    .successHandler((request, response, authentication) -> {
				        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
				        String email = oAuth2User.getAttribute("email");
				        String jwt = jwtService.generateToken(email);
				        response.setContentType("application/json");
				        response.getWriter().write("{\"token\": \"" + jwt + "\"}");
				    }))
				.build();

	}
@Bean
public AuthenticationProvider authenticationProvider(@Autowired UserDetailsService userDetailsService) {
	DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
	daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(12));
	daoAuthenticationProvider.setUserDetailsService(userDetailsService);
	return daoAuthenticationProvider;
}
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
	return authenticationConfiguration.getAuthenticationManager();
}
@Bean
public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("support_desk_apis")
            .version("1.0")
            .description("This is a sample APIs for support desk.")
        );
}
@Bean
public AuditorAware<String> auditorProvider() {
    return new AuditorAwareImpl();
}


}