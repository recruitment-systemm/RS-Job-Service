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
    public JobResponse createJob(UUID organizationId, UUID employeeId, CreateJobRequest request) {

        JobEntity job = JobEntity.builder()
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

        JobEntity savedJob = jobRepository.save(job);
        JobResponse response = toResponse(savedJob);
        return response;
    }

    private JobResponse toResponse(JobEntity job) {
        return JobResponse.builder()
                .id(job.getId())
                .organizationId(job.getOrganizationId())
                .createdBy(job.getCreatedBy())
                .title(job.getTitle())
                .description(job.getDescription())
                .address(job.getAddress())
                .latitude(job.getLatitude())
                .longitude(job.getLongitude())
                .status(job.getStatus())
                .createdAt(job.getCreatedAt())
                .build();
    }
}