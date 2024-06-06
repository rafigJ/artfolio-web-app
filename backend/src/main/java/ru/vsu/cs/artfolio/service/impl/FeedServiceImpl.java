package ru.vsu.cs.artfolio.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.FeedService;
import ru.vsu.cs.artfolio.service.PostService;

import static ru.vsu.cs.artfolio.criteria.PostSpecifications.*;

@Service
@RequiredArgsConstructor
public class FeedServiceImpl implements FeedService {

    private final PostService postService;
    private final UserRepository userRepository;

    @Override
    public PageDto<PostResponseDto> getPostsPageOrderedByTime(Pageable page) {
        return postService.getPostsPageBySpecifications(sortByCreateTime(), page);
    }

    @Override
    public PageDto<PostResponseDto> getPostsPageByName(String name, Pageable page) {
        Specification<PostEntity> specification = nameContainsIgnoreCaseSortByCreateTime(name);
        return postService.getPostsPageBySpecifications(specification, page);
    }

    @Override
    public PageDto<PostResponseDto> getPostsPageOrderedByPopularity(Pageable page) {
        return postService.getPostsPageBySpecifications(sortByLikeCount(), page);
    }

    @Override
    public PageDto<PostResponseDto> getPostsPageOrderedByFollowerSubscribe(UserEntity user, Pageable page) {
        return postService.getPostsPageBySpecifications(postsByFollowedUsers(user), page);
    }

    @Override
    public PageDto<PostResponseDto> getPostsPageBySpecifications(Specification<PostEntity> specification, Pageable page) {
        return postService.getPostsPageBySpecifications(specification, page);
    }
}
