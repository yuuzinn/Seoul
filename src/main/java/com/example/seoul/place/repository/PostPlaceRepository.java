package com.example.seoul.place.repository;

import com.example.seoul.domain.Post;
import com.example.seoul.domain.PostPlace;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostPlaceRepository extends JpaRepository<PostPlace, Long> {

    List<PostPlace> findByPostOrderByPlaceOrderAsc(Post post);

    @Query("SELECT p.imageUrl FROM PostPlace p WHERE p.post = :post ORDER BY p.placeOrder ASC LIMIT 1")
    String findFirstImageUrlByPost(@Param("post") Post post);

    @Modifying
    @Query("DELETE FROM PostPlace pp WHERE pp.post = :post")
    void deleteByPost(@Param("post") Post post);
}
