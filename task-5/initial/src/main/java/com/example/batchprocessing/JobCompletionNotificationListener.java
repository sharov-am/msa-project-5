package com.example.batchprocessing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;


@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

	private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	private final JdbcTemplate jdbcTemplate;

	//private final Counter jobCounter;
	private final  MeterRegistry registry;

	public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate,MeterRegistry registry) {
		this.jdbcTemplate = jdbcTemplate;
		this.registry = registry;
		/*
		this.jobCounter = Counter.builder("batch_job_executions_total")
                .description("Общее количество запусков Job")
                .tag("job_name", "importProductJob")
				.tag("status", status)
                .register(registry);
		*/		


	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("!!! JOB FINISHED! Time to verify the results");

			jdbcTemplate
					.query("SELECT productId, productSku, productName, productAmount, productData FROM products", new DataClassRowMapper<>(Product.class))
					.forEach(person -> log.info("Transformed <{}> in the database.", person));
		}
		String status = jobExecution.getStatus().toString();
		

    	Counter.builder("batch_job_result_total")
           .description("Статистика запусков")
           .tag("status", status)
           .register(registry) 
           .increment();

	}
}
