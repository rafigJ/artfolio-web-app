package ru.vsu.cs.artfolio.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.cs.artfolio.auth.user.Role;
import ru.vsu.cs.artfolio.data.TestDataConstants;
import ru.vsu.cs.artfolio.dto.PageDto;
import ru.vsu.cs.artfolio.dto.comment.CommentRequestDto;
import ru.vsu.cs.artfolio.dto.comment.CommentResponseDto;
import ru.vsu.cs.artfolio.entity.UserEntity;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@Sql(value = "/sql/before_all/test_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "/sql/after_all/test_data_clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Disabled("not implemented")
public class CommentServiceIT {

    @Autowired
    CommentService commentService;

    @Test
    void getAllByPostId_ValidId_ReturnCommentPage() throws Exception {
        // given
        Long postId = TestDataConstants.POST_1_ID;
        Pageable pageable = PageRequest.of(0, 10);

        // when
        PageDto<CommentResponseDto> commentsPage = commentService.getAllByPostId(postId, pageable);

        // then
        assertNotNull(commentsPage);
        assertFalse(commentsPage.content().isEmpty());
    }

    @Test
    void createComment_ValidPostId_ValidRequest_ReturnCreatedComment() throws Exception {
        // given
        Long postId = TestDataConstants.POST_1_ID;
        UserEntity user = new UserEntity();
        user.setUuid(TestDataConstants.USER_UUID);
        user.setRole(Role.USER);

        CommentRequestDto requestDto = new CommentRequestDto("This is a test comment");

        // when
        CommentResponseDto createdComment = commentService.createComment(user, postId, requestDto);

        // then
        assertNotNull(createdComment);
        assertEquals("This is a test comment", createdComment.getComment());
    }

    @Test
    void deleteComment_ValidPostId_ValidCommentId() throws Exception {
        // given
        Long postId = TestDataConstants.POST_1_ID;
        UserEntity user = new UserEntity();
        user.setUuid(TestDataConstants.USER_UUID);
        user.setRole(Role.USER);

        // Assuming there's a comment with ID 1 for the post with ID 1
        Long commentId = 1L;

        // when & then
        assertDoesNotThrow(() -> commentService.deleteCommentById(user, postId, commentId));
    }
}
