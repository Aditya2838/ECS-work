package com.example.batchdemo.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.Collection;


public class ReportTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(ReportTasklet.class);

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Collection<StepExecution> stepExecutions =
                chunkContext.getStepContext().getStepExecution().getJobExecution().getStepExecutions();

        long totalRead = 0;
        long totalWritten = 0;
        for (StepExecution se : stepExecutions) {
            if ("processingStep".equals(se.getStepName())) {
                totalRead += se.getReadCount();
                totalWritten += se.getWriteCount();
            }
        }

        String jobStatus = chunkContext.getStepContext().getStepExecution()
                .getJobExecution().getStatus().toString();

        log.info("===== JOB SUMMARY =====");
        log.info("Total Read     : {}", totalRead);
        log.info("Total Written  : {}", totalWritten);
        log.info("Job Status     : {}", jobStatus);
        log.info("========================");

        return RepeatStatus.FINISHED;
    }
}
