package com.example.seoul.place.service;

import com.example.seoul.client.TmapPoiSearchClient;
import com.example.seoul.domain.Post;
import com.example.seoul.domain.PostPlace;
import com.example.seoul.place.dto.PoiSearchResponseDto;
import com.example.seoul.place.dto.PostPlaceRequest;
import com.example.seoul.place.repository.PostPlaceRepository;
import com.example.seoul.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostPlaceServiceImpl implements PostPlaceService{
    private final TmapPoiSearchClient tmapPoiSearchClient;
    private final PostRepository postRepository;
    private final PostPlaceRepository postPlaceRepository;

    @Override
    public PoiSearchResponseDto searchPoi(String keyword, int count) {
        return tmapPoiSearchClient.searchPoi(keyword, count);
    }

//    @Override
//    @Transactional
//    public void addPostPlace(PostPlaceRequest request) {
//        Post post = postRepository.findById(request.getPostId())
//                .orElseThrow(() -> new IllegalArgumentException("NOT FOUND POST."));
//
//        PostPlace postPlace = PostPlace.builder()
//                .post(post)
//                .placeName(request.getPlaceName())
//                .latitude(request.getLatitude())
//                .longitude(request.getLongitude())
//                .description(request.getDescription())
//                .imageUrl(request.getImageUrl())
//                .placeOrder(request.getPlaceOrder())
//                .build();
//
//        postPlaceRepository.save(postPlace);
//    }
}
