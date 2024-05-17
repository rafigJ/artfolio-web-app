package ru.vsu.cs.artfolio.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.mapper.wrappers.MinioResult;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MinioService {

    @Value("${application.minio.bucketName}")
    private String bucketName;

    private final MinioClient minioClient;

    public MinioResult uploadPreviewFile(MultipartFile file) {
        ByteArrayOutputStream imageFile = resizeCompressImage(file);
        try (InputStream is = new ByteArrayInputStream(imageFile.toByteArray())) {
            imageFile.close();
            String name = UUID.randomUUID().toString();

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name)
                    .stream(is, is.available(), -1)
                    .contentType(file.getContentType())
                    .build());

            return new MinioResult(name, file.getContentType());
        } catch (Exception e) {
            throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    public MinioResult uploadFile(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
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

    private static ByteArrayOutputStream resizeCompressImage(MultipartFile file) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            InputStream in = new ByteArrayInputStream(file.getBytes());
            BufferedImage originalImage = ImageIO.read(in);
            Thumbnails.of(originalImage)
                    .size(406, 204)
                    .outputFormat("jpg")
                    .toOutputStream(byteArrayOutputStream);
            return byteArrayOutputStream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
