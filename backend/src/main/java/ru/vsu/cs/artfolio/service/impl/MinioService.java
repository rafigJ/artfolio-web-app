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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.exception.BadRequestException;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.mapper.wrappers.MinioRequest;
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

    private static final Logger LOG = LoggerFactory.getLogger(MinioService.class);

    @Value("${application.minio.bucketName}")
    private String bucketName;

    private final MinioClient minioClient;


    public MinioResult uploadPreviewFile(MultipartFile file) {
        ByteArrayOutputStream imageFile = resizeCompressPreviewImage(file);
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
            LOG.warn("Upload preview file {} throw exception: {}", file.getName(), e.getMessage());
            throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    public MinioResult uploadAvatarFile(MinioRequest file) {
        ByteArrayOutputStream imageFile = resizeCompressAvatarFile(file);
        try (InputStream is = new ByteArrayInputStream(imageFile.toByteArray())) {
            String name = UUID.randomUUID().toString();

            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name)
                    .stream(is, is.available(), -1)
                    .contentType(file.contentType())
                    .build());

            return new MinioResult(name, file.contentType());
        } catch (Exception e) {
            LOG.warn("Upload avatar throw exception: {}", e.getMessage());
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
            LOG.warn("Upload file {} throw exception: {}", file.getName(), e.getMessage());
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
            LOG.warn("Download file {} throw exception: {}", fileName, e.getMessage());
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
                LOG.warn("Error in deleting object " + error.objectName() + "; " + error.message());
            } catch (Exception e) {
                LOG.warn("Delete files {} throw exception: {}", fileNames, e.getMessage());
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
            LOG.warn("Delete file {} throw exception: {}", fileName, e.getMessage());
            throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    private static ByteArrayOutputStream resizeCompressPreviewImage(MultipartFile file) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            InputStream in = new ByteArrayInputStream(file.getBytes());
            BufferedImage originalImage = ImageIO.read(in);
            if (originalImage == null) {
                LOG.warn("File have bad signature (it's not jpg/png format)");
                throw new BadRequestException("File have bad signature (it's not jpg/png format)");
            }
            if (file.getContentType() == null) {
                LOG.warn("File {} must have content-type field", file);
                throw new BadRequestException("File must have content-type field");
            }
            Thumbnails.of(originalImage)
                    .size(406, 204)
                    .crop(Positions.CENTER)
                    .outputFormat(file.getContentType().substring("image/".length()))
                    .toOutputStream(byteArrayOutputStream);
            return byteArrayOutputStream;
        } catch (IOException e) {
            LOG.warn("Resize compress image {} throw exception: {}", file.getName(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static ByteArrayOutputStream resizeCompressAvatarFile(MinioRequest file) {
        try (InputStream fileIn = file.inputStream()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = new ByteArrayInputStream(fileIn.readAllBytes());
            BufferedImage originalImage = ImageIO.read(in);

            if (originalImage == null) {
                LOG.warn("File have bad signature (it's not jpg/png format)");
                throw new BadRequestException("File have bad signature (it's not jpg/png format)");
            }
            if (file.contentType() == null) {
                LOG.warn("File {} must have content-type field", file);
                throw new BadRequestException("File must have content-type field");
            }
            Thumbnails.of(originalImage)
                    .size(150, 150)
                    .crop(Positions.CENTER)
                    .outputFormat(file.contentType().substring("image/".length()))
                    .toOutputStream(out);
            return out;
        } catch (IOException e) {
            LOG.warn("Resize compress avatar file throw exception: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
