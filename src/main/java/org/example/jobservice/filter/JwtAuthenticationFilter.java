package org.example.jobservice.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = extractToken(request);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String subject = claims.getSubject();
            String organizationIdClaim = claims.get("organizationId", String.class);
            String role = claims.get("role", String.class);
            String sessionId = claims.get("sessionId", String.class);
            String tokenType = claims.get("type", String.class);

            if (subject == null || organizationIdClaim == null || role == null || sessionId == null || tokenType == null) {
                sendUnauthorized(response, "Invalid token claims");
                return;
            }

            if (!"access".equalsIgnoreCase(tokenType)) {
                sendUnauthorized(response, "Invalid access token");
                return;
            }

            UUID employeeId = UUID.fromString(subject);
            UUID organizationId = UUID.fromString(organizationIdClaim);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(employeeId, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
            authentication.setDetails(organizationId);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            sendUnauthorized(response, "Invalid or expired access token");
        }
    }

    private String extractToken(HttpServletRequest request) {

        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {

            if ("Access".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private void sendUnauthorized(
            HttpServletResponse response,
            String message
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                """
                {
                    "success": false,
                    "message": "%s"
                }
                """.formatted(message)
        );
    }
}