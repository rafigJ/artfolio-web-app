package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.entity.MediaFileEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.repository.MediaRepository;
import ru.vsu.cs.artfolio.service.MediaService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    private final MediaRepository repository;

    @Override
    public List<Long> uploadMedia(Long postId, List<MultipartFile> mediaFiles) throws IOException {
        List<MediaFileEntity> entities = new ArrayList<>();
        int pos = 0;
        for (MultipartFile mediaFile : mediaFiles) {
            PostEntity post = new PostEntity();
            post.setId(postId);
            entities.add(MediaFileEntity.builder()
                    .post(post)
                    .file(mediaFile.getBytes())
                    .position(pos++)
                    .type(mediaFile.getContentType())
                    .build());
        }

        return repository.saveAllAndFlush(entities).stream()
                .sorted(Comparator.comparingInt(MediaFileEntity::getPosition))
                .map(MediaFileEntity::getId)
                .toList();
    }

    @Override
    public byte[] downloadMedia(Long mediaId) {
        MediaFileEntity dbImageData = repository.findById(mediaId)
                .orElseThrow(() -> new NotFoundException("Media by id: " + mediaId + " not found"));

        return dbImageData.getFile();
    }

    @Override
    public List<Long> getMediaIdsByPostId(Long postId) {
        List<MediaFileEntity> mediaList = repository.findAllByPostIdOrderByPosition(postId);
        if (mediaList.isEmpty()) {
            throw new NotFoundException("Post by id" + postId + " not found");
        }
        return mediaList.stream().map(MediaFileEntity::getId).toList();
    }
}
