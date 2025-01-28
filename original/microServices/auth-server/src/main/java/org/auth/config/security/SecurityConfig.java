package org.auth.config.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import org.auth.model.enums.UserDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@OpenAPIDefinition(info = @Info(title = "Auth API", version = "1.0", description = "API for Authentication"))
public class SecurityConfig {

    private static final String[] WHITE_LIST = {
        "/api/user/register",
        "/api/user/login",
        "/swagger-ui.html",
        "/swagger-ui/**",
        "/v3/api-docs/**",
        "/v3/api-docs.yaml",
        "/swagger-resources/**",
        "/swagger-config/**",
        "/webjars/**",
        "/actuator/prometheus"
    };
    private final JwtUtil jwtUtil;
    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    
    @Bean
    @Operation(summary = "Configure security filter chain")
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity)
        throws Exception {
        return httpSecurity
            .csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .formLogin(AbstractHttpConfigurer::disable)
            .addFilterBefore(new JwtAuthorizationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(request -> request
                .requestMatchers(WHITE_LIST).permitAll()
                .anyRequest().hasAnyRole(UserDetails.COMPLETED.name(),
                    UserDetails.NOT_COMPLETED.name()))
            .build();
    }
    
    @Bean
    @Operation(summary = "Configure password encoder")
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}