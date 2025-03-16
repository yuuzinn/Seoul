package com.example.seoul.tag.service;

import com.example.seoul.domain.type.TagType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    List<String> getTagsByType(TagType type);
}
