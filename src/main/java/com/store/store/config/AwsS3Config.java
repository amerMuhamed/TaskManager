package com.store.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {

    @Bean
    public S3Client s3Client() {
        String awsAccessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String awsSecretKey = System.getenv("AWS_SECRET_ACCESS_KEY");
        if (awsAccessKey == null || awsAccessKey.isEmpty() || awsSecretKey == null || awsSecretKey.isEmpty()) {
            throw new IllegalArgumentException("AWS Access Key ID and Secret Access Key cannot be empty.");
        }

        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(awsAccessKey, awsSecretKey);

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .region(Region.of("eu-north-1")) // تأكد من أن المنطقة صحيحة لمتطلباتك
                .build();
    }
}
