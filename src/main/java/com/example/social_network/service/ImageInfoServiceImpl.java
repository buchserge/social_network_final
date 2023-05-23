package com.example.social_network.service;


import com.example.social_network.domain.ImageInfo;
import com.example.social_network.repo.ImageInfoRepo;
import com.example.social_network.service.interfaces.ImageInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageInfoServiceImpl implements ImageInfoService {
    @Autowired
    private final ImageInfoRepo imageInfoRepo;

    public ImageInfoServiceImpl(ImageInfoRepo imageInfoRepo) {
        this.imageInfoRepo = imageInfoRepo;
    }


    @Override
    public ImageInfo findImageById(Long id) {
        return imageInfoRepo.findById(id).orElseThrow();
    }

    @Override
    public void saveImage(ImageInfo imageInfo) {
        imageInfoRepo.save(imageInfo);
    }
}
