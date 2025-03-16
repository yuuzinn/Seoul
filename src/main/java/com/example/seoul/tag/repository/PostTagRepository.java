package com.example.seoul.tag.repository;

import com.example.seoul.domain.Post;
import com.example.seoul.domain.PostPlace;
import com.example.seoul.domain.PostTag;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    @Query("SELECT t.name FROM PostTag pt JOIN pt.tag t WHERE pt.post.id = :postId")
    List<String> findTagNamesByPostId(@Param("postId") Long postId);
    List<PostTag> findAllByPost(Post post);

    @Query("SELECT t.name FROM PostTag pt JOIN pt.tag t WHERE pt.post.id = :postId AND t.type = 'SUBWAY'")
    String findSubwayTagByPostId(@Param("postId") Long postId);


    @Modifying
    @Query("DELETE FROM PostTag pt WHERE pt.post = :post")
    void deleteByPost(@Param("post") Post post);
}
