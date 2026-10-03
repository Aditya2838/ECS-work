package com.example.batchdemo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

import java.time.Duration;
import java.time.LocalDateTime;

public class JobLoggingListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobLoggingListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("Job [{}] starting. Start Time = {}",
                jobExecution.getJobInstance().getJobName(), LocalDateTime.now());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        LocalDateTime start = jobExecution.getStartTime();
        LocalDateTime end = jobExecution.getEndTime() != null
                ? jobExecution.getEndTime()
                : LocalDateTime.now();

        Duration duration = start != null ? Duration.between(start, end) : Duration.ZERO;

        log.info("Job [{}] finished. Start Time = {}, End Time = {}, Duration = {}ms, Final Status = {}",
                jobExecution.getJobInstance().getJobName(), start, end,
                duration.toMillis(), jobExecution.getStatus());
    }
}