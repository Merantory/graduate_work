package com.tyunin.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public interface ObjectStorageService {
	String uploadImage(MultipartFile file);
	String uploadImage(MultipartFile file, String path);
	byte[] downloadImage(String key);
	void deleteImage(String key);
	List<String> getImages(String path);
	String generatePresignedUrl(String key, int expirationMinutes);
}
