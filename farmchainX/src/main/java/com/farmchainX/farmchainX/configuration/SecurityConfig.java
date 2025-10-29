package com.farmchainX.farmchainX.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.farmchainX.farmchainX.jwt.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity(prePostEnabled = true) // enables @PreAuthorize and @RolesAllowed
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF since we’re using stateless JWTs
            .csrf(csrf -> csrf.disable())

            // Authorize requests by path and role
            .authorizeHttpRequests(auth -> auth

                // 🔓 Public endpoints (no login required)
                .requestMatchers("/api/auth/**").permitAll()        // register/login
            

                // 🧑‍🌾 Product APIs — only Farmers can create/edit
                .requestMatchers("/api/products/**")
                    .hasAnyRole("FARMER")

                // 🔗 Supply Chain Tracking APIs
                // Only Distributor/Retailer/Admin can update
                .requestMatchers("/api/track/update")
                    .hasAnyRole("DISTRIBUTOR","RETAILER","ADMIN")

                // All users (even without login) can view product journeys
                .requestMatchers("/api/track/**").permitAll()

                // 🧑‍💼 Admin routes (overview, management)
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Anything else must be authenticated
                .anyRequest().authenticated()
            )

            // Stateless session: each request must include JWT
            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Add our JWT filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}