package com.example.social_network.controller;

import com.example.social_network.domain.ImageInfo;
import com.example.social_network.domain.entity.User;
import com.example.social_network.service.ImageInfoServiceImpl;
import com.example.social_network.service.UserServiceImpl;
import com.example.social_network.utils.UpdateImageUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Controller
@RequestMapping("/api/image")
public class ImageController {
    @Autowired
    private final ImageInfoServiceImpl imageInfoServiceImpl;
    @Autowired
    private final UserServiceImpl userServiceImpl;
    @Autowired
    private  final UpdateImageUtils updateImageUtils;

    public ImageController(ImageInfoServiceImpl imageInfoServiceImpl, UserServiceImpl userServiceImpl, UpdateImageUtils updateImageUtils) {
        this.imageInfoServiceImpl = imageInfoServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.updateImageUtils = updateImageUtils;
    }


    @PostMapping("/uploadImageData")
    String uploadImageDatabase(@AuthenticationPrincipal User user, @RequestParam("image") MultipartFile file,
                               RedirectAttributes attributes, Model model,
                               @RequestHeader(required = false) String referer) throws IOException {

        ImageInfo imageInfo = new ImageInfo();
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        if (file.isEmpty()) {
            attributes.addFlashAttribute("error", "you need to choose an image");
            attributes.addFlashAttribute("imageError", "true");
            return "redirect:" + components.getPath();
        } else {
            updateImageUtils.saveImage(user, file, imageInfo);
            return "redirect:" + components.getPath();
        }
    }


    @GetMapping("/getImage/{id}")
    public void showImage(@PathVariable Long id, HttpServletResponse response) throws IOException {

        ImageInfo imageById = imageInfoServiceImpl.findImageById(id);

        updateImageUtils.getImage(response,imageById);

    }


    @GetMapping("/getImageChat/{id}")
    public void showImageChat(@PathVariable Long id, HttpServletResponse response) throws IOException {

        User userById = userServiceImpl.findById(id);
        ImageInfo imageInfo = (ImageInfo) userById.getImageInfo();

        updateImageUtils.getImage(response,imageInfo);


    }


}

