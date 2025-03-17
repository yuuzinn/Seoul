package com.example.seoul.post.service;

import com.example.seoul.domain.*;
import com.example.seoul.domain.type.TagType;
import com.example.seoul.exception.CustomException;
import com.example.seoul.exception.ErrorCode;
import com.example.seoul.likes.repository.LikesRepository;
import com.example.seoul.place.repository.PostPlaceRepository;
import com.example.seoul.post.dto.PostDetailResponse;
import com.example.seoul.post.dto.PostListResponse;
import com.example.seoul.post.dto.PostRequest;
import com.example.seoul.post.repository.PostRepository;
import com.example.seoul.subway.repository.SubwayStationRepository;
import com.example.seoul.tag.repository.PostTagRepository;
import com.example.seoul.tag.repository.TagRepository;
import com.example.seoul.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private PostTagRepository postTagRepository;
    @Mock
    private SubwayStationRepository subwayStationRepository;
    @Mock
    private PostPlaceRepository postPlaceRepository;
    @Mock
    private LikesRepository likesRepository;

    private User user;
    private Post post;
    private PostRequest postRequest;
    private PostPlace postPlace;
    private Tag moodTag;
    private Tag subwayTag;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User("testUser", "password", "nickname", "test@example.com", "profile.jpg", false);
        ReflectionTestUtils.setField(user, "id", 1L);

        post = new Post(user, "Test Title", "Test Content");
        ReflectionTestUtils.setField(post, "id", 1L);

        postRequest = new PostRequest("Updated Title", "Updated Content", "홍대입구역", List.of("데이트", "야경"), List.of());

        postPlace = new PostPlace(post, "Place Name", 37.5665, 126.9780, "Description", "imageUrl.jpg", 1);
        ReflectionTestUtils.setField(postPlace, "id", 1L);

        moodTag = new Tag("데이트", TagType.MOOD);
        subwayTag = new Tag("홍대입구역", TagType.SUBWAY);
    }

    @DisplayName("게시글 생성 성공 테스트")
    @Test
    void testCreatePost_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(subwayStationRepository.existsByStationName(anyString())).thenReturn(true);
        when(tagRepository.findByNameAndType(anyString(), eq(TagType.SUBWAY))).thenReturn(Optional.empty());
        when(tagRepository.save(any(Tag.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post savedPost = invocation.getArgument(0);
            ReflectionTestUtils.setField(savedPost, "id", 1L);
            return savedPost;
        });

        Long postId = postService.createPost(postRequest, 1L);

        assertNotNull(postId);
        verify(postRepository, times(1)).save(any(Post.class));
        verify(postTagRepository, times(1)).save(any(PostTag.class));
        verify(postPlaceRepository, times(1)).saveAll(anyList());
    }

    @DisplayName("게시글 수정 성공 테스트")
    @Test
    void testUpdatePost_Success() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(tagRepository.findByNameAndType(anyString(), eq(TagType.MOOD))).thenReturn(Optional.of(moodTag));
        when(postTagRepository.findAllByPost(any(Post.class))).thenReturn(List.of());
        when(postPlaceRepository.findByPostOrderByPlaceOrderAsc(any(Post.class))).thenReturn(List.of());

        postService.updatePost(1L, 1L, postRequest);

        assertEquals(postRequest.getTitle(), post.getTitle());
        assertEquals(postRequest.getContent(), post.getContent());

        verify(postTagRepository, times(1)).deleteAll(anyList());
        verify(postTagRepository, times(1)).saveAll(anyList());
        verify(postPlaceRepository, times(1)).saveAll(anyList());
    }

    @DisplayName("게시글 상세 조회 성공 테스트")
    @Test
    void testGetPostDetail_Success() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(postTagRepository.findSubwayTagByPostId(anyLong())).thenReturn("홍대입구역");
        when(postTagRepository.findTagNamesByPostId(anyLong())).thenReturn(List.of("데이트", "야경"));
        when(postPlaceRepository.findByPostOrderByPlaceOrderAsc(any(Post.class))).thenReturn(List.of(postPlace));

        PostDetailResponse response = postService.getPostDetail(1L);

        assertNotNull(response);
        assertEquals(post.getTitle(), response.getTitle());
        assertEquals("홍대입구역", response.getSubwayTag());
        assertTrue(response.getMoodTags().contains("데이트"));

        verify(postRepository, times(1)).findById(anyLong());
    }

    @DisplayName("게시글 목록 조회 테스트")
    @Test
    void testGetPosts_Success() {
        when(postRepository.findPostsBySubwayTagAndPostIdLessThanOrderByPostIdDesc(anyString(), anyLong(), any()))
                .thenReturn(List.of(post));
        when(postTagRepository.findSubwayTagByPostId(anyLong())).thenReturn("홍대입구역");
        when(postPlaceRepository.findFirstImageUrlByPost(any(Post.class))).thenReturn("test_image.jpg");
        when(postTagRepository.findTagNamesByPostId(anyLong())).thenReturn(List.of("데이트", "야경"));

        PostListResponse response = postService.getPosts("홍대입구역", 10L, 5);

        assertNotNull(response);
        assertEquals(1, response.getPosts().size());
        assertEquals("홍대입구역", response.getPosts().get(0).getSubwayTag());
        assertEquals("test_image.jpg", response.getPosts().get(0).getThumbnailImageUrl());

        verify(postRepository, times(1)).findPostsBySubwayTagAndPostIdLessThanOrderByPostIdDesc(anyString(), anyLong(), any());
    }

    @DisplayName("게시글 삭제 성공 테스트")
    @Test
    void testDeletePost_Success() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        postService.deletePost(1L, 1L);

        verify(postTagRepository, times(1)).deleteByPost(any(Post.class));
        verify(postPlaceRepository, times(1)).deleteByPost(any(Post.class));
        verify(likesRepository, times(1)).deleteByPost(any(Post.class));
        verify(postRepository, times(1)).delete(any(Post.class));
    }

    @DisplayName("게시글 생성 시 유효하지 않은 유저일 경우 예외 발생")
    @Test
    void testCreatePost_InvalidUser_ThrowsException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> postService.createPost(postRequest, 1L));

        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
        verify(postRepository, never()).save(any());
    }

    @DisplayName("게시글 생성 시 지하철 태그가 존재하지 않을 경우 예외 발생")
    @Test
    void testCreatePost_InvalidSubwayTag_ThrowsException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(subwayStationRepository.existsByStationName(anyString())).thenReturn(false);

        CustomException exception = assertThrows(CustomException.class,
                () -> postService.createPost(postRequest, 1L));

        assertEquals(ErrorCode.NOT_FOUND_SUBWAY, exception.getErrorCode());
        verify(postRepository, never()).save(any());
    }

    @DisplayName("게시글 수정 시 작성자가 아닌 유저가 수정 요청할 경우 예외 발생")
    @Test
    void testUpdatePost_NotAuthor_ThrowsException() {
        User anotherUser = new User("otherUser", "password", "nickname2", "other@example.com", "profile2.jpg", false);
        ReflectionTestUtils.setField(anotherUser, "id", 2L);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        CustomException exception = assertThrows(CustomException.class,
                () -> postService.updatePost(1L, 2L, postRequest));

        assertEquals(ErrorCode.FORBIDDEN_ACCESS, exception.getErrorCode());
        verify(postRepository, never()).save(any());
    }

    @DisplayName("게시글 상세 조회 시 존재하지 않는 게시글일 경우 예외 발생")
    @Test
    void testGetPostDetail_NotFound_ThrowsException() {
        when(postRepository.findById(anyLong())).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class,
                () -> postService.getPostDetail(1L));

        assertEquals(ErrorCode.NOT_FOUND_POST, exception.getErrorCode());
    }

    @DisplayName("게시글 삭제 시 작성자가 아닌 경우 예외 발생")
    @Test
    void testDeletePost_NotAuthor_ThrowsException() {
        User anotherUser = new User("otherUser", "password", "nickname2", "other@example.com", "profile2.jpg", false);
        ReflectionTestUtils.setField(anotherUser, "id", 2L);

        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));

        CustomException exception = assertThrows(CustomException.class,
                () -> postService.deletePost(1L, 2L));

        assertEquals(ErrorCode.FORBIDDEN_ACCESS, exception.getErrorCode());
        verify(postRepository, never()).delete(any());
    }

}
