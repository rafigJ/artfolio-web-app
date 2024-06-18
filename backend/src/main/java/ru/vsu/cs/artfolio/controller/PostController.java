package ru.vsu.cs.artfolio.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.auth.user.User;
import ru.vsu.cs.artfolio.dto.MediaDto;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.PostLikeResponse;
import ru.vsu.cs.artfolio.dto.RestExceptionDto;
import ru.vsu.cs.artfolio.dto.comment.CommentRequestDto;
import ru.vsu.cs.artfolio.dto.comment.CommentResponseDto;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.service.CommentService;
import ru.vsu.cs.artfolio.service.PostService;

import javax.annotation.Nullable;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private static final Logger LOG = LoggerFactory.getLogger(PostController.class);

    private final PostService service;
    private final CommentService commentService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FullPostResponseDto> createPost(
            @AuthenticationPrincipal User user,
            @RequestPart("post") @Valid PostRequestDto post,
            @RequestPart(value = "file", required = false) List<MultipartFile> listFiles) {
        LOG.info("User {} saves the post {}", user.getUsername(), post);
        var createdPost = service.createPost(user.getUserEntity(), post, listFiles);
        return ResponseEntity.ok(createdPost);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FullPostResponseDto> getPostById(@PathVariable("id") Long id, @Nullable @AuthenticationPrincipal User user) {
        LOG.info("Get post by id: {} by user: {}", id, user != null ? user.getUsername() : null);
        UserEntity userEntity = user != null ? user.getUserEntity() : null;
        return ResponseEntity.ok(service.getPostById(userEntity, id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<FullPostResponseDto> updatePost(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal User user,
            @RequestPart("post") @Valid PostRequestDto post,
            @RequestPart("file") List<MultipartFile> listFiles) {

        LOG.info("User {} update post {}", user.getUsername(), post);
        var updatedPost = service.updatePost(user.getUserEntity(), id, post, listFiles);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> deletePost(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal User user) {
        LOG.info("User {} delete post by id {}", user.getUsername(), id);
        service.deletePost(user.getUserEntity(), id);
        return ResponseEntity.ok("{\"message\": \"post " + id + " is deleted\"}");
    }

    @PostMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostLikeResponse> likePost(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        LOG.info("User {} likes post with id {}", user.getUsername(), id);
        Long likeCount = service.likePost(user.getUserEntity().getUuid(), id);
        return ResponseEntity.ok(new PostLikeResponse(id, likeCount));
    }

    @DeleteMapping("/{id}/like")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PostLikeResponse> deleteLike(@PathVariable("id") Long id, @AuthenticationPrincipal User user) {
        LOG.info("User {} delete like from post with id {}", user.getUsername(), id);
        Long likeCount = service.deleteLikeFromPost(user.getUserEntity().getUuid(), id);
        return ResponseEntity.ok(new PostLikeResponse(id, likeCount));
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<PageDto<CommentResponseDto>> getCommentPageByPostId(@RequestParam(value = "_page", defaultValue = "0") Integer page,
                                                                              @RequestParam(value = "_limit", defaultValue = "10") Integer limit,
                                                                              @PathVariable("id") Long id) {
        return ResponseEntity.ok(commentService.getAllByPostId(id, PageRequest.of(page, limit)));
    }

    @PostMapping("/{id}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentResponseDto> commentPost(@PathVariable("id") Long id,
                                                          @RequestBody @Valid CommentRequestDto comment,
                                                          @AuthenticationPrincipal User user) {
        LOG.info("User {} comments post by id {}", user.getUsername(), id);
        CommentResponseDto createdComment = commentService.createComment(user.getUserEntity(), id, comment);
        return ResponseEntity.ok(createdComment);
    }

    @DeleteMapping("/{id}/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteComment(@PathVariable("id") Long id,
                                           @PathVariable("commentId") Long commentId,
                                           @AuthenticationPrincipal User user) {
        commentService.deleteCommentById(user.getUserEntity(), id, commentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/preview")
    public ResponseEntity<InputStreamResource> downloadPreview(@PathVariable Long id) {
        try {
            MediaDto media = service.getPreviewByPostId(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(media.contentType()))
                    .body(new InputStreamResource(media.fileStream()));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/medias/{id}")
    public ResponseEntity<InputStreamResource> downloadMedia(@PathVariable Long id) {
        try {
            MediaDto media = service.getMediaById(id);
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(media.contentType()))
                    .body(new InputStreamResource(media.fileStream()));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
