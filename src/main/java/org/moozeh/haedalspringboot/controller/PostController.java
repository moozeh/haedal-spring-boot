package org.moozeh.haedalspringboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moozeh.haedalspringboot.domain.Post;
import org.moozeh.haedalspringboot.domain.User;
import org.moozeh.haedalspringboot.dto.response.PostResponseDto;
import org.moozeh.haedalspringboot.dto.response.UserSimpleResponseDto;
import org.moozeh.haedalspringboot.service.AuthService;
import org.moozeh.haedalspringboot.service.ImageService;
import org.moozeh.haedalspringboot.service.LikeService;
import org.moozeh.haedalspringboot.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class PostController {
    
    private final AuthService authService;
    private final ImageService imageService;
    private final LikeService likeService;
    private final PostService postService;
    
    @PostMapping("/posts")
    public ResponseEntity<Void> createPost(
        @RequestParam("image") MultipartFile image,
        @RequestParam("content") String content,
        HttpServletRequest request
    ) throws IOException {
        User currentUser = authService.getCurrentUser(request);
        String imageUrl = imageService.savePostImage(image);
        
        Post post = new Post(currentUser, content, imageUrl);
        postService.savePost(post);
        
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/posts/user/{userId}")
    public ResponseEntity<List<PostResponseDto>> getPostsByUser(@PathVariable Long userId) {
        List<PostResponseDto> posts = postService.getPostsByUser(userId);
        return ResponseEntity.ok(posts);
    }
    
    @PostMapping("/posts/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        likeService.likePost(currentUser, postId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/posts/{postId}/like")
    public ResponseEntity<Void> unlikePost(@PathVariable Long postId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        likeService.unlikePost(currentUser, postId);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/posts/{postId}/like")
    public ResponseEntity<List<UserSimpleResponseDto>> getUsersWhoLikedPost(
        @PathVariable Long postId,
        HttpServletRequest request
    ) {
        User currentUser = authService.getCurrentUser(request);
        List<UserSimpleResponseDto> usersWhoLikedPost = likeService.getUsersWhoLikedPost(currentUser, postId);
        return ResponseEntity.ok(usersWhoLikedPost);
    }
}
