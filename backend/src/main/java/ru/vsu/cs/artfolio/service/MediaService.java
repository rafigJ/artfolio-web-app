package ru.vsu.cs.artfolio.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {

    List<Long> uploadMedia(Long postId, List<MultipartFile> mediaFiles) throws IOException;

    byte[] downloadMedia(Long mediaId);

    List<Long> getMediaIdsByPostId(Long postId);
}
