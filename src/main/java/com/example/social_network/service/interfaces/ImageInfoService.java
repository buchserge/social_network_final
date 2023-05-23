package com.example.social_network.service.interfaces;

import com.example.social_network.domain.ImageInfo;

public interface ImageInfoService {
    ImageInfo findImageById(Long id);

    void saveImage(ImageInfo imageInfo);
}
