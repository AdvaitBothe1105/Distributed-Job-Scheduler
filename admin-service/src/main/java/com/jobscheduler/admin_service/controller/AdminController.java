package com.jobscheduler.admin_service.controller;

import com.jobscheduler.admin_service.dto.StatsResponse;
import com.jobscheduler.admin_service.model.Job;
import com.jobscheduler.admin_service.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/jobs")
    public List<Job> getAllJobs(){
        return adminService.getAllJobs();
    }

    @GetMapping("/jobs/status/{status}")
    public List<Job> getJobByStatus(@PathVariable String status){
        return adminService.getJobByStatus(status);
    }

    @GetMapping("/jobs/dlq")
    public List<Job> getJobByDLQ() {
        return adminService.getDLQJobs();
    }

    @PostMapping("/jobs/{id}/retry")
    public Job retryDLQJob(@PathVariable String id){
        return adminService.retryDLQJob(id);
    }

    @GetMapping("/stats")
    public StatsResponse getStats(){
        return adminService.getStats();
    }

}
