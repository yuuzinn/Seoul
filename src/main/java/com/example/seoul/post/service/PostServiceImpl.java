package com.example.seoul.post.service;

import com.example.seoul.domain.*;
import com.example.seoul.domain.type.TagType;
import com.example.seoul.exception.CustomException;
import com.example.seoul.exception.ErrorCode;
import com.example.seoul.likes.repository.LikesRepository;
import com.example.seoul.place.dto.PostPlaceRequest;
import com.example.seoul.place.repository.PostPlaceRepository;
import com.example.seoul.post.dto.*;
import com.example.seoul.post.repository.PostRepository;
import com.example.seoul.subway.repository.SubwayStationRepository;
import com.example.seoul.tag.repository.PostTagRepository;
import com.example.seoul.tag.repository.TagRepository;
import com.example.seoul.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostTagRepository postTagRepository;
    private final SubwayStationRepository subwayStationRepository;
    private final PostPlaceRepository postPlaceRepository;
    private final LikesRepository likesRepository;

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

        // Subway 태그 검증 및 저장
        validateSubwayTag(request.getSubwayTag());
        savePostTag(post, request.getSubwayTag(), TagType.SUBWAY);

        // Mood 태그 저장
        savePostTags(post, request.getMoodTags(), TagType.MOOD);

        // 장소 저장 추가
        savePostPlaces(post, request.getPostPlaces());
        return post.getId();
    }

    @Override
    @Transactional
    public void updatePost(Long postId, Long userId, PostRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 글이 없습니다."));

        // 작성자 검증
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }

        // Post 기본 정보 수정
        post.update(request.getTitle(), request.getContent());

        // 무드 태그 업데이트
        List<PostTag> existingTags = postTagRepository.findAllByPost(post);
        List<String> newTagNames = request.getMoodTags();

        List<PostTag> newTags = newTagNames.stream()
                .map(tagName -> {
                    Tag tag = tagRepository.findByNameAndType(tagName, TagType.MOOD)
                            .orElseGet(() -> tagRepository.save(new Tag(tagName, TagType.MOOD)));

                    return new PostTag(post, tag);
                })
                .toList();

        // 기존 태그 중 새로운 태그 리스트에 없는 태그만 삭제
        List<PostTag> tagsToRemove = existingTags.stream()
                .filter(tag -> !newTagNames.contains(tag.getTag().getName()))
                .toList();
        postTagRepository.deleteAll(tagsToRemove);

        postTagRepository.saveAll(newTags);

        //  장소 업데이트 (기존 장소 유지, 순서 변경 반영)
        List<PostPlace> existingPlaces = postPlaceRepository.findByPostOrderByPlaceOrderAsc(post);
        List<PostPlaceRequest> placeRequests = request.getPostPlaces();

        List<PostPlace> updatedPlaces = placeRequests.stream()
                .map(pr -> {
                    // 기존 장소 찾기 (이름 + 위도 + 경도 기준)
                    PostPlace place = existingPlaces.stream()
                            .filter(p -> p.getPlaceName().equals(pr.getPlaceName())
                                    && p.getLatitude() == pr.getLatitude()
                                    && p.getLongitude() == pr.getLongitude())
                            .findFirst()
                            .orElse(null);

                    if (place != null) { // 기존 장소면 업데이트
                        place.update(pr.getPlaceName(), pr.getLatitude(), pr.getLongitude(), pr.getDescription(), pr.getImageUrl(), pr.getPlaceOrder());
                        return place;
                    } else { // 새 장소면 추가
                        return new PostPlace(post, pr.getPlaceName(), pr.getLatitude(), pr.getLongitude(), pr.getDescription(), pr.getImageUrl(), pr.getPlaceOrder());
                    }
                })
                .toList();

        postPlaceRepository.saveAll(updatedPlaces);
    }



    @Override
    @Transactional(readOnly = true)
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        // 지하철 태그 가져오기
        String subwayTag = postTagRepository.findSubwayTagByPostId(post.getId());

        // 분위기 태그 가져오기
        List<String> moodTags = postTagRepository.findTagNamesByPostId(post.getId());
        moodTags.remove(subwayTag);
        // 장소 목록 가져오기
        List<PostPlace> places = postPlaceRepository.findByPostOrderByPlaceOrderAsc(post);
        List<PostPlaceResponse> placeResponses = places.stream()
                .map(PostPlaceResponse::new)
                .toList();

        return new PostDetailResponse(post, subwayTag, moodTags, placeResponses);
    }


    @Override
    @Transactional(readOnly = true)
    public PostListResponse getPosts(String subwayTag, Long lastPostId, int size) {
        List<Post> posts = postRepository.findPostsBySubwayTagAndPostIdLessThanOrderByPostIdDesc(
                subwayTag, lastPostId, PageRequest.of(0, size)
        );

        List<PostSummaryResponse> postSummaries = posts.stream()
                .map(post -> {
                    String subwayTagFromDb = postTagRepository.findSubwayTagByPostId(post.getId()); // 지하철 태그 가져오기
                    String thumbnail = postPlaceRepository.findFirstImageUrlByPost(post); // 첫 번째 이미지 URL 가져오기
                    List<String> moodTags = postTagRepository.findTagNamesByPostId(post.getId()); // 분위기 태그 가져오기

                    return new PostSummaryResponse(post, subwayTagFromDb, thumbnail, moodTags);
                })
                .toList();

        boolean hasNext = posts.size() == size;

        return new PostListResponse(postSummaries, hasNext);
    }

    @Override
    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_POST));

        if (!post.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_ACCESS);
        }

        postTagRepository.deleteByPost(post);
        postPlaceRepository.deleteByPost(post);
        likesRepository.deleteByPost(post);

        postRepository.delete(post);
    }

    @Override
    public List<PostResponse> getMyPosts(Long userId, Long lastId) {
        List<Post> posts = postRepository.findMyPosts(userId, lastId);
        return posts.stream().map(PostResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<PostResponse> getLikedPosts(Long userId, Long lastId) {
        List<Post> posts = likesRepository.findLikedPosts(userId, lastId);
        return posts.stream().map(PostResponse::fromEntity).collect(Collectors.toList());
    }

    @Override
    public PostListResponse searchPosts(String subwayTag, List<String> tags, String sort, Long lastPostId, int size) {
        Pageable pageable = PageRequest.of(0, size);

        List<Post> posts;

        boolean hasMoodTags = tags != null && !tags.isEmpty();

        if ("likes".equalsIgnoreCase(sort)) {
            posts = hasMoodTags
                    ? postRepository.findPostsBySubwayTagAndMoodTagsOrderByLikesDesc(subwayTag, tags, lastPostId, pageable)
                    : postRepository.findPostsBySubwayTagOnlyOrderByLikesDesc(subwayTag, lastPostId, pageable);
        } else {
            posts = hasMoodTags
                    ? postRepository.findPostsBySubwayTagAndMoodTagsOrderByIdDesc(subwayTag, tags, lastPostId, pageable)
                    : postRepository.findPostsBySubwayTagOnlyOrderByIdDesc(subwayTag, lastPostId, pageable);
        }

        List<PostSummaryResponse> postSummaries = posts.stream()
                .map(post -> {
                    String subwayTagFromDb = postTagRepository.findSubwayTagByPostId(post.getId());
                    String thumbnail = postPlaceRepository.findFirstImageUrlByPost(post);
                    List<String> moodTags = postTagRepository.findTagNamesByPostId(post.getId());
                    return new PostSummaryResponse(post, subwayTagFromDb, thumbnail, moodTags);
                })
                .toList();

        boolean hasNext = posts.size() == size;
        return new PostListResponse(postSummaries, hasNext);
    }



    private void validateSubwayTag(String subwayTag) {
        if (!subwayStationRepository.existsByStationName(subwayTag)) {
            throw new CustomException(ErrorCode.NOT_FOUND_SUBWAY);
        }
    }

    private void savePostTag(Post post, String tagName, TagType tagType) {

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

    private void savePostTags(Post post, List<String> tagNames, TagType tagType) {
        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }

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

    private void savePostPlaces(Post post, List<PostPlaceRequest> placeRequests) {
        if (placeRequests == null || placeRequests.isEmpty()) {
            throw new CustomException(ErrorCode.SHOULD_2_OR_MORE_PLACES);
        }

        List<PostPlace> postPlaces = placeRequests.stream()
                .map(request -> PostPlace.builder()
                        .post(post)
                        .placeName(request.getPlaceName())
                        .latitude(request.getLatitude())
                        .longitude(request.getLongitude())
                        .description(request.getDescription())
                        .imageUrl(request.getImageUrl())
                        .placeOrder(request.getPlaceOrder())
                        .build())
                .toList();

        postPlaceRepository.saveAll(postPlaces);
    }
}
