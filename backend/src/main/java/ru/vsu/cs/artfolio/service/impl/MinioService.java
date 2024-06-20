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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MinioService {

    private static final Logger LOG = LoggerFactory.getLogger(MinioService.class);

    @Value("${application.minio.bucketName}")
    private String bucketName;

    private final MinioClient minioClient;

    public MinioResult uploadPreviewFile(MultipartFile file) {
        try {
            ByteArrayOutputStream imageFile = resizeCompressImage(file);
            return uploadToMinio(file.getContentType(), imageFile, file.getOriginalFilename());
        } catch (IOException e) {
            LOG.warn("Upload file {} threw exception: {}", file.getName(), e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public MinioResult uploadAvatarFile(MinioRequest file) {
        ByteArrayOutputStream imageFile = resizeCompressImage(file.inputStream(), file.contentType(), 150, 150);
        return uploadToMinio(file.contentType(), imageFile, "avatar");
    }

    public MinioResult uploadFile(MultipartFile file, String name) {
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name)
                    .stream(inputStream, inputStream.available(), -1)
                    .contentType(file.getContentType())
                    .build());
            return new MinioResult(name, file.getContentType());
        } catch (Exception e) {
            LOG.warn("Upload file {} threw exception: {}", file.getOriginalFilename(), e.getMessage());
            throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    public InputStream downloadFile(String fileName) {
        try {
            return minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            LOG.warn("Download file {} threw exception: {}", fileName, e.getMessage());
            throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    public void deleteFiles(List<String> fileNames) {
        List<DeleteObject> deleteObjects = fileNames.stream().map(DeleteObject::new).collect(Collectors.toList());
        Iterable<Result<DeleteError>> results = minioClient.removeObjects(RemoveObjectsArgs.builder()
                .bucket(bucketName)
                .objects(deleteObjects)
                .build());

        for (Result<DeleteError> result : results) {
            try {
                DeleteError error = result.get();
                LOG.warn("Error in deleting object {}: {}", error.objectName(), error.message());
            } catch (Exception e) {
                LOG.warn("Delete files {} threw exception: {}", fileNames, e.getMessage());
                throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
            }
        }
    }

    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (Exception e) {
            LOG.warn("Delete file {} threw exception: {}", fileName, e.getMessage());
            throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }

    private static ByteArrayOutputStream resizeCompressImage(MultipartFile file) throws IOException {
        return resizeCompressImage(new ByteArrayInputStream(file.getBytes()), file.getContentType(), 406, 204);
    }

    private static ByteArrayOutputStream resizeCompressImage(InputStream inputStream, String contentType, int width, int height) {
        try (InputStream in = inputStream; ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            BufferedImage originalImage = ImageIO.read(in);
            if (originalImage == null) {
                throw new BadRequestException("File has a bad signature (it's not in jpg/png format)");
            }

            if (contentType == null || !contentType.startsWith("image/")) {
                throw new BadRequestException("File must have a valid image content-type");
            }

            String outputFormat = contentType.substring("image/".length());
            Thumbnails.of(originalImage)
                    .size(width, height)
                    .crop(Positions.CENTER)
                    .outputFormat(outputFormat)
                    .toOutputStream(byteArrayOutputStream);

            return byteArrayOutputStream;
        } catch (IOException e) {
            throw new RuntimeException("Resize compress image threw exception", e);
        }
    }

    private MinioResult uploadToMinio(String contentType, ByteArrayOutputStream imageFile, String fileName) {
        try (InputStream is = new ByteArrayInputStream(imageFile.toByteArray())) {
            String name = UUID.randomUUID().toString();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(name)
                    .stream(is, is.available(), -1)
                    .contentType(contentType)
                    .build());
            return new MinioResult(name, contentType);
        } catch (Exception e) {
            LOG.warn("Upload file {} threw exception: {}", fileName, e.getMessage());
            throw new RestException(e.getMessage(), HttpStatus.BAD_GATEWAY);
        }
    }
}
