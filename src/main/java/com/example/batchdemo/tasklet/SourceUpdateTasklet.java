package com.example.batchdemo.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


public class SourceUpdateTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(SourceUpdateTasklet.class);

    private final JdbcTemplate jdbcTemplate;

    public SourceUpdateTasklet(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        int updated = jdbcTemplate.update(
                "UPDATE source_records "
                        + "SET status = 'PROCESSED' "
                        + "WHERE status = 'NEW' "
                        + "AND id IN (SELECT source_id FROM target_records)");

        log.info("SourceUpdateTasklet: marked {} source_records row(s) as PROCESSED", updated);
        return RepeatStatus.FINISHED;
    }
}
