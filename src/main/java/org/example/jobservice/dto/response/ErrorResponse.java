package org.example.jobservice.dto.response;

public record ErrorResponse(boolean success, String message, ErrorDetails error) { }