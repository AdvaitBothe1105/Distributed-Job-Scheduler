package com.jobscheduler.worker_service.service;

import com.jobscheduler.worker_service.dto.JobEvent;
import com.jobscheduler.worker_service.model.JobType;
import org.springframework.stereotype.Service;

@Service
public class JobExecutorService {

    public void executeEvent(JobEvent jobEvent) throws Exception{
        switch (JobType.valueOf(jobEvent.type())){

            case EMAIL_BLAST -> {
                System.out.println("Executing EMAIL_BLAST for: "
                        + jobEvent.ownerId());
                System.out.println("Payload: " + jobEvent.payload());

                Thread.sleep(1000);

                System.out.println("EMAIL_BLAST completed: " + jobEvent.jobId());
            }

            case REPORT_GEN -> {
                System.out.println("Generating report for: "
                        + jobEvent.ownerId());
                Thread.sleep(2000); // reports take longer
                System.out.println("REPORT_GEN completed: " + jobEvent.jobId());
            }

            case DATA_SYNC -> {
                System.out.println("Syncing data for: " + jobEvent.ownerId());
                Thread.sleep(1500);
                System.out.println("DATA_SYNC completed: " + jobEvent.jobId());
            }

            default -> throw new RuntimeException(
                    "Unknown job type: " + jobEvent.type()
            );

        }
    }
//    public void executeEvent(JobEvent event) {
//        throw new RuntimeException("Testing retry");
//    }
}
