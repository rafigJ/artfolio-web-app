package ru.vsu.cs.artfolio.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.mapper.wrappers.MinioResult;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {

    @Value("${application.minio.bucketName}")
    private String bucketName;

    private final MinioClient minioClient;

    public MinioResult uploadFile(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            String name = UUID.randomUUID().toString();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(file.getContentType())
                    .build());
            return new MinioResult(name, file.getContentType());
        } catch (Exception e) {
            throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    public InputStream downloadFile(String fileName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .build()
            );
        } catch (Exception e) {
            throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    public void deleteFiles(List<String> fileNames) {
        var deleteObjects = fileNames.stream().map(DeleteObject::new).toList();
        minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(bucketName)
                        .objects(deleteObjects)
                        .build()
        );
    }

    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build()
            );
        } catch (Exception e) {
            throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
