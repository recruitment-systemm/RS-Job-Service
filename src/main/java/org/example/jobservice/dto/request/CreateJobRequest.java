package org.example.jobservice.dto.request;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateJobRequest(

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Address is required")
        @Size(max = 255, message = "Address must not exceed 255 characters")
        String address,

        @Digits(integer = 3, fraction = 6, message = "Latitude must have at most 3 integer digits and 6 decimal digits")
        @DecimalMin(value = "-90.0")
        @DecimalMax(value = "90.0")
        BigDecimal latitude,

        @Digits(integer = 3, fraction = 6, message = "Longitude must have at most 3 integer digits and 6 decimal digits")
        @DecimalMin(value = "-180.0")
        @DecimalMax(value = "180.0")
        BigDecimal longitude
) {}