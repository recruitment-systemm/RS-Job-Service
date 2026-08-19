package org.example.jobservice.exception;

public record ValidationError(String field, String message) { }
