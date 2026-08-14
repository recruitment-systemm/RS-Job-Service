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
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        System.out.println("🔥 JWT FILTER RUNNING");
        System.out.println("Path: " + request.getServletPath());

        String token = extractToken(request);

        if (token == null) {
            System.out.println("❌ No access token found");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("✅ Access token found");
        System.out.println("Token length: " + token.length());
        System.out.println("Secret length: " + jwtSecret.length());

        try {

            SecretKey key = Keys.hmacShaKeyFor(
                    jwtSecret.getBytes(StandardCharsets.UTF_8)
            );

            System.out.println("✅ SecretKey created");

            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            System.out.println("✅ JWT parsed successfully");
            System.out.println("Claims: " + claims);

            String subject = claims.getSubject();
            String organizationIdClaim =
                    claims.get("organizationId", String.class);
            String role =
                    claims.get("role", String.class);
            String sessionId =
                    claims.get("sessionId", String.class);
            String tokenType =
                    claims.get("type", String.class);

            if (subject == null ||
                    organizationIdClaim == null ||
                    role == null ||
                    sessionId == null ||
                    tokenType == null) {

                System.out.println("❌ Missing required JWT claims");

                sendUnauthorized(
                        response,
                        "Invalid token claims"
                );
                return;
            }

            if (!"access".equalsIgnoreCase(tokenType)) {

                System.out.println("❌ Token is not an access token");

                sendUnauthorized(
                        response,
                        "Invalid access token"
                );
                return;
            }

            UUID employeeId = UUID.fromString(subject);
            UUID organizationId = UUID.fromString(organizationIdClaim);

            System.out.println("Subject: " + subject);
            System.out.println("Employee ID: " + employeeId);
            System.out.println("Organization ID: " + organizationId);
            System.out.println("Role: " + role);
            System.out.println("Session ID: " + sessionId);
            System.out.println("Token Type: " + tokenType);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            employeeId,
                            null,
                            Collections.singletonList(
                                    new SimpleGrantedAuthority(
                                            "ROLE_" + role.toUpperCase()
                                    )
                            )
                    );

            /*
             * We use authentication.details to store organizationId.
             */
            authentication.setDetails(organizationId);

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            System.out.println("✅ Authentication created successfully");
            System.out.println("Employee: " + employeeId);
            System.out.println("Organization: " + organizationId);
            System.out.println("Role: " + role);

            filterChain.doFilter(request, response);

        } catch (Exception e) {

            System.out.println("❌ JWT ERROR");
            System.out.println("Exception: " + e.getClass().getName());
            System.out.println("Message: " + e.getMessage());

            SecurityContextHolder.clearContext();

            sendUnauthorized(
                    response,
                    "Invalid or expired access token"
            );
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