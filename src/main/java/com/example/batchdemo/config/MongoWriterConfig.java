package com.example.batchdemo.config;

import com.example.batchdemo.domain.TargetRecord;
import com.example.batchdemo.repository.TargetRecordMongoRepository;

import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongoWriterConfig {

    @Bean
    public RepositoryItemWriter<TargetRecord> mongoWriter(
            TargetRecordMongoRepository repository) {

        return new RepositoryItemWriterBuilder<TargetRecord>()
                .repository(repository)
                .methodName("save")
                .build();
    }
}