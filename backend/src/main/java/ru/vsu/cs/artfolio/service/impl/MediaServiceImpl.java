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
    public byte[] downloadMedia(Long mediaId) {
        MediaFileEntity dbImageData = repository.findById(mediaId)
                .orElseThrow(() -> new NotFoundException("Media by id: " + mediaId + " not found"));

        return dbImageData.getFile();
    }

    @Override
    public List<Long> getMediaIdsByPostId(Long postId) {
        throw new UnsupportedOperationException();
//        List<MediaFileEntity> mediaList = repository.findAllByPostIdOrderByPosition(postId);
//        if (mediaList.isEmpty()) {
//            throw new NotFoundException("Post by id" + postId + " not found");
//        }
//        return mediaList.stream().map(MediaFileEntity::getId).toList();
    }
}
