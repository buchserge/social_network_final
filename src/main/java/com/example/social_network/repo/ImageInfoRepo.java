package com.example.social_network.repo;


import com.example.social_network.domain.ImageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ImageInfoRepo extends JpaRepository<ImageInfo,Long> {

}
