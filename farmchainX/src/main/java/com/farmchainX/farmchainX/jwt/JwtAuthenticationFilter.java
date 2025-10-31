package com.farmchainX.farmchainX.jwt;

import com.farmchainX.farmchainX.Security.JwtUtil;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ Step 1 — Skip public routes completely
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Step 2 — Normal JWT validation for protected routes
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7);

        try {
            String email = jwtUtil.extractUsername(token);
            String role = jwtUtil.extractRole(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority(role.trim().toUpperCase()));

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        } catch (JwtException ex) {
            logger.warn("Invalid JWT: " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // ✅ Helper method: define which URLs are public
    private boolean isPublicPath(String path) {
        return path.startsWith("/api/auth")
                || path.startsWith("/api/verify")
                || path.startsWith("/uploads")
                || path.contains("/qrcode/download")
                || (path.startsWith("/api/products") && "GET".equalsIgnoreCase(path));
    }
}