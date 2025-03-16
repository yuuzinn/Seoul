package com.example.seoul.tag.repository;

import com.example.seoul.domain.Tag;
import com.example.seoul.domain.type.TagType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByNameAndType(String tagName, TagType tagType);
    List<Tag> findByType(TagType type);
}
