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
import ru.vsu.cs.artfolio.dto.user.FullUserResponseDto;
import ru.vsu.cs.artfolio.dto.user.request.UserUpdateRequestDto;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.NotFoundException;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.impl.MinioService;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static ru.vsu.cs.artfolio.data.TestDataConstants.ADMIN_CITY;
import static ru.vsu.cs.artfolio.data.TestDataConstants.ADMIN_COUNTRY;
import static ru.vsu.cs.artfolio.data.TestDataConstants.ADMIN_EMAIL;
import static ru.vsu.cs.artfolio.data.TestDataConstants.ADMIN_FOLLOWING_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.ADMIN_FULL_NAME;
import static ru.vsu.cs.artfolio.data.TestDataConstants.ADMIN_LIKE_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.ADMIN_POST_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.ADMIN_SUBSCRIBERS_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.ADMIN_USERNAME;
import static ru.vsu.cs.artfolio.data.TestDataConstants.ADMIN_UUID;
import static ru.vsu.cs.artfolio.data.TestDataConstants.AVATAR_NAME;
import static ru.vsu.cs.artfolio.data.TestDataConstants.DELETED_CITY;
import static ru.vsu.cs.artfolio.data.TestDataConstants.DELETED_COUNTRY;
import static ru.vsu.cs.artfolio.data.TestDataConstants.DELETED_EMAIL;
import static ru.vsu.cs.artfolio.data.TestDataConstants.DELETED_FOLLOWING_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.DELETED_FULL_NAME;
import static ru.vsu.cs.artfolio.data.TestDataConstants.DELETED_LIKE_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.DELETED_POST_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.DELETED_SUBSCRIBERS_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.DELETED_USERNAME;
import static ru.vsu.cs.artfolio.data.TestDataConstants.DELETED_UUID;
import static ru.vsu.cs.artfolio.data.TestDataConstants.DESCRIPTION;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_CITY;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_COUNTRY;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_EMAIL;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_FOLLOWING_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_FULL_NAME;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_LIKE_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_POST_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_SUBSCRIBERS_COUNT;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_USERNAME;
import static ru.vsu.cs.artfolio.data.TestDataConstants.USER_UUID;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@Sql(value = "/sql/before_all/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/after_all/test_data_clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserServiceIT {

    private MockMultipartFile mockMultipartFile;

    @Autowired
    UserRepository userRepository;

    @SpyBean
    MinioService minioService;

    @Autowired
    UserService userService;

    @BeforeEach
    void downloadFile() throws Exception {
        final InputStream mockFile = getClass().getClassLoader().getResourceAsStream("dummy-image.jpg");
        mockMultipartFile = new MockMultipartFile("dummy-image.jpg", "dummy-image.jpg", "image/jpeg", mockFile);
    }

    @Test
    void updateUserInformation_ValidUser_ReturnFullUserResponseDto() throws Exception {
        // given
        var executor = userRepository.findById(USER_UUID).orElseThrow();
        UserUpdateRequestDto updatedUser = new UserUpdateRequestDto("New name", "new desc", "new country", "new city", "newusername");

        // when
        var res = userService.updateUserInformation(executor, updatedUser, mockMultipartFile);

        // then
        assertEquals("New name", res.fullName);
        assertEquals("newusername", res.username);
        assertEquals("new desc", res.description);
        assertEquals("new country", res.country);
        assertEquals("new city", res.city);

        clearMinio();
    }

    @Test
    void getUserByUsername_ByUnknown_ExistId_ReturnFullUserResponse() throws Exception {
        // given
        var username = USER_USERNAME;

        // when
        FullUserResponseDto res = userService.getUserByUsername(null, username);

        // then
        assertUserEquals(res);
        assertNull(res.isFollowed);
    }

    @Test
    void getUserByUsername_ByUnknown_NotExistId_ThrowingNotFoundException() throws Exception {
        // given

        // when & then
        assertThrows(NotFoundException.class, () -> userService.getUserByUsername(null, "dummy_random_username"));
    }

    @Test
    void getUserByUsername_ByUnknown_DeletedUser_ThrowingNotFoundException() throws Exception {
        // given

        // when & then
        assertThrows(NotFoundException.class, () -> userService.getUserByUsername(null, DELETED_USERNAME));
    }

    @Test
    void getUserByUsername_ByUser_ExistId_ReturnFullUserResponse() throws Exception {
        // given
        var userExecutor = userRepository.findById(USER_UUID).orElseThrow();
        var username = ADMIN_USERNAME;

        // when
        FullUserResponseDto res = userService.getUserByUsername(userExecutor, username);

        // then
        assertAdminEquals(res);
        assertNotNull(res.isFollowed);
    }

    @Test
    void getUserByUsername_ByUser_NotExistId_ThrowingNotFoundException() throws Exception {
        // given
        var userExecutor = userRepository.findById(USER_UUID).orElseThrow();

        // when & then
        assertThrows(NotFoundException.class, () -> userService.getUserByUsername(userExecutor, "dummy_random_username"));
    }

    @Test
    void getUserByUsername_ByUser_DeletedUser_ThrowingNotFoundException() throws Exception {
        // given
        var userExecutor = userRepository.findById(USER_UUID).orElseThrow();

        // when & then
        assertThrows(NotFoundException.class, () -> userService.getUserByUsername(userExecutor, DELETED_USERNAME));
    }

    @Test
    void getUserByUsername_ByAdmin_ExistId_ReturnFullUserResponse() throws Exception {
        // given
        var adminExecutor = userRepository.findById(ADMIN_UUID).orElseThrow();
        var username = USER_USERNAME;

        // when
        FullUserResponseDto res = userService.getUserByUsername(adminExecutor, username);

        // then
        assertUserEquals(res);
        assertNotNull(res.isFollowed);
    }

    @Test
    void getUserByUsername_ByAdmin_NotExistId_ThrowingNotFoundException() throws Exception {
        // given
        var adminExecutor = userRepository.findById(ADMIN_UUID).orElseThrow();

        // when & then
        assertThrows(NotFoundException.class, () -> userService.getUserByUsername(adminExecutor, "dummy_random_username"));
    }

    @Test
    void getUserByUsername_ByAdmin_DeletedUser_ReturnFullUserResponse() throws Exception {
        // given
        var adminExecutor = userRepository.findById(ADMIN_UUID).orElseThrow();

        // when
        FullUserResponseDto res = userService.getUserByUsername(adminExecutor, DELETED_USERNAME);

        // then
        assertDeletedUserEquals(res);
        assertNotNull(res.isFollowed);
    }

    @Test
    void deleteUser_ByAdmin_DeletesUser() throws Exception {
        // given
        var executor = userRepository.findById(ADMIN_UUID).orElseThrow();

        // when
        userService.deleteUser(executor, USER_USERNAME);

        // then
        var deletedUser = userRepository.findById(USER_UUID).orElseThrow();
        assertTrue(deletedUser.isDeleted());
    }

    @Test
    void downloadAvatar_ReturnsMediaDto() throws Exception {
        // given
        doReturn(mockMultipartFile.getInputStream())
                .when(minioService)
                .downloadFile(AVATAR_NAME);

        var user = userRepository.findById(USER_UUID).orElseThrow();

        // when
        var mediaDto = userService.downloadAvatar(user.getUsername());

        // then
        assertNotNull(mediaDto);
        assertEquals(user.getAvatarType(), mediaDto.contentType());
        assertNotNull(mediaDto.contentType());
    }

    /**
     * Т.к. test container для minio я ещё не настроил, интеграционный тест работает с реальным
     * s3 хранилищем. Следовательно, нам нужно очищать созданные файлы.
     * (Можно также мокать, но так больше случаев можно покрыть)
     */
    void clearMinio() {
        var avatarFiles = userRepository.findAll().parallelStream().map(UserEntity::getAvatarName).toList();
        minioService.deleteFiles(avatarFiles);
    }

    private static void assertUserEquals(FullUserResponseDto actual) {
        assertEquals(USER_UUID, actual.uuid);
        assertEquals(USER_FULL_NAME, actual.fullName);
        assertEquals(DESCRIPTION, actual.description);
        assertEquals(USER_COUNTRY, actual.country);
        assertEquals(USER_CITY, actual.city);
        assertEquals(USER_USERNAME, actual.username);
        assertEquals(USER_EMAIL, actual.email);
        assertEquals(USER_POST_COUNT, actual.postCount);
        assertEquals(USER_SUBSCRIBERS_COUNT, actual.subscribersCount);
        assertEquals(USER_FOLLOWING_COUNT, actual.followingCount);
        assertEquals(USER_LIKE_COUNT, actual.likeCount);
    }

    private static void assertDeletedUserEquals(FullUserResponseDto actual) {
        assertEquals(DELETED_UUID, actual.uuid);
        assertEquals(DELETED_FULL_NAME, actual.fullName);
        assertEquals(DESCRIPTION, actual.description);
        assertEquals(DELETED_COUNTRY, actual.country);
        assertEquals(DELETED_CITY, actual.city);
        assertEquals(DELETED_USERNAME, actual.username);
        assertEquals(DELETED_EMAIL, actual.email);
        assertEquals(DELETED_POST_COUNT, actual.postCount);
        assertEquals(DELETED_SUBSCRIBERS_COUNT, actual.subscribersCount);
        assertEquals(DELETED_FOLLOWING_COUNT, actual.followingCount);
        assertEquals(DELETED_LIKE_COUNT, actual.likeCount);
    }

    private static void assertAdminEquals(FullUserResponseDto actual) {
        assertEquals(ADMIN_UUID, actual.uuid);
        assertEquals(ADMIN_FULL_NAME, actual.fullName);
        assertEquals(DESCRIPTION, actual.description);
        assertEquals(ADMIN_COUNTRY, actual.country);
        assertEquals(ADMIN_CITY, actual.city);
        assertEquals(ADMIN_USERNAME, actual.username);
        assertEquals(ADMIN_EMAIL, actual.email);
        assertEquals(ADMIN_POST_COUNT, actual.postCount);
        assertEquals(ADMIN_SUBSCRIBERS_COUNT, actual.subscribersCount);
        assertEquals(ADMIN_FOLLOWING_COUNT, actual.followingCount);
        assertEquals(ADMIN_LIKE_COUNT, actual.likeCount);
    }

}
