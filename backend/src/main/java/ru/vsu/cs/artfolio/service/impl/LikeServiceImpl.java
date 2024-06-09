package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vsu.cs.artfolio.entity.LikeEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.repository.LikeRepository;
import ru.vsu.cs.artfolio.service.LikeService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final LikeRepository likeRepository;

    @Override
    public void createLike(UUID executorId, Long postId) {
        if (likeRepository.existsByUserUuidAndPostId(executorId, postId)) {
            return;
        }
        likeRepository.save(
                LikeEntity.builder()
                        .userUuid(executorId)
                        .postId(postId)
                        .post(PostEntity.builder().id(postId).build())
                        .build()
        );
    }

    @Override
    public boolean hasLike(UUID userId, Long postId) {
        return likeRepository.existsByUserUuidAndPostId(userId, postId);
    }

    @Override
    public Long getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    @Override
    public Long getLikeCount(List<Long> postIds) {
        return likeRepository.countByPostIdIn(postIds);
    }

    @Override
    public List<Long> getLikeCountsEachPostInList(List<Long> postIds) {
        List<Long[]> counts = likeRepository.countEach(postIds);
        Map<Long, Long> postIdLikeCountMap = counts.stream().collect(Collectors.toMap(
                count -> count[0],
                count -> count[1]
        ));
        return postIds.stream().map(postIdLikeCountMap::get).toList();
    }

    @Override
    public void deleteLike(UUID executorId, Long postId) {
        Optional<LikeEntity> likeEntity = likeRepository.findByUserUuidAndPostId(executorId, postId);
        if (likeEntity.isEmpty()) {
            return;
        }
        likeRepository.delete(likeEntity.get());
    }
}
