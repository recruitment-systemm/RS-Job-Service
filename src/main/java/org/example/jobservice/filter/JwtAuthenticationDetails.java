package org.example.jobservice.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class JwtAuthenticationDetails {

    private UUID organizationId;
    private String role;
    private String sessionId;
}