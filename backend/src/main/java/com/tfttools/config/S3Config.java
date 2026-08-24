package com.tfttools.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

/**
 * Credentials are wired explicitly from Spring-bound properties rather than the AWS SDK's default
 * credential chain: {@code spring.config.import} loads {@code .env} into the Spring {@link org.springframework.core.env.Environment},
 * not into real OS environment variables, so {@code DefaultCredentialsProvider} would not see them.
 */
@Configuration
public class S3Config
{
    @Value("${aws.s3.region}")
    private String region;

    @Value("${aws.s3.access-key-id}")
    private String accessKeyId;

    @Value("${aws.s3.secret-access-key}")
    private String secretAccessKey;

    @Bean
    public S3Client s3Client()
    {
        // Fall back to placeholder credentials rather than failing bean creation when unset, so the app
        // still boots in environments without AWS configured (icon caching then simply fails at call time,
        // caught and logged per-champion by ChampionIconCacheService).
        String resolvedAccessKeyId = blankToPlaceholder(accessKeyId);
        String resolvedSecretAccessKey = blankToPlaceholder(secretAccessKey);

        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(resolvedAccessKeyId, resolvedSecretAccessKey)))
                .build();
    }

    private String blankToPlaceholder(String value)
    {
        return (value == null || value.isBlank()) ? "unset" : value;
    }
}
