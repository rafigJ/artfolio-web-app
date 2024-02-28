package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.service.PostService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private static final String FOLDER_PATH = "D:/Java/someFolder";

    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        Path filePath = Path.of(FOLDER_PATH, file.getOriginalFilename());
        postRepository.save(PostEntity.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .filePath(filePath.toString())
                .build());

        file.transferTo(new File(filePath.toString()));

        return "post uploaded successfully: " + filePath;
    }

    @Override
    public byte[] downloadImage(String fileName) throws IOException {
        PostEntity postData = postRepository.findByName(fileName)
                .orElseThrow(() -> new NotFoundException("Image with " + fileName + " name not found"));
        String imagePath = postData.getFilePath();
        return Files.readAllBytes(Path.of(imagePath));
    }
}
