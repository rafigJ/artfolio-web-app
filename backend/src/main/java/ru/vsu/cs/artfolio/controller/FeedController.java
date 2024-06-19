package ru.vsu.cs.artfolio.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vsu.cs.artfolio.auth.user.User;
import ru.vsu.cs.artfolio.controller.enums.FeedSection;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.exception.BadRequestException;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.service.FeedService;

import javax.annotation.Nullable;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {

    private static final Logger LOG = LoggerFactory.getLogger(FeedController.class);
    private final FeedService feedService;

    @GetMapping()
    public ResponseEntity<PageDto<PostResponseDto>> getPostsPage(@RequestParam(value = "_page", defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "_limit", defaultValue = "10") Integer limit,
                                                                 @RequestParam(value = "section", defaultValue = "NEW") FeedSection section,
                                                                 @Nullable @AuthenticationPrincipal User user) {
        Pageable pageable = PageRequest.of(page, limit);
        PageDto<PostResponseDto> posts = switch (section) {
            case NEW -> {
                LOG.info("Get posts NEW page = {}, limit = {}", page, limit);
                yield feedService.getPostsPageOrderedByTime(pageable);
            }
            case POPULAR -> {
                LOG.info("Get posts POPULAR page = {}, limit = {}", page, limit);
                yield feedService.getPostsPageOrderedByPopularity(pageable);
            }
            case SUBSCRIBE -> {
                if (user == null) {
                    throw new RestException("user must be logged in for SUBSCRIBE query", HttpStatus.UNAUTHORIZED);
                }
                LOG.info("User {} get posts SUBSCRIBE page = {}, limit = {}", user.getUserEntity().getUsername(), page, limit);
                yield feedService.getPostsPageOrderedByFollowerSubscribe(user.getUserEntity(), pageable);
            }
        };
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/search")
    public ResponseEntity<PageDto<PostResponseDto>> searchPosts(@RequestParam(value = "_page", defaultValue = "0") Integer page,
                                                                @RequestParam(value = "_limit", defaultValue = "10") Integer limit,
                                                                @RequestParam(value = "name") String name) {
        LOG.info("Get posts by search query: {}", name);
        Pageable pageable = PageRequest.of(page, limit);
        PageDto<PostResponseDto> posts = feedService.getPostsPageByName(name, pageable);
        return ResponseEntity.ok(posts);
    }
}
