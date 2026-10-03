package com.example.batchdemo.processor;
 
import com.example.batchdemo.domain.SourceRecord;
import com.example.batchdemo.domain.TargetRecord;
 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
 
public class RecordUpperCaseProcessor
        implements ItemProcessor<SourceRecord, TargetRecord> {
 
    private static final Logger LOG =
            LoggerFactory.getLogger(
                    RecordUpperCaseProcessor.class
            );
 
    @Override
    public TargetRecord process(SourceRecord item) {
 
        String upperCaseName =
                item.getName() == null
                        ? null
                        : item.getName().toUpperCase();
 
        LOG.info(
                "Processing id={}, name={}, uppercaseName={}",
                item.getId(),
                item.getName(),
                upperCaseName
        );
 
        return new TargetRecord(
                item.getId(),
                item.getName(),
                item.getAge(),
                upperCaseName,
                "PROCESSED"
        );
    }
}