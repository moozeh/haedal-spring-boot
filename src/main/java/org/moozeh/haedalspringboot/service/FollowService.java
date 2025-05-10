package org.moozeh.haedalspringboot.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moozeh.haedalspringboot.domain.Follow;
import org.moozeh.haedalspringboot.domain.User;
import org.moozeh.haedalspringboot.dto.response.UserSimpleResponseDto;
import org.moozeh.haedalspringboot.repository.FollowRepository;
import org.moozeh.haedalspringboot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FollowService {
    
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final UserService userService;
    
    public void followUser(User currentUser, Long followingId) {
        User targetUser = userRepository.findById(followingId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        
        if (followRepository.existsByFollowerAndFollowing(currentUser, targetUser)) {
            throw new IllegalStateException("이미 팔로우한 회원입니다.");
        }
        
        if (targetUser.getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }
        
        Follow follow = new Follow(currentUser, targetUser);
        
        followRepository.save(follow);
    }
    
    public void unfollowUser(User currentUser, Long followingId) {
        User targetUser = userRepository.findById(followingId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        
        if (targetUser.getId().equals(currentUser.getId())) {
            throw new IllegalArgumentException("자기 자신을 언팔로우할 수 없습니다.");
        }
        
        Follow follow = followRepository.findByFollowerAndFollowing(currentUser, targetUser)
            .orElseThrow(() -> new IllegalStateException("이미 팔로우하지 않은 회원입니다."));
        
        followRepository.delete(follow);
    }
    
    public List<UserSimpleResponseDto> getFollowingUsers(User currentUser, Long userId) {
        User targetUser = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        
        List<Follow> followings = followRepository.findByFollower(targetUser);
        
        return followings.stream()
            .map((follow -> userService.convertUserToSimpleDto(currentUser, follow.getFollowing())))
            .toList();
    }
    
    public List<UserSimpleResponseDto> getFollowerUsers(User currentUser, Long userId) {
        User targetUser = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        
        List<Follow> followers = followRepository.findByFollowing(targetUser);
        
        return followers.stream()
            .map((follow -> userService.convertUserToSimpleDto(currentUser, follow.getFollower())))
            .toList();
    }
}
