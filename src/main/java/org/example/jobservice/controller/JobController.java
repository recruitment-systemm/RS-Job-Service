package org.example.jobservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.request.CreateJobRequest;
import org.example.jobservice.dto.response.ApiResponse;
import org.example.jobservice.dto.response.JobResponse;
import org.example.jobservice.entity.JobStatus;
import org.example.jobservice.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('HR')")
    public ApiResponse<JobResponse> createJob(@Valid @RequestBody CreateJobRequest request, Authentication authentication) {
        UUID employeeId = (UUID) authentication.getPrincipal();
        UUID organizationId = UUID.fromString(authentication.getDetails().toString());
        JobResponse response = jobService.createJob(organizationId, employeeId, request);
        return ApiResponse.success(HttpStatus.CREATED.value(), "Job created successfully", response);
    }

    @GetMapping
    public ApiResponse<List<JobResponse>> getOpenJobs() {
        return ApiResponse.success(HttpStatus.OK.value(), "Open jobs retrieved successfully", jobService.getOpenJobs());
    }

    @GetMapping("/organization")
    @PreAuthorize("hasRole('HR')")
    public ApiResponse<List<JobResponse>> getOrganizationJobs(Authentication authentication) {
        UUID organizationId = UUID.fromString(authentication.getDetails().toString());
        return ApiResponse.success(HttpStatus.OK.value(), "Organization jobs retrieved successfully", jobService.getOrganizationJobs(organizationId));
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<JobResponse>> getAllJobs() {
        return ApiResponse.success(HttpStatus.OK.value(), "All jobs retrieved successfully", jobService.getAllJobs());
    }

    @PatchMapping("/{jobId}/status")
    @PreAuthorize("hasRole('HR')")
    public ApiResponse<JobResponse> updateStatus(@PathVariable UUID jobId, @RequestParam JobStatus status, Authentication authentication) {
        UUID organizationId = UUID.fromString(authentication.getDetails().toString());
        JobResponse response = jobService.updateStatus(organizationId, jobId, status);
        return ApiResponse.success(HttpStatus.OK.value(), "Job status updated successfully", response);
    }
}