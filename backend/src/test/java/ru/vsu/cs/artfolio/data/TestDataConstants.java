package ru.vsu.cs.artfolio.data;

import ru.vsu.cs.artfolio.auth.user.Role;
import ru.vsu.cs.artfolio.entity.MediaFileEntity;
import ru.vsu.cs.artfolio.entity.PostEntity;
import ru.vsu.cs.artfolio.entity.UserEntity;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestDataConstants {

    // User UUIDs
    public static final UUID USER_UUID = UUID.fromString("7c826e51-b416-475d-97b1-e01b2835db52");
    public static final UUID ADMIN_UUID = UUID.fromString("7c826e51-b416-475d-97b1-e01b2835db53");
    public static final UUID DELETED_UUID = UUID.fromString("7c826e51-b416-475d-97b1-e01b2835db54");

    // Usernames
    public static final String USER_USERNAME = "boltonArt";
    public static final String ADMIN_USERNAME = "boltonAdmin";
    public static final String DELETED_USERNAME = "boltonDeleted";

    // Emails
    public static final String USER_EMAIL = "bolton@vesteros.com";
    public static final String ADMIN_EMAIL = "boltonAdmin@vesteros.com";
    public static final String DELETED_EMAIL = "boltonDeleted@vesteros.com";

    // Password and Secret Word
    public static final String PASSWORD = "somePassword19";
    public static final String PASSWORD_HASH = "$2a$10$Z2kInA94pRomtwFtMlZGoeNVyi/mvo2OZu0MAzYMWD6eILKwRm9am";
    public static final String SECRET_WORD = "winterIsComing";
    public static final String SECRET_WORD_HASH = "$2a$10$R/TyfojNSA/mSntqEt/rKeQNoAMsX4JjAIL1cUfSH1i1hVkhTcBz.";

    // Timestamps
    public static final LocalDateTime CREATE_TIME = LocalDateTime.of(2024, 6, 4, 22, 16, 35, 176926000);
    public static final LocalDateTime UPDATE_TIME = LocalDateTime.of(2024, 6, 4, 22, 16, 35, 176926000);

    // Avatar Info
    public static final String AVATAR_NAME = "dummy_file";
    public static final String AVATAR_TYPE = "image/jpeg";

    // Full Names
    public static final String USER_FULL_NAME = "Ramsey Bolton";
    public static final String ADMIN_FULL_NAME = "Ramsey Admin";
    public static final String DELETED_FULL_NAME = "Ramsey Deleted";

    // Locations
    public static final String USER_COUNTRY = "Russia";
    public static final String USER_CITY = "Ufa";
    public static final String DELETED_COUNTRY = "Deleted";
    public static final String DELETED_CITY = "Deleted";

    // Additional Info
    public static final String ADDITIONAL_INFO = "Born on Vesteros";

    // Roles
    public static final String USER_ROLE = "USER";
    public static final String ADMIN_ROLE = "ADMIN";

    // Deletion Status
    public static final Boolean USER_DELETED = false;
    public static final Boolean ADMIN_DELETED = false;
    public static final Boolean DELETED_DELETED = true;

    // UserEntity Mocks
    public static final UserEntity USER_ENTITY = UserEntity.builder()
            .uuid(USER_UUID)
            .email(USER_EMAIL)
            .username(USER_USERNAME)
            .password(PASSWORD_HASH)
            .secretWord(SECRET_WORD_HASH)
            .fullName(USER_FULL_NAME)
            .country(USER_COUNTRY)
            .city(USER_CITY)
            .additionalInfo(ADDITIONAL_INFO)
            .avatarName(AVATAR_NAME)
            .avatarType(AVATAR_TYPE)
            .role(Role.valueOf(USER_ROLE))
            .createTime(CREATE_TIME)
            .updateTime(UPDATE_TIME)
            .deleted(USER_DELETED)
            .build();

    public static final UserEntity ADMIN_ENTITY = UserEntity.builder()
            .uuid(ADMIN_UUID)
            .email(ADMIN_EMAIL)
            .username(ADMIN_USERNAME)
            .password(PASSWORD_HASH)
            .secretWord(SECRET_WORD_HASH)
            .fullName(ADMIN_FULL_NAME)
            .country(USER_COUNTRY)
            .city(USER_CITY)
            .additionalInfo(ADDITIONAL_INFO)
            .avatarName(AVATAR_NAME)
            .avatarType(AVATAR_TYPE)
            .role(Role.valueOf(ADMIN_ROLE))
            .createTime(CREATE_TIME)
            .updateTime(UPDATE_TIME)
            .deleted(ADMIN_DELETED)
            .build();

    public static final UserEntity DELETED_ENTITY = UserEntity.builder()
            .uuid(DELETED_UUID)
            .email(DELETED_EMAIL)
            .username(DELETED_USERNAME)
            .password(PASSWORD_HASH)
            .secretWord(SECRET_WORD_HASH)
            .fullName(DELETED_FULL_NAME)
            .country(DELETED_COUNTRY)
            .city(DELETED_CITY)
            .additionalInfo(ADDITIONAL_INFO)
            .avatarName(AVATAR_NAME)
            .avatarType(AVATAR_TYPE)
            .role(Role.valueOf(USER_ROLE))
            .createTime(CREATE_TIME)
            .updateTime(UPDATE_TIME)
            .deleted(DELETED_DELETED)
            .build();

    // PostEntity Mocks
    public static final PostEntity POST_ENTITY1 = PostEntity.builder()
            .id(1L)
            .name("post1")
            .description("description")
            .previewMediaName(AVATAR_NAME)
            .previewType(AVATAR_TYPE)
            .owner(USER_ENTITY)
            .createTime(CREATE_TIME)
            .deleted(false)
            .build();

    public static final PostEntity POST_ENTITY2 = PostEntity.builder()
            .id(2L)
            .name("deleted_post")
            .description("deletedDescription")
            .previewMediaName(AVATAR_NAME)
            .previewType(AVATAR_TYPE)
            .owner(ADMIN_ENTITY)
            .createTime(CREATE_TIME)
            .deleted(true)
            .build();

    // MediaFileEntity Mocks
    public static final MediaFileEntity MEDIA_FILE1 = MediaFileEntity.builder()
            .id(1L)
            .post(POST_ENTITY1)
            .type(AVATAR_TYPE)
            .fileName(AVATAR_NAME)
            .position(0)
            .build();

    public static final MediaFileEntity MEDIA_FILE2 = MediaFileEntity.builder()
            .id(2L)
            .post(POST_ENTITY1)
            .type(AVATAR_TYPE)
            .fileName(AVATAR_NAME)
            .position(1)
            .build();

    public static final MediaFileEntity MEDIA_FILE3 = MediaFileEntity.builder()
            .id(3L)
            .post(POST_ENTITY2)
            .type(AVATAR_TYPE)
            .fileName(AVATAR_NAME)
            .position(0)
            .build();
}
