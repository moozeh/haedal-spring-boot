package org.moozeh.haedalspringboot.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moozeh.haedalspringboot.domain.Like;
import org.moozeh.haedalspringboot.domain.Post;
import org.moozeh.haedalspringboot.domain.User;
import org.moozeh.haedalspringboot.dto.response.UserSimpleResponseDto;
import org.moozeh.haedalspringboot.repository.LikeRepository;
import org.moozeh.haedalspringboot.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {
    
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserService userService;
    
    public void likePost(User currentUser, Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        
        if (likeRepository.existsByUserAndPost(currentUser, post)) {
            throw new IllegalStateException("이미 좋아요를 누른 게시물입니다.");
        }
        
        Like like = new Like(currentUser, post);
        
        likeRepository.save(like);
    }
    
    public void unlikePost(User currentUser, Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        
        Like like = likeRepository.findByUserAndPost(currentUser, post)
            .orElseThrow(() -> new IllegalStateException("좋아요를 누르지 않은 게시물입니다."));
        
        likeRepository.delete(like);
    }
    
    public List<UserSimpleResponseDto> getUsersWhoLikedPost(User currentUser, Long postId) {
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다"));
        
        List<Like> likes = likeRepository.findByPost(post);
        
        return likes.stream()
            .map(like -> userService.convertUserToSimpleDto(currentUser, like.getUser()))
            .toList();
    }
}
