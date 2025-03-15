package com.example.seoul.subway.repository;

import com.example.seoul.domain.SubwayStation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubwayStationRepository extends JpaRepository<SubwayStation, Long> {
    boolean existsByStationName(String name);
}
