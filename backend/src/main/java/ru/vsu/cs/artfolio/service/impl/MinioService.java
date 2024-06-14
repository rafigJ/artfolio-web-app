package ru.vsu.cs.artfolio.service.impl;

import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.RemoveObjectsArgs;
import io.minio.Result;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.exception.BadRequestException;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.mapper.wrappers.MinioResult;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(
                RemoveObjectsArgs.builder()
                        .bucket(bucketName)
                        .objects(deleteObjects)
                        .build()
        );
        for (Result<DeleteError> result : results) {
            try {
                DeleteError error = result.get();
                System.out.println("Error in deleting object " + error.objectName() + "; " + error.message());
            } catch (Exception e) {
                throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
            }
        }
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
            if (originalImage == null) {
                throw new BadRequestException("File have bad signature (it's not jpg/png format)");
            }
            Thumbnails.of(originalImage)
                    .size(406, 204)
                    .crop(Positions.TOP_CENTER)
                    .outputFormat(file.getContentType().substring("image/".length()))
                    .toOutputStream(byteArrayOutputStream);
            return byteArrayOutputStream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
