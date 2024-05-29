package ru.vsu.cs.artfolio.service;

import java.util.List;
import java.util.UUID;

public interface LikeService {

    void createLike(UUID executorId, Long postId);

    Long getLikeCount(Long postId);

    Long getLikeCount(List<Long> postIds);

    List<Long> getLikeCountsEachPostInList(List<Long> postIds);

    void deleteLike(UUID executorId, Long postId);

}
