package com.example.jobportal.job.controller;

import com.example.jobportal.auth.service.JobPortalUserPrincipal;
import com.example.jobportal.job.dto.JobPostingDto;
import com.example.jobportal.job.entity.JobPosting;
import com.example.jobportal.job.service.JobPostingService;
import com.example.jobportal.user.dto.ResponseMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/jobs")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @PostMapping("/create")
    public ResponseEntity<ResponseMessage> createJobPosting(
            @RequestBody JobPostingDto jobPostingDto,
            @AuthenticationPrincipal JobPortalUserPrincipal principal) {

        if (principal == null) throw new AccessDeniedException("Authentication required to create job profile.");

        JobPosting jobPosting = jobPostingService.createJobPosting(jobPostingDto, principal);

        ResponseMessage responseMessage = ResponseMessage.builder()
                .message("Job post created")
                .build();
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PutMapping("update/{jobId}")
    public ResponseEntity<ResponseMessage> updateJobPosting(
            @PathVariable String jobId,
            @RequestBody JobPostingDto jobPostingUpdates,
            @AuthenticationPrincipal JobPortalUserPrincipal principal) {

        if (principal == null) throw new AccessDeniedException("Authentication required to create job profile.");

        JobPosting updatedJob = jobPostingService.updateJobPosting(jobId, jobPostingUpdates,  principal);

        ResponseMessage responseMessage = ResponseMessage.builder()
                .message("Job post updated successfully")
                .build();
        return ResponseEntity.ok(responseMessage);
    }

    @GetMapping("/getAllActive")
    public ResponseEntity<List<JobPosting>> getAllActiveJobs(@AuthenticationPrincipal JobPortalUserPrincipal principal) {
        List<JobPosting> jobs = jobPostingService.findAllJobsByUser(principal);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<JobPosting>> getAllJobs(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "100", required = false) int size) {
        List<JobPosting> paginatedJobs = jobPostingService.getRecentJobs(page, size);
        return ResponseEntity.ok(paginatedJobs);
    }

    @GetMapping("/getById/{jobId}")
    public ResponseEntity<JobPosting> getJobById(@PathVariable String jobId) {
        JobPosting job = jobPostingService.findJobPostById(jobId);
        return ResponseEntity.ok(job);
    }
}
