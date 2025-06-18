package com.tyunin.backend.service.impl;

import com.tyunin.backend.exception.ProfilePictureNotFound;
import com.tyunin.backend.service.ObjectStorageService;
import com.tyunin.backend.service.UserImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserImagesServiceImp implements UserImagesService {

	private final String USERS_STRING = "users";
	private final String IMAGES_STRING = "images";
	private final String PROFILE_PICTURE_STRING = "profile-picture";

	private final ObjectStorageService objectStorageService;
	private final Logger logger;

	@Autowired
	public UserImagesServiceImp(ObjectStorageService objectStorageService, Logger logger) {
		this.objectStorageService = objectStorageService;
		this.logger = logger;
	}

	@Override
	public String getUserProfilePictureKey(Long userId) {
		List<String> keys = objectStorageService.getImages(USERS_STRING + "/" + userId + "/" + PROFILE_PICTURE_STRING);
		if (keys.isEmpty()) throw new ProfilePictureNotFound();
		return keys.get(0);
	}

	@Override
	public List<String> getUserImages(Long userId) {
		return objectStorageService.getImages(USERS_STRING + "/" + userId + "/" + IMAGES_STRING);
	}

	@Override
	public String uploadImage(MultipartFile file, Long userId) {
		return objectStorageService.uploadImage(file, USERS_STRING + "/" + userId + "/" + IMAGES_STRING);
	}

	@Override
	public String uploadProfilePicture(MultipartFile file, Long userId) {
		try {
			objectStorageService.deleteImage(getUserProfilePictureKey(userId));
		} catch (Exception e) {
			logger.error("Failed upload profile picture: " + e.getMessage());
		}
		return objectStorageService.uploadImage(file, USERS_STRING + "/" + userId + "/" + PROFILE_PICTURE_STRING);
	}

	@Override
	public byte[] downloadImage(String key) {
		return objectStorageService.downloadImage(key);
	}

	@Override
	public void deleteImage(String key) {
		objectStorageService.deleteImage(key);
	}

	@Override
	public String generatePresignedUrl(String key, int expirationMinutes) {
		return objectStorageService.generatePresignedUrl(key, expirationMinutes);
	}
}
