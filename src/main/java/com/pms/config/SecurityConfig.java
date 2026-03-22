package com.pms.config;

import com.pms.security.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // 🔒 Disable CSRF (for APIs)
            .csrf(csrf -> csrf.disable())

            // 🌐 Enable CORS (uses CorsConfig.java)
            .cors(cors -> {})

            // 🔐 Authorization rules
            .authorizeHttpRequests(auth -> auth
                // ✅ Public APIs
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()

                // 🔒 Protected APIs
                .anyRequest().authenticated()
            )

            // ⚙️ Stateless session (JWT)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        // 🔑 Add JWT filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // 🧪 H2 Console fix
        http.headers(headers -> headers.frameOptions(frame -> frame.disable()));

        return http.build();
    }
}