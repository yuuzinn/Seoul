package com.example.seoul.likes.service;

import com.example.seoul.domain.Likes;
import com.example.seoul.domain.Post;
import com.example.seoul.domain.User;
import com.example.seoul.exception.CustomException;
import com.example.seoul.exception.ErrorCode;
import com.example.seoul.likes.repository.LikesRepository;
import com.example.seoul.notification.service.NotificationService;
import com.example.seoul.post.repository.PostRepository;
import com.example.seoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikesServiceImpl implements LikesService{
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    @Override
    public void likePost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        if (likesRepository.existsByUserAndPost(user, post)) {
            throw new CustomException(ErrorCode.ALREADY_LIKES);
        }

        likesRepository.save(new Likes(user, post));
        post.increaseLikeCount();

        // 알림 전송
        if (!post.getUser().getId().equals(userId)) {
            notificationService.notifyLike(post.getUser(), user, post);
        }
    }

    @Transactional
    @Override
    public void unlikePost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        Likes like = likesRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_LIKES));

        likesRepository.delete(like);
        post.decreaseLikeCount();
    }
}
