package com.example.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/v1/jobs")
public class JobRestController {

    private static final Logger log = LoggerFactory.getLogger(JobRestController.class);
    
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importProductJob; 

    @PostMapping("/import-products")
    public ResponseEntity<String> runEtlJob() {
        try {
          
            JobParameters params = new JobParametersBuilder()
                    .addLong("start_time", System.currentTimeMillis())
                    .toJobParameters();

            jobLauncher.run(importProductJob, params);

            log.info("test api job run ({})", params);

            return ResponseEntity.accepted().body("ETL Job started successfully");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Failed to start Job: " + e.getMessage());
        }
    }
}
