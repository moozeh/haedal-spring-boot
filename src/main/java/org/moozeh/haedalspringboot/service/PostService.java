package org.moozeh.haedalspringboot.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.moozeh.haedalspringboot.domain.Post;
import org.moozeh.haedalspringboot.domain.User;
import org.moozeh.haedalspringboot.dto.response.PostResponseDto;
import org.moozeh.haedalspringboot.dto.response.UserSimpleResponseDto;
import org.moozeh.haedalspringboot.repository.LikeRepository;
import org.moozeh.haedalspringboot.repository.PostRepository;
import org.moozeh.haedalspringboot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final LikeService likeService;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    
    public void savePost(Post post) {
        Post saved = postRepository.save(post);
    }

    public List<PostResponseDto> getPostsByUser(Long targetUserId) {
        User targetUser = userRepository.findById(targetUserId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Post> posts = postRepository.findByUser(targetUser);
        posts.sort((p1, p2) -> p2.getCreatedAt().compareTo(p1.getCreatedAt()));

        return posts.stream().map(post -> convertPostToDto(targetUser, post)).toList();
    }

    public PostResponseDto convertPostToDto(User currentUser, Post post) {
        User author = post.getUser();
        UserSimpleResponseDto userSimpleResponseDto = userService.convertUserToSimpleDto(currentUser, author);
        String imageUrl = post.getImageUrl();
        String imageData = imageService
            .encodeImageToBase64(System.getProperty("user.dir") + "/src/main/resources/static/" + imageUrl);

        return new PostResponseDto(
            post.getId(),
            userSimpleResponseDto,
            imageData,
            post.getContent(),
            likeRepository.countByPost(post),
            likeRepository.existsByUserAndPost(author, post),
            post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm"))
        );
    }
}
