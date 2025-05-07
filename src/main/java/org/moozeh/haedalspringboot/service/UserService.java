package org.moozeh.haedalspringboot.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.moozeh.haedalspringboot.domain.User;
import org.moozeh.haedalspringboot.dto.request.UserUpdateRequestDto;
import org.moozeh.haedalspringboot.dto.response.UserDetailResponseDto;
import org.moozeh.haedalspringboot.dto.response.UserSimpleResponseDto;
import org.moozeh.haedalspringboot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;

    public UserSimpleResponseDto saveUser(User newUser) {
        // 중복 회원 검증
        if (userRepository.existsByUsername(newUser.getUsername())) {
            throw new IllegalStateException("중복되는 username 입니다.");
        }

        userRepository.save(newUser);
        return convertUserToSimpleDto(newUser, newUser);
    }

    public UserSimpleResponseDto convertUserToSimpleDto(User currentUser, User targetUser) {
        String imageUrl = targetUser.getImageUrl();
        String imageData = imageService
            .encodeImageToBase64(System.getProperty("user.dir") + "/src/main/resources/static/" + imageUrl);
        return new UserSimpleResponseDto(
            targetUser.getId(),
            targetUser.getUsername(),
            targetUser.getName(),
            imageData,
            false
        );
    }

    public UserDetailResponseDto convertUserToDetailDto(User currentUser, User targetUser) {
        String imageUrl = targetUser.getImageUrl();
        String imageData = imageService
            .encodeImageToBase64(System.getProperty("user.dir") + "/src/main/resources/static/" + imageUrl);
        return new UserDetailResponseDto(
            targetUser.getId(),
            targetUser.getUsername(),
            targetUser.getName(),
            imageData,
            false,
            targetUser.getBio(),
            targetUser.getJoinedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm")),
            0L,
            0L,
            0L
        );
    }

    public List<UserSimpleResponseDto> getAllUsers(User currentUser) {
        List<User> users = userRepository.findAll();
        users.remove(currentUser);
        return users.stream().map(user -> convertUserToSimpleDto(user, user)).collect(Collectors.toList());
    }

    public List<UserSimpleResponseDto> getUserByUsername(User currentUser, String username) {
        List<UserSimpleResponseDto> users = new ArrayList<>();
        User targetUser = userRepository.findByUsername(username).orElse(null);

        if (targetUser != null) {
            users.add(convertUserToSimpleDto(currentUser, targetUser));
        }

        return users;
    }

    public UserDetailResponseDto updateUser(User currentUser, UserUpdateRequestDto userUpdateRequestDto) {
        if (userUpdateRequestDto.getUsername() != null) {
            currentUser.setUsername(userUpdateRequestDto.getUsername());
        }
        if (userUpdateRequestDto.getPassword() != null) {
            currentUser.setPassword(userUpdateRequestDto.getPassword());
        }
        if (userUpdateRequestDto.getName() != null) {
            currentUser.setName(userUpdateRequestDto.getName());
        }
        if (userUpdateRequestDto.getBio() != null) {
            currentUser.setBio(userUpdateRequestDto.getBio());
        }

        return convertUserToDetailDto(currentUser, currentUser);
    }

    public UserDetailResponseDto getUserDetail(User currentUser, Long targetUserId) {
        User targetUser = userRepository.findById(targetUserId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
        return convertUserToDetailDto(currentUser, targetUser);
    }
}
