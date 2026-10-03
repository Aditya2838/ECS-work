package com.example.batchdemo.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;


public class StepLoggingListener implements StepExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(StepLoggingListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Step [{}] starting", stepExecution.getStepName());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Step [{}] finished. Read Count = {}, Write Count = {}, Commit Count = {}, Skip Count = {}",
                stepExecution.getStepName(),
                stepExecution.getReadCount(),
                stepExecution.getWriteCount(),
                stepExecution.getCommitCount(),
                stepExecution.getSkipCount());
        return stepExecution.getExitStatus();
    }
}
