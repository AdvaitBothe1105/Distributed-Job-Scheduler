package com.jobscheduler.job_service.controller;

import com.jobscheduler.job_service.dto.JobRequest;
import com.jobscheduler.job_service.dto.JobResponse;
import com.jobscheduler.job_service.model.Job;
import com.jobscheduler.job_service.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class JobController {

    @Autowired
    private JobService jobService;

    @PostMapping("/jobs")
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse submitJob(
            @RequestBody JobRequest request,
            @RequestHeader("Authorization") String token){
                return jobService.submitJob(request,token);
    }

    @GetMapping("/jobs")
    public List<Job> getMyJobs(
            @RequestHeader("Authorization") String token
    ){
        return jobService.getMyJobs(token);
    }

    @GetMapping("/jobs/{id}")
    public Job getJobById(
            @PathVariable String id,
            @RequestHeader("Authorization") String token
    ){
        return jobService.getJobById(id, token);
    }

}
