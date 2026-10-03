package com.example.batchdemo.reader;

import com.example.batchdemo.domain.SourceRecord;
import com.example.batchdemo.mapper.SourceRecordFieldSetMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.core.io.FileSystemResource;

import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class S3CsvItemReader implements ItemStreamReader<SourceRecord> {

    private static final Logger log = LoggerFactory.getLogger(S3CsvItemReader.class);

    private final S3Client s3Client;
    private final String bucketName;
    private final String objectKey;

    private FlatFileItemReader<SourceRecord> delegate;
    private Path temporaryFile;

    public S3CsvItemReader(
            S3Client s3Client,
            String bucketName,
            String objectKey) {

        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.objectKey = objectKey;
    }

    @Override
    public void open(ExecutionContext executionContext) {

        try {
            temporaryFile = Files.createTempFile(
                    "spring-batch-s3-",
                    ".csv"
            );

            GetObjectRequest request =
                    GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(objectKey)
                            .build();

            ResponseInputStream<?> s3InputStream =
                    s3Client.getObject(request);

            copyToTemporaryFile(
                    s3InputStream,
                    temporaryFile.toFile()
            );

            delegate = new FlatFileItemReaderBuilder<SourceRecord>()
                    .name("s3CsvItemReader")
                    .resource(new FileSystemResource(
                            temporaryFile.toFile()
                    ))
                    .linesToSkip(1)
                    .delimited()
                    .delimiter(",")
                    .names("id", "name", "age")
                    .fieldSetMapper(
                            new SourceRecordFieldSetMapper()
                    )
                    .build();

            delegate.open(executionContext);

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Unable to read CSV file from S3",
                    exception
            );
        }
    }

    private void copyToTemporaryFile(
            InputStream inputStream,
            File targetFile) throws IOException {

        try (
                InputStream input = inputStream;
                FileOutputStream output =
                        new FileOutputStream(targetFile)
        ) {

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }
    }

    @Override
    public SourceRecord read() throws Exception {

        if (delegate == null) {
            throw new IllegalStateException(
                    "Reader delegate is null — open() was not called. " +
                    "Check that the @Bean method's declared return type " +
                    "implements ItemStream (e.g. ItemStreamReader) so " +
                    "Spring Batch registers it as a stream and invokes open()."
            );
        }

        return delegate.read();
    }

    @Override
    public void update(ExecutionContext executionContext) {

        if (delegate != null) {
            delegate.update(executionContext);
        }
    }

    @Override
    public void close() {

        if (delegate != null) {
            delegate.close();
        }

        if (temporaryFile != null) {
            try {
                Files.deleteIfExists(temporaryFile);
            } catch (IOException e) {
                log.warn("Failed to delete temporary file {}", temporaryFile, e);
            }
        }
    }
}