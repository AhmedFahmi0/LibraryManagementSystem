package com.libraryManagement.libraryManagement.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {
	
//	private final UserServiceImpl userDetailsService;
	private static final String[] WHITE_LIST_URL = {"/api/v1/auth/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"};
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

	    @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    	   http
               .csrf(AbstractHttpConfigurer::disable)
               .authorizeHttpRequests(req ->
                       req.requestMatchers(WHITE_LIST_URL)
                               .permitAll()
                             
                               .anyRequest()
                               .authenticated()
               )
               .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .authenticationProvider(authenticationProvider)
               .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
               
       ;

       return http.build();
	    }
	    
//	    @Bean
//	    public AuthenticationProvider authenticationProvider() {
//	      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//	      authProvider.setUserDetailsService(userDetailsService);
//	      authProvider.setPasswordEncoder(passwordEncoder());
//	      return authProvider;
//	    }

//	    @Bean
//	    public AuditorAware<Integer> auditorAware() {
//	      return new ApplicationAuditAware();
//	    }

//	    @Bean
//	    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//	      return config.getAuthenticationManager();
//	    }

//	    @Bean
//	    public PasswordEncoder passwordEncoder() {
//	      return new BCryptPasswordEncoder();
//	    }

	}
	
	    
