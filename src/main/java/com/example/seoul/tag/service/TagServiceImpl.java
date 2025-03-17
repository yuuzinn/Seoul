package com.example.seoul.tag.service;

import com.example.seoul.domain.type.TagType;
import com.example.seoul.tag.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.seoul.domain.Tag;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService{
    private final TagRepository tagRepository;
    @Override
    public List<String> getTagsByType(TagType type) {
        return tagRepository.findByType(type)
                .stream()
                .map(Tag::getName)
                .toList();
    }
}
