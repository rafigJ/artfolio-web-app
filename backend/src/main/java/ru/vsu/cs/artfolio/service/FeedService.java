package ru.vsu.cs.artfolio.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.entity.PostEntity;

import java.util.UUID;

public interface FeedService {

    PageDto<PostResponseDto> getPostsPageOrderedByTime(Pageable page);

    PageDto<PostResponseDto> getPostsPageByName(String name, Pageable page);

    PageDto<PostResponseDto> getPostsPageOrderedByPopularity(Pageable page);

    PageDto<PostResponseDto> getPostsPageOrderedByFollowerSubscribe(UUID userId, Pageable page);

    PageDto<PostResponseDto> getPostsPageBySpecifications(Specification<PostEntity> specification, Pageable page);

}
