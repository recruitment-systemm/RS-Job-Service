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
    public JobResponse createJob(
            @Valid @RequestBody CreateJobRequest request,
            Authentication authentication
    ) {

        UUID employeeId =
                (UUID) authentication.getPrincipal();

        UUID organizationId =
                (UUID) authentication.getDetails();

        System.out.println("========== CREATE JOB REQUEST ==========");
        System.out.println("Employee ID: " + employeeId);
        System.out.println("Organization ID: " + organizationId);
        System.out.println(
                "Authorities: " +
                        authentication.getAuthorities()
        );

        JobResponse response =
                jobService.createJob(
                        organizationId,
                        employeeId,
                        request
                );

        System.out.println("========== JOB RESPONSE ==========");
        System.out.println("ID: " + response.id());
        System.out.println(
                "Organization ID: " +
                        response.organizationId()
        );
        System.out.println(
                "Created By: " +
                        response.createdBy()
        );
        System.out.println("Title: " + response.title());
        System.out.println(
                "Description: " +
                        response.description()
        );
        System.out.println(
                "Address: " +
                        response.address()
        );
        System.out.println(
                "Latitude: " +
                        response.latitude()
        );
        System.out.println(
                "Longitude: " +
                        response.longitude()
        );
        System.out.println(
                "Status: " +
                        response.status()
        );
        System.out.println(
                "Created At: " +
                        response.createdAt()
        );
        System.out.println("================================");

        return response;
    }
}