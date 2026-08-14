package org.example.jobservice.service;

import lombok.RequiredArgsConstructor;
import org.example.jobservice.dto.request.CreateJobRequest;
import org.example.jobservice.dto.response.JobResponse;
import org.example.jobservice.entity.JobEntity;
import org.example.jobservice.entity.JobStatus;
import org.example.jobservice.repository.JobRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    @Transactional
    public JobResponse createJob(
            UUID organizationId,
            UUID employeeId,
            CreateJobRequest request
    ) {

        System.out.println("========== JOB SERVICE ==========");
        System.out.println(
                "Organization ID: " +
                        organizationId
        );
        System.out.println(
                "Employee ID: " +
                        employeeId
        );

        System.out.println(
                "Title: " +
                        request.title()
        );

        System.out.println(
                "Description: " +
                        request.description()
        );

        System.out.println(
                "Address: " +
                        request.address()
        );

        System.out.println(
                "Latitude: " +
                        request.latitude()
        );

        System.out.println(
                "Longitude: " +
                        request.longitude()
        );

        JobEntity job =
                JobEntity.builder()
                        .id(UUID.randomUUID())
                        .organizationId(organizationId)
                        .createdBy(employeeId)
                        .title(request.title())
                        .description(request.description())
                        .address(request.address())
                        .latitude(request.latitude())
                        .longitude(request.longitude())
                        .status(JobStatus.DRAFT)
                        .createdAt(OffsetDateTime.now())
                        .build();

        JobEntity savedJob =
                jobRepository.save(job);

        JobResponse response =
                toResponse(savedJob);

        System.out.println(
                "Job successfully saved: " +
                        response.id()
        );

        return response;
    }

    private JobResponse toResponse(
            JobEntity job
    ) {

        return JobResponse.builder()
                .id(job.getId())
                .organizationId(
                        job.getOrganizationId()
                )
                .createdBy(
                        job.getCreatedBy()
                )
                .title(
                        job.getTitle()
                )
                .description(
                        job.getDescription()
                )
                .address(
                        job.getAddress()
                )
                .latitude(
                        job.getLatitude()
                )
                .longitude(
                        job.getLongitude()
                )
                .status(
                        job.getStatus()
                )
                .createdAt(
                        job.getCreatedAt()
                )
                .build();
    }
}