package ru.vsu.cs.artfolio.controller.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.vsu.cs.artfolio.auth.user.Role;
import ru.vsu.cs.artfolio.auth.user.User;
import ru.vsu.cs.artfolio.controller.FeedController;
import ru.vsu.cs.artfolio.controller.enums.FeedSection;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.post.PostResponseDto;
import ru.vsu.cs.artfolio.dto.user.UserResponseDto;
import ru.vsu.cs.artfolio.entity.UserEntity;
import ru.vsu.cs.artfolio.exception.BadRequestException;
import ru.vsu.cs.artfolio.exception.RestException;
import ru.vsu.cs.artfolio.service.FeedService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedControllerUnitTest {

    @Mock
    private FeedService feedService;

    @InjectMocks
    private FeedController feedController;

    private Pageable pageable;
    private PageDto<PostResponseDto> postPage;
    private UserEntity testUserEntity;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 10);
        var uuid = UUID.randomUUID();
        var owner = new UserResponseDto(uuid, "Test User", "test@user.com", "testUser", "Country", "City");
        List<PostResponseDto> posts = Collections.singletonList(new PostResponseDto(1L, "Test Post", 10L, owner));
        postPage = new PageDto<>(posts, 1L, 1);

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
    }

    @Test
    void getPostsPage_NewSection_ReturnsPosts() {
        // given
        when(feedService.getPostsPageOrderedByTime(pageable)).thenReturn(postPage);

        // when
        ResponseEntity<PageDto<PostResponseDto>> response = feedController.getPostsPage(0, 10, FeedSection.NEW, null);

        // then
        assertEquals(postPage, response.getBody());
        verify(feedService).getPostsPageOrderedByTime(pageable);
    }

    @Test
    void getPostsPage_PopularSection_ReturnsPosts() {
        // given
        when(feedService.getPostsPageOrderedByPopularity(pageable)).thenReturn(postPage);

        // when
        ResponseEntity<PageDto<PostResponseDto>> response = feedController.getPostsPage(0, 10, FeedSection.POPULAR, null);

        // then
        assertEquals(postPage, response.getBody());
        verify(feedService).getPostsPageOrderedByPopularity(pageable);
    }

    @Test
    void getPostsPage_SubscribeSectionWithoutUser_ThrowsBadRequestException() {
        // given - no specific setup needed

        // when & then
        assertThrows(RestException.class, () -> feedController.getPostsPage(0, 10, FeedSection.SUBSCRIBE, null));
    }

    @Test
    void getPostsPage_SubscribeSectionWithUser_ReturnsPosts() {
        // given
        User user = mock(User.class);
        when(user.getUserEntity()).thenReturn(testUserEntity);
        when(feedService.getPostsPageOrderedByFollowerSubscribe(any(), eq(pageable))).thenReturn(postPage);

        // when
        ResponseEntity<PageDto<PostResponseDto>> response = feedController.getPostsPage(0, 10, FeedSection.SUBSCRIBE, user);

        // then
        assertEquals(postPage, response.getBody());
        verify(feedService).getPostsPageOrderedByFollowerSubscribe(any(), eq(pageable));
    }

    @Test
    void searchPosts_ReturnsPosts() {
        // given
        when(feedService.getPostsPageByName("Test", pageable)).thenReturn(postPage);

        // when
        ResponseEntity<PageDto<PostResponseDto>> response = feedController.searchPosts(0, 10, "Test");

        // then
        assertEquals(postPage, response.getBody());
        verify(feedService).getPostsPageByName("Test", pageable);
    }
}
