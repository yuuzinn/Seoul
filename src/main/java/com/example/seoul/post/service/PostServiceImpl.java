package com.example.seoul.post.service;

import com.example.seoul.domain.Post;
import com.example.seoul.domain.PostTag;
import com.example.seoul.domain.Tag;
import com.example.seoul.domain.User;
import com.example.seoul.domain.type.TagType;
import com.example.seoul.post.dto.PostRequest;
import com.example.seoul.post.repository.PostRepository;
import com.example.seoul.tag.repository.PostTagRepository;
import com.example.seoul.tag.repository.TagRepository;
import com.example.seoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    @Transactional
    @Override
    public Long createPost(PostRequest request, Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("NOT FOUND USER."));

        Post post = Post.builder()
                .user(user)
                .title(request.getTitle())
                .content(request.getContent())
                .build();
        postRepository.save(post);

        savePostTags(post, request.getSubwayTags(), TagType.SUBWAY);
        savePostTags(post, request.getMoodTags(), TagType.MOOD);

        return post.getId();
    }

    private void savePostTags(Post post, List<String> tagNames, TagType tagType) {
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByNameAndType(tagName, tagType)
                    .orElseGet(() -> tagRepository.save(
                            Tag.builder()
                                    .name(tagName)
                                    .type(tagType)
                                    .build()
                    ));

            PostTag postTag = PostTag.builder()
                    .post(post)
                    .tag(tag)
                    .build();

            postTagRepository.save(postTag);
        }
    }

}
