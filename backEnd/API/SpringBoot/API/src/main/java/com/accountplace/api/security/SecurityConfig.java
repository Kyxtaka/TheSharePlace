package com.accountplace.api.security;

import com.accountplace.api.exceptions.CustomAccessDeniedHandler;
import com.accountplace.api.exceptions.CustomAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebMvcConfigurer  {

    private final CustomAuthenticationEntryPoint authEntryPoint;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final String APIauthUrl = "/api/auth/**";

    @Autowired
    public SecurityConfig(
            CustomAuthenticationEntryPoint authEntryPoint,
            CustomAccessDeniedHandler accessDeniedHandler
    ) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.authEntryPoint = authEntryPoint;
    }

    /**
     * Configures CORS mappings to allow specific origins and HTTP methods.
     *
     * @param registry The CorsRegistry to configure CORS settings.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "https://theshareplace.hikarizsu.fr/",
                        "http://localhost:4200/"
                )//  WebServer + Angular dev server
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    /**
     * Configures HTTP security settings including session management,
     * authentication and authorization settings, and JWT filtering.
     *
     * @param http The HttpSecurity object used to configure security.
     * @return The configured SecurityFilterChain.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(authEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler)
                )
                //Dont create session on server side
                .sessionManagement( sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // requests Authorizations
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                        authorizationManagerRequestMatcherRegistry
                                .requestMatchers(HttpMethod.POST,APIauthUrl).permitAll()
                                .anyRequest().authenticated()
                )
                //filter
                .addFilterBefore(
                        jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )
                .securityContext( (securityContext) -> securityContext
                        .securityContextRepository(new DelegatingSecurityContextRepository(
                                new RequestAttributeSecurityContextRepository(),
                                new HttpSessionSecurityContextRepository()
                        ))
                )
//                .requiresChannel(channel -> channel
//                        .anyRequest().requiresSecure()
//                )
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    /**
     * Provides the JWT authentication filter bean.
     *
     * @return The JWTAuthenticationFilter bean.
     * @throws Exception If an error occurs during the creation of the filter.
     */
    // Inject JWT Authentication Filter
    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JWTAuthenticationFilter();
    }

    /**
     * Provides a password encoder using BCrypt for hashing passwords.
     *
     * @return A BCryptPasswordEncoder bean.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**²
     * Provides a CSRF token repository using cookies for CSRF protection.
     *
     * @return A CookieCsrfTokenRepository for CSRF protection.
     */
    //injection csrfTokenRepository for csrf token
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        CookieCsrfTokenRepository repository = new CookieCsrfTokenRepository();
        repository.setCookieName("XSRF-TOKEN");
        repository.setHeaderName("X-XSRF-TOKEN");
        repository.setCookieCustomizer(cookie -> {
            cookie
                    .path("/")
                    .httpOnly(false)
                    .secure(false) // Change to true in production
                    .maxAge(Duration.ofMinutes(10));
        });
        return repository;
    }

    /**
     * Provides the AuthenticationManager bean.
     *
     * @param authenticationConfiguration The authentication configuration to retrieve the manager.
     * @return The AuthenticationManager bean.
     * @throws Exception If an error occurs during the creation of the manager.
     */
    // Inject Authentication Manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}