package ru.vsu.cs.artfolio.controller.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import ru.vsu.cs.artfolio.auth.user.Role;
import ru.vsu.cs.artfolio.auth.user.User;
import ru.vsu.cs.artfolio.controller.PostController;
import ru.vsu.cs.artfolio.dto.MediaDto;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.PostLikeResponse;
import ru.vsu.cs.artfolio.dto.comment.CommentRequestDto;
import ru.vsu.cs.artfolio.dto.comment.CommentResponseDto;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.service.CommentService;
import ru.vsu.cs.artfolio.service.PostService;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostControllerUnitTest {

    @Mock
    private PostService postService;

    @Mock
    private CommentService commentService;

    @InjectMocks
    private PostController postController;

    private UserEntity testUserEntity;
    private UserResponseDto testUserResponse;

    @Mock
    private User testUser;

    @BeforeEach
    void setUp() {
        var uuid = UUID.randomUUID();
        testUserEntity = UserEntity.builder()
                .uuid(uuid)
                .email("test@user.com")
                .username("testUser")
                .password("password")
                .secretWord("secret")
                .fullName("Test User")
                .country("Country")
                .city("City")
                .additionalInfo("Additional Info")
                .avatarName("avatar.jpg")
                .avatarType("image/jpeg")
                .role(Role.USER)
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .deleted(false)
                .build();
        testUserResponse = UserResponseDto.builder()
                .uuid(uuid)
                .email("test@user.com")
                .fullName("Test User")
                .username("testUser")
                .build();
        testUser = mock(User.class);
    }

    @Test
    void createPost_ValidRequest_ReturnsCreatedPost() {
        // given
        PostRequestDto postRequestDto = new PostRequestDto("Test Post", "Test Description");
        List<MultipartFile> files = Collections.emptyList();
        FullPostResponseDto createdPost = FullPostResponseDto.builder()
                .id(1L)
                .name("Test Post")
                .description("Test Description")
                .build();

        when(testUser.getUserEntity()).thenReturn(testUserEntity);
        when(postService.createPost(any(), any(), any())).thenReturn(createdPost);

        // when
        ResponseEntity<FullPostResponseDto> response = postController.createPost(testUser, postRequestDto, files);

        // then
        assertEquals(createdPost, response.getBody());
        verify(postService).createPost(any(UserEntity.class), any(PostRequestDto.class), anyList());
    }

    @Test
    void getPostById_ExistingPost_ReturnsPost() {
        // given
        FullPostResponseDto postResponse = FullPostResponseDto.builder()
                .id(1L)
                .name("Test Post")
                .description("Test Description")
                .build();
        when(testUser.getUserEntity()).thenReturn(testUserEntity);
        when(postService.getPostById(any(), anyLong())).thenReturn(postResponse);

        // when
        ResponseEntity<FullPostResponseDto> response = postController.getPostById(1L, testUser);

        // then
        assertEquals(postResponse, response.getBody());
        verify(postService).getPostById(testUserEntity, 1L);
    }

    @Test
    void updatePost_ValidRequest_ReturnsUpdatedPost() {
        // given
        PostRequestDto postRequestDto = new PostRequestDto("Updated Post", "Updated Description");
        List<MultipartFile> files = Collections.emptyList();
        FullPostResponseDto updatedPost = FullPostResponseDto.builder()
                .id(1L)
                .name("Updated Post")
                .description("Updated Description")
                .build();

        when(testUser.getUserEntity()).thenReturn(testUserEntity);
        when(postService.updatePost(any(), anyLong(), any(), anyList())).thenReturn(updatedPost);

        // when
        ResponseEntity<FullPostResponseDto> response = postController.updatePost(1L, testUser, postRequestDto, files);

        // then
        assertEquals(updatedPost, response.getBody());
        verify(postService).updatePost(testUserEntity, 1L, postRequestDto, files);
    }

    @Test
    void deletePost_ValidRequest_ReturnsSuccessMessage() {
        // given
        when(testUser.getUserEntity()).thenReturn(testUserEntity);
        doNothing().when(postService).deletePost(any(), anyLong());

        // when
        ResponseEntity<String> response = postController.deletePost(1L, testUser);

        // then
        assertEquals("{\"message\": \"post 1 is deleted\"}", response.getBody());
        verify(postService).deletePost(testUserEntity, 1L);
    }

    @Test
    void likePost_ValidRequest_ReturnsLikeCount() {
        // given
        when(testUser.getUserEntity()).thenReturn(testUserEntity);
        when(postService.likePost(any(), anyLong())).thenReturn(10L);

        // when
        ResponseEntity<PostLikeResponse> response = postController.likePost(1L, testUser);

        // then
        assertEquals(new PostLikeResponse(1L, 10L), response.getBody());
        verify(postService).likePost(testUserEntity.getUuid(), 1L);
    }

    @Test
    void deleteLike_ValidRequest_ReturnsLikeCount() {
        // given
        when(testUser.getUserEntity()).thenReturn(testUserEntity);
        when(postService.deleteLikeFromPost(any(), anyLong())).thenReturn(9L);

        // when
        ResponseEntity<PostLikeResponse> response = postController.deleteLike(1L, testUser);

        // then
        assertEquals(new PostLikeResponse(1L, 9L), response.getBody());
        verify(postService).deleteLikeFromPost(testUserEntity.getUuid(), 1L);
    }

    @Test
    void getCommentPageByPostId_ValidRequest_ReturnsCommentsPage() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        PageDto<CommentResponseDto> commentsPage = new PageDto<>(Collections.emptyList(), 0L, 1);

        when(commentService.getAllByPostId(anyLong(), any(Pageable.class))).thenReturn(commentsPage);

        // when
        ResponseEntity<PageDto<CommentResponseDto>> response = postController.getCommentPageByPostId(0, 10, 1L);

        // then
        assertEquals(commentsPage, response.getBody());
        verify(commentService).getAllByPostId(1L, pageable);
    }

    @Test
    void commentPost_ValidRequest_ReturnsCreatedComment() {
        // given
        CommentRequestDto commentRequest = new CommentRequestDto("Test Comment");
        CommentResponseDto createdComment = new CommentResponseDto(1L, testUserResponse, "Test Comment", LocalDateTime.now());

        when(testUser.getUserEntity()).thenReturn(testUserEntity);
        when(commentService.createComment(any(), anyLong(), any())).thenReturn(createdComment);

        // when
        ResponseEntity<CommentResponseDto> response = postController.commentPost(1L, commentRequest, testUser);

        // then
        assertEquals(createdComment, response.getBody());
        verify(commentService).createComment(testUserEntity, 1L, commentRequest);
    }

    @Test
    void deleteComment_ValidRequest_ReturnsOk() {
        // given
        when(testUser.getUserEntity()).thenReturn(testUserEntity);
        doNothing().when(commentService).deleteCommentById(any(), anyLong(), anyLong());

        // when
        ResponseEntity<?> response = postController.deleteComment(1L, 1L, testUser);

        // then
        assertEquals(ResponseEntity.ok().build(), response);
        verify(commentService).deleteCommentById(testUserEntity, 1L, 1L);
    }

    @Test
    void downloadPreview_ValidRequest_ReturnsMedia() {
        // given
        MediaDto media = new MediaDto(new ByteArrayInputStream(new byte[0]), "image/jpeg");

        when(postService.getPreviewByPostId(anyLong())).thenReturn(media);

        // when
        ResponseEntity<InputStreamResource> response = postController.downloadPreview(1L);

        // then
        assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());
        verify(postService).getPreviewByPostId(1L);
    }

    @Test
    void downloadMedia_ValidRequest_ReturnsMedia() {
        // given
        MediaDto media = new MediaDto(new ByteArrayInputStream(new byte[0]), "image/jpeg");

        when(postService.getMediaById(anyLong())).thenReturn(media);

        // when
        ResponseEntity<InputStreamResource> response = postController.downloadMedia(1L);

        // then
        assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());
        verify(postService).getMediaById(1L);
    }
}

