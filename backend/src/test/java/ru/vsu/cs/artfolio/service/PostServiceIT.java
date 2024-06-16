package ru.vsu.cs.artfolio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.entity.MediaFileEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.repository.MediaRepository;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.impl.MinioService;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.vsu.cs.artfolio.data.TestDataConstants.ADMIN_UUID;
import static ru.vsu.cs.artfolio.data.TestDataConstants.POST_1_DESCRIPTION;
import static ru.vsu.cs.artfolio.data.TestDataConstants.POST_1_ID;
import static ru.vsu.cs.artfolio.data.TestDataConstants.POST_1_LIKE_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.POST_1_MEDIA_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.POST_1_NAME;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_EMAIL;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_FULL_NAME;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_USERNAME;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_UUID;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@Sql(value = "/sql/before_all/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/after_all/test_data_clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PostServiceIT {

    private MockMultipartFile mockMultipartFile;

    @Autowired
    UserRepository userRepository;

    @SpyBean
    MinioService minioService;

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @BeforeEach
    void downloadFile() throws Exception {
        final InputStream mockFile = PostServiceIT.class.getClassLoader().getResourceAsStream("dummy-image.jpg");
        mockMultipartFile = new MockMultipartFile("dummy-image.jpg", "dummy-image.jpg", "image/jpeg", mockFile);
    }

    @Test
    void createPost_ValidRequest_ReturnFullPostResponseDto() throws Exception {
        // given
        UserEntity executor = userRepository.findById(USER_UUID).orElseThrow();
        PostRequestDto postRequestDto = new PostRequestDto("post1", "description");

        // when
        FullPostResponseDto savedPost = postService.createPost(executor, postRequestDto, List.of(mockMultipartFile, mockMultipartFile));

        // then
        assertNotNull(savedPost.getId());
        assertEquals("post1", savedPost.getName());
        assertEquals("description", savedPost.getDescription());
        assertEquals(0, savedPost.getLikeCount());
        assertEquals(2, savedPost.getMediaIds().size());

        UserResponseDto owner = savedPost.getOwner();
        assertUserEquals(owner);
        clearMinio();
    }

    @Test
    void getPostById_ByUnknown_ValidId_ReturnFullPostResponseDto() {
        // given savedPost with 1L id

        // when
        FullPostResponseDto post = postService.getPostById(null, 1L);

        // then
        assertPost1Equals(post);
        assertNull(post.getHasLike());

        UserResponseDto owner = post.getOwner();
        assertUserEquals(owner);
    }

    @Test
    void getPostById_ByUser_ValidId_ReturnFullPostResponseDto() {
        // given
        var executor = userRepository.findById(USER_UUID).orElseThrow();

        // when
        FullPostResponseDto post = postService.getPostById(executor, 1L);

        // then
        assertPost1Equals(post);
        assertEquals(false, post.getHasLike());

        UserResponseDto owner = post.getOwner();
        assertUserEquals(owner);
    }

    @Test
    void getPostById_ByAdmin_ValidId_ReturnFullPostResponseDto() throws Exception {
        // given
        var executor = userRepository.findById(ADMIN_UUID).orElseThrow();

        // when
        FullPostResponseDto post = postService.getPostById(executor, 1L);

        // then
        assertPost1Equals(post);
        assertEquals(false, post.getHasLike());

        UserResponseDto owner = post.getOwner();
        assertUserEquals(owner);
    }

    @Test
    void getPostById_ByUnknown_InvalidId_ThrowsNotFoundException() throws Exception {
        // given not saved post with 1000L id

        // when & then
        assertThrows(NotFoundException.class, () -> postService.getPostById(null, 1000L));
    }

    /**
     * Т.к. test container для minio я ещё не настроил, интеграционный тест работает с реальным
     * s3 хранилищем. Следовательно, нам нужно очищать созданные файлы.
     * (Можно также мокать, но так больше случаев можно покрыть)
     */
    void clearMinio() {
        var mediaFiles = mediaRepository.findAll().parallelStream().map(MediaFileEntity::getFileName).toList();
        var postPreviewFiles = postRepository.findAll().parallelStream().map(PostEntity::getPreviewMediaName).toList();
        minioService.deleteFiles(mediaFiles);
        minioService.deleteFiles(postPreviewFiles);
    }

    private static void assertPost1Equals(FullPostResponseDto actual) {
        assertEquals(POST_1_ID, actual.id);
        assertEquals(POST_1_NAME, actual.getName());
        assertEquals(POST_1_DESCRIPTION, actual.getDescription());
        assertEquals(POST_1_LIKE_COUNT, actual.getLikeCount());
        assertEquals(POST_1_MEDIA_COUNT, actual.getMediaIds().size());
    }

    private static void assertUserEquals(UserResponseDto actual) {
        assertEquals(USER_UUID, actual.uuid);
        assertEquals(USER_FULL_NAME, actual.fullName);
        assertEquals(USER_EMAIL, actual.email);
        assertEquals(USER_USERNAME, actual.username);
    }
}
