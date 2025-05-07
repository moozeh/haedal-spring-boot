package org.moozeh.haedalspringboot.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.moozeh.haedalspringboot.domain.User;
import org.moozeh.haedalspringboot.service.AuthService;
import org.moozeh.haedalspringboot.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final AuthService authService;
    private final ImageService imageService;

    @PutMapping("/users/image")
    public ResponseEntity<String> updateUserImage(@RequestParam("image") MultipartFile image, HttpServletRequest request)
            throws IOException {
        User currentUser = authService.getCurrentUser(request);

        String savedImageName = imageService.updateUserImage(currentUser, image);
        return ResponseEntity.ok(savedImageName);
    }


}
