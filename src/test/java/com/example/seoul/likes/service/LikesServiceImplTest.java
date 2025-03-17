package com.example.seoul.likes.service;

import com.example.seoul.domain.Likes;
import com.example.seoul.domain.Post;
import com.example.seoul.domain.User;
import com.example.seoul.exception.CustomException;
import com.example.seoul.exception.ErrorCode;
import com.example.seoul.likes.repository.LikesRepository;
import com.example.seoul.post.repository.PostRepository;
import com.example.seoul.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikesServiceImplTest {

    @Mock
    private LikesRepository likesRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LikesServiceImpl likesService;

    private User user;
    private Post post;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("testUser")
                .password("password")
                .nickname("tester")
                .email("test@example.com")
                .build();

        post = Post.builder()
                .user(user)
                .title("Test Post")
                .content("This is a test post")
                .build();
    }

    @Test
    @DisplayName("유효한 userId와 postId로 좋아요 추가 시 정상적으로 저장")
    void testLikePost_Success() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(likesRepository.existsByUserAndPost(any(), any())).thenReturn(false);

        likesService.likePost(1L, 1L);

        verify(likesRepository, times(1)).save(any(Likes.class));
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("이미 좋아요를 누른 경우 예외가 발생")
    void testLikePost_AlreadyLiked_ThrowsException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(likesRepository.existsByUserAndPost(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> likesService.likePost(1L, 1L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.ALREADY_LIKES.getMessage());

        verify(likesRepository, never()).save(any(Likes.class));
    }

    @Test
    @DisplayName("좋아요 취소가 정상적으로 저장")
    void testUnlikePost_Success() {
        Likes like = new Likes(user, post);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(likesRepository.findByUserAndPost(any(), any())).thenReturn(Optional.of(like));

        likesService.unlikePost(1L, 1L);

        verify(likesRepository, times(1)).delete(any(Likes.class));
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("좋아요를 누른 적 없는 경우 예외가 발생")
    void testUnlikePost_NotLiked_ThrowsException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(postRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(likesRepository.findByUserAndPost(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> likesService.unlikePost(1L, 1L))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorCode.NOT_FOUND_LIKES.getMessage());

        verify(likesRepository, never()).delete(any(Likes.class));
    }
}
