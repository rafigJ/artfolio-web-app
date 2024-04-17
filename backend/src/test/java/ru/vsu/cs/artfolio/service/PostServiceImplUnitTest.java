package ru.vsu.cs.artfolio.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.vsu.cs.artfolio.repository.PostRepository;
import ru.vsu.cs.artfolio.repository.UserRepository;
import ru.vsu.cs.artfolio.service.impl.PostServiceImpl;


@ExtendWith(MockitoExtension.class)
public class PostServiceImplUnitTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostServiceImplUnitTest.class);

    @Mock
    PostRepository postRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    MediaService mediaService;

    @Spy
    ModelMapper modelMapper;

    @InjectMocks
    PostServiceImpl service;


//    @Test
//    void getPostById_ValidId_ReturnFullPostResponse() {
//        // given
//        UUID uuid = UUID.randomUUID();
//        String name = "name";
//        String mail = "email@email.com";
//        String password = "password";
//        UserEntity ownerEntity = new UserEntity(uuid, name, mail, password, Role.USER, LocalDateTime.MIN, LocalDateTime.MIN);
//
//        String postName = "post_name";
//        String description = "description";
//        PostEntity postEntity = new PostEntity(1L, postName, description, ownerEntity, LocalDateTime.now());
//        Optional<PostEntity> optionalPostEntity = Optional.of(postEntity);
//        doReturn(optionalPostEntity).when(postRepository).findById(1L);
//
//        // when
//        FullPostResponseDto responseDto = service.getPostById(1L);
//
//        // assert
//        assertNotNull(responseDto);
//        assertEquals(responseDto.id, 1L);
//        assertEquals(responseDto.name, postName);
//        assertEquals(responseDto.description, description);
//
//        UserResponseDto owner = responseDto.owner;
//        assertNotNull(owner);
//        assertEquals(owner.uuid, uuid);
//        assertEquals(owner.fullName, name);
//        assertEquals(owner.email, mail);
//    }
}
