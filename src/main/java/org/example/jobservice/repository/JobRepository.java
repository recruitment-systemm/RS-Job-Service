package org.example.jobservice.repository;

import org.example.jobservice.entity.JobEntity;
import org.example.jobservice.entity.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JobRepository extends JpaRepository<JobEntity, UUID> {
    List<JobEntity> findByOrganizationId(UUID organizationId);
    List<JobEntity> findByStatus(JobStatus status);
    Optional<JobEntity> findByIdAndOrganizationId(UUID id, UUID organizationId);
}