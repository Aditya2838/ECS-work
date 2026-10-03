package com.example.batchdemo.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


public class ValidationTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(ValidationTasklet.class);

    private final JdbcTemplate jdbcTemplate;

    public ValidationTasklet(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        Integer tableCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM information_schema.tables "
                        + "WHERE table_schema = DATABASE() AND table_name = 'source_records'",
                Integer.class);

        if (tableCount == null || tableCount == 0) {
            throw new IllegalStateException(
                    "Validation failed: table 'source_records' does not exist in the current database.");
        }

        Long totalRecords = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM source_records", Long.class);
        Long newRecords = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM source_records WHERE status = 'NEW'", Long.class);

        log.info("Validation passed: source_records exists. total={}, newToProcess={}",
                totalRecords, newRecords);

        return RepeatStatus.FINISHED;
    }
}
