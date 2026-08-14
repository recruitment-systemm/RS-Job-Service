package org.example.jobservice.dto.response;

import lombok.Builder;
import org.example.jobservice.entity.JobStatus;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Builder
public record JobResponse(
        UUID id,
        UUID organizationId,
        UUID createdBy,
        String title,
        String description,
        String address,
        BigDecimal latitude,
        BigDecimal longitude,
        JobStatus status,
        OffsetDateTime createdAt
) { }