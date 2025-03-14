package com.example.seoul.place.repository;

import com.example.seoul.domain.PostPlace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostPlaceRepository extends JpaRepository<PostPlace, Long> {
}
