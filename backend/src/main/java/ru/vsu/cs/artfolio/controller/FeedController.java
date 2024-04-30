package ru.vsu.cs.artfolio.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
import ru.vsu.cs.artfolio.service.FeedService;

@RestController
@RequestMapping("/api/v1/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping()
    public ResponseEntity<PageDto<PostResponseDto>> getPostsPage(@RequestParam(value = "_page", defaultValue = "0") Integer page,
                                                                 @RequestParam(value = "_limit", defaultValue = "10") Integer limit,
                                                                 @RequestParam(value = "section", defaultValue = "NEW") FeedSection section, @AuthenticationPrincipal User user) {
        Pageable pageable = PageRequest.of(page, limit);
        PageDto<PostResponseDto> posts = switch (section) {
            case NEW -> feedService.getPostsPageOrderedByTime(pageable);
            case POPULAR -> feedService.getPostsPageOrderedByPopularity(pageable);
            case SUBSCRIBE -> {
                if (user == null) {
                    throw new BadRequestException("user must be logged in for SUBSCRIBE query");
                }
                yield feedService.getPostsPageOrderedByFollowerSubscribe(user.getUserEntity().getUuid(), pageable);
            }
        };
        return ResponseEntity.ok(posts);
    }

}