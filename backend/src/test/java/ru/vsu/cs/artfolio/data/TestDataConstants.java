package ru.vsu.cs.artfolio.data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

/***
 * Константы из файла test_data.sql
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestDataConstants {

    /*/*****************************************************/
    /* USERS                                               */
    /*/*****************************************************/

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
    public static final String SECRET_WORD = "winterIsComing";

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
    public static final String ADMIN_COUNTRY = "Admin County";
    public static final String ADMIN_CITY = "Admin";
    public static final String DELETED_COUNTRY = "Deleted";
    public static final String DELETED_CITY = "Deleted";

    // Description
    public static final String DESCRIPTION = "Born on Vesteros";

    // USER additional info (like count, post count, subscribers, followers)
    public static final Long USER_LIKE_COUNT = 2L;
    public static final Long USER_POST_COUNT = 1L;
    public static final Long USER_SUBSCRIBERS_COUNT = 0L;
    public static final Long USER_FOLLOWING_COUNT = 0L;

    // DELETED USER additional info (like count, post count, subscribers, followers)
    public static final Long DELETED_LIKE_COUNT = 0L;
    public static final Long DELETED_POST_COUNT = 0L;
    public static final Long DELETED_SUBSCRIBERS_COUNT = 0L;
    public static final Long DELETED_FOLLOWING_COUNT = 0L;

    // ADMIN additional info (like count, post count, subscribers, followers)
    public static final Long ADMIN_LIKE_COUNT = 0L;
    public static final Long ADMIN_POST_COUNT = 0L; // not deleted
    public static final Long ADMIN_SUBSCRIBERS_COUNT = 0L;
    public static final Long ADMIN_FOLLOWING_COUNT = 0L;


    /*/*****************************************************/
    /* POSTS                                               */
    /*/*****************************************************/

    public static final Long POST_1_ID = 1L;
    public static final UUID POST_1_OWNER_ID = USER_UUID;
    public static final String POST_1_NAME = "post1";
    public static final String POST_1_DESCRIPTION = "description";

    // Post 1 additional info (like_count, media_count)
    public static final Long POST_1_MEDIA_COUNT = 2L;
    public static final Long POST_1_LIKE_COUNT = 2L;


}
