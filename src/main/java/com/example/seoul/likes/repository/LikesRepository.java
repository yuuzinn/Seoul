package com.example.seoul.likes.repository;

import com.example.seoul.domain.Likes;
import com.example.seoul.domain.Post;
import com.example.seoul.domain.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {
    @Modifying
    @Query("DELETE FROM Likes l WHERE l.post = :post")
    void deleteByPost(@Param("post") Post post);

    boolean existsByUserAndPost(User user, Post post);

    Optional<Likes> findByUserAndPost(User user, Post post);
    @Query("""
        SELECT l.post FROM Likes l
        WHERE l.user.id = :userId
        AND (:lastId IS NULL OR l.post.id < :lastId)
        ORDER BY l.post.id DESC
        LIMIT 10
    """)
    List<Post> findLikedPosts(@Param("userId") Long userId, @Param("lastId") Long lastId);
}
