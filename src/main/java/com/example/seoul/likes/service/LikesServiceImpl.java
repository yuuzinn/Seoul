package com.example.seoul.likes.service;

import com.example.seoul.domain.Likes;
import com.example.seoul.domain.Post;
import com.example.seoul.domain.User;
import com.example.seoul.likes.repository.LikesRepository;
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

    @Transactional
    public void likePost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("NOT FOUND USER"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("NOT FOUND POST"));

        if (likesRepository.existsByUserAndPost(user, post)) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }

        likesRepository.save(new Likes(user, post));
        post.increaseLikeCount();
    }

    @Transactional
    public void unlikePost(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("NOT FOUND USER"));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("NOT FOUND POST"));

        Likes like = likesRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new IllegalArgumentException("좋아요한 기록이 없습니다."));

        likesRepository.delete(like);
        post.decreaseLikeCount();
    }
}
