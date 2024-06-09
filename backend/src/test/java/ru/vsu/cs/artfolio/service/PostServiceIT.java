package ru.vsu.cs.artfolio.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@Sql(value = "/sql/post_service/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "/sql/post_service/test_data_update.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = "/sql/after_all/test_data_clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
public class PostServiceIT {

    // test_data.sql
    private static final String DUMMY_FILE_NAME_DB = "dummy_file"; // 'saved' file in media_file table
    private static final UUID USER_UUID = UUID.fromString("7c826e51-b416-475d-97b1-e01b2835db52"); // 'saved' user in _user table
    private static final UUID ADMIN_UUID = UUID.fromString("7c826e51-b416-475d-97b1-e01b2835db53"); // 'saved' user in _user table
    private static final InputStream mockFile = PostServiceIT.class.getClassLoader().getResourceAsStream("dummy-image.jpg");
    private static MockMultipartFile mockMultipartFile;

    @BeforeAll
    static void downloadFile() throws Exception {
        mockMultipartFile = new MockMultipartFile("dummy-image.jpg", "dummy-image.jpg", "image/jpeg", mockFile);
    }

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
        assertUserEquals(owner, executor);
        clearMinio();
    }

    @Test
    void getPostById_ByUnknown_ValidId_ReturnFullPostResponseDto() {
        // given savedPost with 1L id
        var realOwner = userRepository.findById(USER_UUID).orElseThrow();

        // when
        FullPostResponseDto post = postService.getPostById(null, 1L);
        // then
        assertNotNull(post.getId());
        assertEquals("post1", post.getName());
        assertEquals("description", post.getDescription());
        assertEquals(0, post.getLikeCount());
        assertEquals(2, post.getMediaIds().size());

        UserResponseDto owner = post.getOwner();
        assertUserEquals(owner, realOwner);
    }

    @Test
    void getPostById_ByUser_ValidId_ReturnFullPostResponseDto() {
        // given
        var executor = userRepository.findById(ADMIN_UUID).orElseThrow();

        // when
        FullPostResponseDto post = postService.getPostById(executor, 1L);

        // then
        assertNotNull(post.getId());
        assertEquals("post1", post.getName());
        assertEquals("description", post.getDescription());
        assertEquals(0, post.getLikeCount());
        assertEquals(2, post.getMediaIds().size());
        assertEquals(false, post.getHasLike());

        UserResponseDto owner = post.getOwner();
        assertUserEquals(owner, executor);
    }

    @Test
    void getPostById_ByAdmin_ValidId_ReturnFullPostResponseDto() throws Exception {
        // given
        var realOwner = userRepository.findById(USER_UUID).orElseThrow();
        var executor = userRepository.findById(ADMIN_UUID).orElseThrow();

        // when
        FullPostResponseDto post = postService.getPostById(executor, 1L);

        // then
        assertNotNull(post.getId());
        assertEquals("post1", post.getName());
        assertEquals("description", post.getDescription());
        assertEquals(0, post.getLikeCount());
        assertEquals(2, post.getMediaIds().size());
        assertEquals(false, post.getHasLike());

        UserResponseDto owner = post.getOwner();
        assertUserEquals(owner, realOwner);
    }

    @Test
    void getPostById_ByUnknown_InvalidId_ThrowsNotFoundException() throws Exception {
        // given not saved post with 1000L id

        // when & then
        assertThrows(NotFoundException.class, () -> postService.getPostById(null, 1000L));
    }

    private void assertUserEquals(UserResponseDto responseDto, UserEntity userEntity) {
        assertEquals(responseDto.uuid, userEntity.getUuid());
        assertEquals(responseDto.fullName, userEntity.getFullName());
        assertEquals(responseDto.email, userEntity.getEmail());
        assertEquals(responseDto.username, userEntity.getUsername());
    }
}
