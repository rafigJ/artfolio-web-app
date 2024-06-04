package ru.vsu.cs.artfolio.service;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.artfolio.dto.post.FullPostResponseDto;
import ru.vsu.cs.artfolio.dto.post.PostRequestDto;
import ru.vsu.cs.artfolio.entity.MediaFileEntity;
import ru.vsu.cs.artfolio.repository.MediaRepository;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.impl.MinioService;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
@Transactional
@Sql(value = "/sql/post_service/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
public class PostServiceIT {

    // test_data.sql
    private static final UUID mockUserUuid = UUID.fromString("7c826e51-b416-475d-97b1-e01b2835db52");
    private static final InputStream mockFile = PostServiceIT.class.getClassLoader().getResourceAsStream("dummy-image.jpg");
    private static MockMultipartFile mockMultipartFile;

    @BeforeAll
    static void downloadFile() throws Exception {
        mockMultipartFile = new MockMultipartFile("dummy-image.jpg", "dummy-image.jpg", "image/jpeg", mockFile);
    }

    @Autowired
    UserRepository userRepository;

    @Autowired
    MinioService minioService;

    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    PostService postService;

    @Test
    void createPost_ValidRequest_ReturnFullPostResponseDto() {
        log.info(String.valueOf(mediaRepository.count()));
        // given
        var executor = userRepository.findById(mockUserUuid).orElseThrow();
        PostRequestDto postRequestDto = new PostRequestDto("post1", "description");

        // when
        FullPostResponseDto post = postService.createPost(executor, postRequestDto, List.of(mockMultipartFile, mockMultipartFile));

        // then
        assertNotNull(post.getId());
        assertEquals("post1", post.getName());
        assertEquals("description", post.getDescription());
        assertEquals(0, post.getLikeCount());
        assertEquals(List.of(1L, 2L), post.getMediaIds());

        var owner = post.getOwner();
        assertEquals(owner.uuid, executor.getUuid());
        assertEquals(owner.fullName, executor.getFullName());
        assertEquals(owner.email, executor.getEmail());
        assertEquals(owner.username, executor.getUsername());

        // after
        clearMinio();
        log.info(String.valueOf(mediaRepository.count()));
    }

    @Test
    void getPostById_ByUnknown_ValidId_ReturnFullPostResponseDto() {
        log.info(String.valueOf(mediaRepository.count()));
        // given
        var executor = userRepository.findById(mockUserUuid).orElseThrow();
        PostRequestDto postRequestDto = new PostRequestDto("post1", "description");
        postService.createPost(executor, postRequestDto, List.of(mockMultipartFile, mockMultipartFile));

        // when
        FullPostResponseDto post = postService.getPostById(null, 1L);

        // then
        assertNotNull(post.getId());
        assertEquals("post1", post.getName());
        assertEquals("description", post.getDescription());
        assertEquals(0, post.getLikeCount());
        assertEquals(List.of(1L, 2L), post.getMediaIds());

        var owner = post.getOwner();
        assertEquals(owner.uuid, executor.getUuid());
        assertEquals(owner.fullName, executor.getFullName());
        assertEquals(owner.email, executor.getEmail());
        assertEquals(owner.username, executor.getUsername());

        // after
        clearMinio();
        log.info(String.valueOf(mediaRepository.count()));
    }

    @Test
    void getPostById_ByUser_ValidId_ReturnFullPostResponseDto() {
        log.info(String.valueOf(mediaRepository.count()));
        // given
        var executor = userRepository.findById(mockUserUuid).orElseThrow();
        PostRequestDto postRequestDto = new PostRequestDto("post1", "description");
        postService.createPost(executor, postRequestDto, List.of(mockMultipartFile, mockMultipartFile));

        // when
        FullPostResponseDto post = postService.getPostById(executor, 1L);

        // then
        assertNotNull(post.getId());
        assertEquals("post1", post.getName());
        assertEquals("description", post.getDescription());
        assertEquals(0, post.getLikeCount());
        assertEquals(List.of(1L, 2L), post.getMediaIds());

        var owner = post.getOwner();
        assertEquals(owner.uuid, executor.getUuid());
        assertEquals(owner.fullName, executor.getFullName());
        assertEquals(owner.email, executor.getEmail());
        assertEquals(owner.username, executor.getUsername());

        // after
        clearMinio();
        log.info(String.valueOf(mediaRepository.count()));
    }

    /**
     * Т.к. test container для minio я ещё не настроил, интеграционный тест работает с реальным
     * s3 хранилищем. Следовательно, нам нужно очищать созданные файлы.
     * (Можно также мокать, но так больше случаев можно покрыть)
     * Метод не помечен @AfterEach т.к. транзакции в рамках тестового метода откатываются
     */
    private void clearMinio() {
        var mediaFiles = mediaRepository.findAll().stream().map(MediaFileEntity::getFileName).toList();
        minioService.deleteFiles(mediaFiles);
    }
}
