package ru.vsu.cs.artfolio.service;

import org.junit.jupiter.api.AfterEach;
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
import ru.vsu.cs.artfolio.entity.MediaFileEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.repository.MediaRepository;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.impl.MinioService;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@Sql(value = "/sql/post_service/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
@Sql(value = "/sql/post_service/test_data_update.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PostServiceIT {

    // test_data.sql
    private static final String DUMMY_FILE_NAME_DB = "dummy_file"; // 'saved' file in media_file table
    private static final UUID mockUserUuid = UUID.fromString("7c826e51-b416-475d-97b1-e01b2835db52"); // 'saved' user in _user table
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
    @AfterEach
    void clearMinio() {
        var mediaFiles = mediaRepository.findAll().parallelStream().map(MediaFileEntity::getFileName).toList();
        var postPreviewFiles = postRepository.findAll().parallelStream().map(PostEntity::getPreviewMediaName).toList();
        minioService.deleteFiles(mediaFiles);
        minioService.deleteFiles(postPreviewFiles);
    }

    @Test
    void createPost_ValidRequest_ReturnFullPostResponseDto() throws Exception {
        // given
        var executor = userRepository.findById(mockUserUuid).orElseThrow();
        PostRequestDto postRequestDto = new PostRequestDto("post1", "description");

        // when
        FullPostResponseDto savedPost = postService.createPost(executor, postRequestDto, List.of(mockMultipartFile, mockMultipartFile));

        // then
        assertNotNull(savedPost.getId());
        assertEquals("post1", savedPost.getName());
        assertEquals("description", savedPost.getDescription());
        assertEquals(0, savedPost.getLikeCount());
        assertEquals(2, savedPost.getMediaIds().size());

        var owner = savedPost.getOwner();
        assertEquals(owner.uuid, executor.getUuid());
        assertEquals(owner.fullName, executor.getFullName());
        assertEquals(owner.email, executor.getEmail());
        assertEquals(owner.username, executor.getUsername());

    }

    @Test
    void getPostById_ByUnknown_ValidId_ReturnFullPostResponseDto() {
        // given savedPost with 1L id
        var postOwner = userRepository.findById(mockUserUuid).orElseThrow();

        // when
        FullPostResponseDto post = postService.getPostById(null, 1L);
        // then
        assertNotNull(post.getId());
        assertEquals("post1", post.getName());
        assertEquals("description", post.getDescription());
        assertEquals(0, post.getLikeCount());
        assertEquals(2, post.getMediaIds().size());

        var owner = post.getOwner();
        assertEquals(owner.uuid, postOwner.getUuid());
        assertEquals(owner.fullName, postOwner.getFullName());
        assertEquals(owner.email, postOwner.getEmail());
        assertEquals(owner.username, postOwner.getUsername());
    }

    @Test
    void getPostById_ByUser_ValidId_ReturnFullPostResponseDto() {
        // given
        var executor = userRepository.findById(mockUserUuid).orElseThrow();

        // when
        FullPostResponseDto post = postService.getPostById(executor, 1L);

        // then
        assertNotNull(post.getId());
        assertEquals("post1", post.getName());
        assertEquals("description", post.getDescription());
        assertEquals(0, post.getLikeCount());
        assertEquals(2, post.getMediaIds().size());

        var owner = post.getOwner();
        assertEquals(owner.uuid, executor.getUuid());
        assertEquals(owner.fullName, executor.getFullName());
        assertEquals(owner.email, executor.getEmail());
        assertEquals(owner.username, executor.getUsername());
    }

    @Test
    @Disabled
    void getPostById_ByAdmin_ValidId_ReturnFullPostResponseDto() throws Exception {
        // given

        // when

        // then
    }

    @Test
    void getPostById_ByUnknown_InvalidId_ThrowsNotFoundException() throws Exception {
        // given not saved post with 1000L id

        // when & then
        assertThrows(NotFoundException.class, () -> postService.getPostById(null, 1000L));
    }

}
