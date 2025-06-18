package com.tyunin.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserImagesService {

	String getUserProfilePictureKey(Long userId);
	List<String> getUserImages(Long userId);
	String uploadImage(MultipartFile file, Long userId);
	String uploadProfilePicture(MultipartFile file, Long userId);
	byte[] downloadImage(String key);
	void deleteImage(String key);
	String generatePresignedUrl(String key, int expirationMinutes);
}
