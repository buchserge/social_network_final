package com.example.social_network.utils;

import com.example.social_network.domain.ImageInfo;
import com.example.social_network.domain.entity.User;
import com.example.social_network.service.ImageInfoServiceImpl;
import com.example.social_network.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Component
public class UpdateImageUtils {
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private ImageInfoServiceImpl imageInfoServiceImpl;

    public UpdateImageUtils(UserServiceImpl userServiceImpl, ImageInfoServiceImpl imageInfoServiceImpl) {
        this.userServiceImpl = userServiceImpl;
        this.imageInfoServiceImpl = imageInfoServiceImpl;
    }

    public void saveImage(User user, MultipartFile file, ImageInfo imageInfo) throws IOException {
        User currentUser = userServiceImpl.getUserByName(user.getName());
        imageInfo.setName(file.getName() + "_" + currentUser.getName());
        imageInfo.setUser(currentUser);
        byte[] bytes = file.getBytes();
        imageInfo.setImageData(bytes);

        if (ObjectUtils.isEmpty(currentUser.getImageInfo())) {
            imageInfoServiceImpl.saveImage(imageInfo);
        } else {
            ImageInfo imageById = imageInfoServiceImpl.findImageById(currentUser.getImageInfo().getId());
            imageById.setName(file.getName() + "_" + currentUser.getName());
            imageById.setUser(currentUser);
            imageById.setImageData(bytes);
            imageInfoServiceImpl.saveImage(imageById);
        }
        userServiceImpl.saveNew(currentUser);
    }

    public void getImage( HttpServletResponse response,ImageInfo imageInfo) throws IOException {
        response.setContentType("image/jpeg");
        byte[] imageData = imageInfo.getImageData();
        String encode = Base64.getEncoder().encodeToString(imageData);

        InputStream is = new ByteArrayInputStream(imageData);
        IOUtils.copy(is, response.getOutputStream());
    }


}
