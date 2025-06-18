package com.tyunin.backend.controller;

import com.tyunin.backend.service.UserImagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserImagesController {

	private final UserImagesService userImagesService;

	@Autowired
	public UserImagesController(UserImagesService userImagesService) {
		this.userImagesService = userImagesService;
	}

	@GetMapping("/{id}/profile/picture")
	public ResponseEntity<Map<String, String>> getUserProfilePicture(@PathVariable("id") Long userId) {
		return ResponseEntity.ok(Map.of("key", userImagesService.getUserProfilePictureKey(userId)));
	}

	@PostMapping("/profile/picture/upload/")
	public ResponseEntity<Map<String, String>> uploadProfilePicture(
			@RequestParam("file") MultipartFile file,
			@RequestParam("userId") Long userId) {

		// Проверка файла
		if (file.isEmpty() || !isValidImageFile(file)) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Недопустимый формат файла"));
		}

		String key = userImagesService.uploadProfilePicture(file, userId);
		String url = userImagesService.generatePresignedUrl(key, 60);

		Map<String, String> response = new HashMap<>();
		response.put("key", key);
		response.put("url", url);

		return ResponseEntity.ok(response);
	}
	@PostMapping("/images/upload")
	public ResponseEntity<Map<String, String>> uploadImage(
			@RequestParam("file") MultipartFile file,
			@RequestParam("userId") Long userId) {

		if (file.isEmpty() || !isValidImageFile(file)) {
			return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Недопустимый формат файла"));
		}

		String key = userImagesService.uploadImage(file, userId);
		String url = userImagesService.generatePresignedUrl(key, 60);

		Map<String, String> response = new HashMap<>();
		response.put("key", key);
		response.put("url", url);

		return ResponseEntity.ok(response);
	}
	@GetMapping("/images")
	public ResponseEntity<byte[]> getImage(@RequestParam("key") String key) {
		try {
			byte[] imageData = userImagesService.downloadImage(key);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(determineContentType(key));
			return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	@GetMapping("/images/presigned/{key}")
	public ResponseEntity<Map<String, String>> getImageUrl(
			@PathVariable String key,
			@RequestParam(value = "expirationMinutes", defaultValue = "30") int expirationMinutes) {

		String url = userImagesService.generatePresignedUrl(key, expirationMinutes);
		return ResponseEntity.ok(Collections.singletonMap("url", url));
	}
	@GetMapping("/{userId}/images")
	public ResponseEntity<List<Map<String, String>>> getUserImages(@PathVariable Long userId) {
		List<String> keys = userImagesService.getUserImages(userId);

		List<Map<String, String>> result = keys.stream().map(key -> {
			Map<String, String> image = new HashMap<>();
			image.put("key", key);
			image.put("url", userImagesService.generatePresignedUrl(key, 60));
			return image;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(result);
	}
	@DeleteMapping("/images/{key}")
	public ResponseEntity<Void> deleteImage(@PathVariable String key) {
		userImagesService.deleteImage(key);
		return ResponseEntity.noContent().build();
	}

	private boolean isValidImageFile(MultipartFile file) {
		String contentType = file.getContentType();
		return contentType != null && contentType.startsWith("image/");
	}

	private MediaType determineContentType(String key) {
		if (key.endsWith(".png")) {
			return MediaType.IMAGE_PNG;
		} else if (key.endsWith(".jpg") || key.endsWith(".jpeg")) {
			return MediaType.IMAGE_JPEG;
		} else if (key.endsWith(".gif")) {
			return MediaType.IMAGE_GIF;
		} else {
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}
}
