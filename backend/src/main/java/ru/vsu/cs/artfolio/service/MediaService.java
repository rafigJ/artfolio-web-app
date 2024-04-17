package ru.vsu.cs.artfolio.service;

import java.util.List;

public interface MediaService {

    byte[] downloadMedia(Long mediaId);

    List<Long> getMediaIdsByPostId(Long postId);
}
