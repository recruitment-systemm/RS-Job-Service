package org.example.jobservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.request.CreateJobRequest;
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
    public JobResponse createJob(@Valid @RequestBody CreateJobRequest request, Authentication authentication) {
        UUID employeeId = (UUID) authentication.getPrincipal();
        UUID organizationId = UUID.fromString(authentication.getDetails().toString());
        return jobService.createJob(organizationId, employeeId, request);
    }

    @GetMapping
    public List<JobResponse> getOpenJobs() {
        return jobService.getOpenJobs();
    }

    @GetMapping("/organization")
    @PreAuthorize("hasRole('HR')")
    public List<JobResponse> getOrganizationJobs(Authentication authentication) {
        UUID organizationId = UUID.fromString(authentication.getDetails().toString());
        return jobService.getOrganizationJobs(organizationId);
    }

    @PatchMapping("/{jobId}/status")
    @PreAuthorize("hasRole('HR')")
    public JobResponse updateStatus(@PathVariable UUID jobId, @RequestParam JobStatus status, Authentication authentication) {
        UUID organizationId = UUID.fromString(authentication.getDetails().toString());
        return jobService.updateStatus(organizationId, jobId, status);
    }
}