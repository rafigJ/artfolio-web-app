package ru.vsu.cs.artfolio.config;

import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Configuration
public class MinioConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MinioConfig.class);

    @Value("${application.minio.url}")
    private String url;

    @Value("${application.minio.accessKey}")
    private String accessKey;

    @Value("${application.minio.secretKey}")
    private String secretKey;

    @Bean
    public MinioClient minioClient() {
        try {
            String healthPattern = "minio/health/live";
            String healthUrl = url.endsWith("/") ?
                    url + healthPattern :
                    url + "/" + healthPattern;
            URL minioHealth = new URL(healthUrl);
            HttpURLConnection con = (HttpURLConnection) minioHealth.openConnection();
            con.connect();
            if (con.getResponseCode() == 200) {
                return MinioClient.builder()
                        .endpoint(url)
                        .credentials(accessKey, secretKey)
                        .build();
            } else {
                throw new IOException("Response from Minio " + con.getResponseCode() + ": " + con.getResponseMessage());
            }
        } catch (IOException e) {
            LOG.warn("Application can't connect to Minio S3; " + e.getMessage());
            throw new RuntimeException("Application can't connect to Minio S3 " + e.getMessage());
        }
    }
}
