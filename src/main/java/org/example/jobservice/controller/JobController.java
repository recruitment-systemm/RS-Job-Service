package org.example.jobservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.request.CreateJobRequest;
import org.example.jobservice.dto.response.JobResponse;
import org.example.jobservice.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse createJob(@Valid @RequestBody CreateJobRequest request, Authentication authentication) {
        UUID employeeId = (UUID) authentication.getPrincipal();
        UUID organizationId = (UUID) authentication.getDetails();
        JobResponse response = jobService.createJob(organizationId, employeeId, request);
        return response;
    }
}