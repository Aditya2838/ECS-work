package com.example.batchdemo.repository;

import com.example.batchdemo.domain.TargetRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TargetRecordMongoRepository extends MongoRepository<TargetRecord, String> {
}