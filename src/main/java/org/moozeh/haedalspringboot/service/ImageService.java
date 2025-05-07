package org.moozeh.haedalspringboot.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.moozeh.haedalspringboot.domain.User;
import org.moozeh.haedalspringboot.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final UserRepository userRepository;
    private final Path uploadDir = Paths.get(System.getProperty("user.dir"), "src/main/resources/static");

    public String updateUserImage(User user, MultipartFile image) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        String uniqueImageName = timestamp + "_" + image.getOriginalFilename();
        Path filePath = uploadDir.resolve(uniqueImageName);
        image.transferTo(filePath);

        user.setImageUrl("userImage/" + uniqueImageName);
        userRepository.save(user);

        return "userImage/" + uniqueImageName;
    }

    public String encodeImageToBase64(String imagePath) {
        try {
            Path path = Paths.get(imagePath);
            byte[] bytes = Files.readAllBytes(path);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            return null;
        }
    }
}
