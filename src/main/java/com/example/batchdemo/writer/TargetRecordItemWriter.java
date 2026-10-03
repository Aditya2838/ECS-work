/*
 * package com.example.batchdemo.writer;
 * 
 * import com.example.batchdemo.domain.TargetRecord;
 * 
 * import javax.sql.DataSource;
 * 
 * import org.springframework.batch.item.database.JdbcBatchItemWriter; import
 * org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
 * import
 * org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
 * 
 * public final class TargetRecordItemWriter {
 * 
 * private TargetRecordItemWriter() { }
 * 
 * public static JdbcBatchItemWriter<TargetRecord> build( DataSource dataSource)
 * {
 * 
 * return new JdbcBatchItemWriterBuilder<TargetRecord>() .dataSource(dataSource)
 * .sql( "INSERT INTO target_record " +
 * "(source_id, name, age, processed_value, " + "status, processed_at) " +
 * "VALUES " + "(:sourceId, :name, :age, :processedValue, " +
 * ":status, :processedAt)" ) .itemSqlParameterSourceProvider( item -> new
 * BeanPropertySqlParameterSource(item) ) .assertUpdates(true) .build(); } }
 */