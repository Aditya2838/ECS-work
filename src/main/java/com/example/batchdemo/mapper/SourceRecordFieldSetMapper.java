package com.example.batchdemo.mapper;
 
import com.example.batchdemo.domain.SourceRecord;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
 
public class SourceRecordFieldSetMapper
        implements FieldSetMapper<SourceRecord> {
 
    @Override
    public SourceRecord mapFieldSet(FieldSet fieldSet) {
 
        SourceRecord record = new SourceRecord();
 
        record.setId(fieldSet.readInt("id"));
        record.setName(fieldSet.readString("name"));
        record.setAge(fieldSet.readInt("age"));
 
        return record;
    }
}