package com.example.seoul.post.repository;

import com.example.seoul.domain.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p WHERE p.id < :lastPostId AND EXISTS (SELECT pt FROM PostTag pt WHERE pt.post = p AND pt.tag.type = 'SUBWAY' AND pt.tag.name = :subwayTag) ORDER BY p.id DESC")
    List<Post> findPostsBySubwayTagAndPostIdLessThanOrderByPostIdDesc(@Param("subwayTag") String subwayTag, @Param("lastPostId") Long lastPostId, Pageable pageable);

    @Query("""
        SELECT p FROM Post p
        WHERE p.user.id = :userId
        AND (:lastId IS NULL OR p.id < :lastId)
        ORDER BY p.id DESC
        LIMIT 10
    """)
    List<Post> findMyPosts(@Param("userId") Long userId, @Param("lastId") Long lastId);

    @Query("""
        SELECT DISTINCT p FROM Post p
        JOIN PostTag pt1 ON pt1.post = p
        WHERE (:lastPostId IS NULL OR p.id < :lastPostId)
        AND pt1.tag.name = :subwayTag
        AND NOT EXISTS (
            SELECT 1 FROM PostTag pt2
            WHERE pt2.post = p
            AND pt2.tag.type = 'MOOD'
            AND (:tags IS NOT EMPTY AND pt2.tag.name NOT IN (:tags))
        )
        ORDER BY p.likeCount DESC
    """)
    List<Post> findPostsBySubwayTagAndTagsOrderByLikesDesc(String subwayTag, List<String> tags, Long lastPostId, Pageable pageable);

    @Query("""
        SELECT DISTINCT p FROM Post p
        JOIN PostTag pt1 ON pt1.post = p
        WHERE (:lastPostId IS NULL OR p.id < :lastPostId)
        AND pt1.tag.name = :subwayTag
        AND NOT EXISTS (
            SELECT 1 FROM PostTag pt2
            WHERE pt2.post = p
            AND pt2.tag.type = 'MOOD'
            AND (:tags IS NOT EMPTY AND pt2.tag.name NOT IN (:tags))
        )
        ORDER BY p.id DESC
    """)
    List<Post> findPostsBySubwayTagAndTagsOrderByIdDesc(String subwayTag, List<String> tags, Long lastPostId, Pageable pageable);
}
