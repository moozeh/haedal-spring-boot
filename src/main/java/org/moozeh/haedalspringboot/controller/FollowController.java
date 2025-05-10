package org.moozeh.haedalspringboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moozeh.haedalspringboot.domain.User;
import org.moozeh.haedalspringboot.dto.response.UserSimpleResponseDto;
import org.moozeh.haedalspringboot.service.AuthService;
import org.moozeh.haedalspringboot.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FollowController {
    
    private final AuthService authService;
    private final FollowService followService;
    
    @PostMapping("/follows/{followingId}")
    public ResponseEntity<Void> followUser(@PathVariable Long followingId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        
        followService.followUser(currentUser, followingId);
        
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/follows/{followingId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long followingId, HttpServletRequest request) {
        User currentUser = authService.getCurrentUser(request);
        
        followService.unfollowUser(currentUser, followingId);
        
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/follows/{userId}/following")
    public ResponseEntity<List<UserSimpleResponseDto>> getFollowing(
        @PathVariable Long userId,
        HttpServletRequest request
    ) {
        User currentUser = authService.getCurrentUser(request);
        List<UserSimpleResponseDto> users = followService.getFollowingUsers(currentUser, userId);
        
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/follows/{userId}/follower")
    public ResponseEntity<List<UserSimpleResponseDto>> getFollowers(
        @PathVariable Long userId,
        HttpServletRequest request
    ) {
        User currentUser = authService.getCurrentUser(request);
        List<UserSimpleResponseDto> users = followService.getFollowerUsers(currentUser, userId);
        
        return ResponseEntity.ok(users);
    }
}
