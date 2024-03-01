package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.service.PostService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Value("${application.post.folder-path}")
    private String FOLDER_PATH;

    @Override
    @Transactional
    public void uploadImages(List<MultipartFile> images) throws IOException {
        List<PostEntity> newPosts = new ArrayList<>();
        for (MultipartFile file : images) {
            Path filePath = Path.of(FOLDER_PATH, file.getOriginalFilename());
            PostEntity newPost = PostEntity.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .filePath(filePath.toString())
                    .build();
            newPosts.add(newPost);
            file.transferTo(new File(filePath.toString()));
        }
        postRepository.saveAllAndFlush(newPosts);
    }

    @Override
    public byte[] downloadImages(String fileName) throws IOException {
        PostEntity postData = postRepository.findByName(fileName)
                .orElseThrow(() -> new NotFoundException("Image with " + fileName + " name not found"));
        String imagePath = postData.getFilePath();
        return Files.readAllBytes(Path.of(imagePath));
    }
}
