package com.tyunin.backend.service.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.tyunin.backend.service.ObjectStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ObjectStorageServiceImpl implements ObjectStorageService {

	@Value("${object-storage.bucket-name}")
	private String bucketName;

	private final AmazonS3 s3Client;

	@Autowired
	public ObjectStorageServiceImpl(AmazonS3 s3Client) {
		this.s3Client = s3Client;
	}

	public String uploadImage(MultipartFile file) {
		return uploadImage(file, "/common");
	}

	/**
	 * Загружает изображение в S3 хранилище
	 */
	public String uploadImage(MultipartFile file, String path) {
		try {
			String fileExtension = getFileExtension(file.getOriginalFilename());
			String key = generateKey(path, fileExtension);

			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(file.getContentType());
			metadata.setContentLength(file.getSize());

			s3Client.putObject(
					new PutObjectRequest(
							bucketName,
							key,
							file.getInputStream(),
							metadata
					).withCannedAcl(CannedAccessControlList.PublicRead)
			);

			return key;
		} catch (IOException e) {
			throw new RuntimeException("Ошибка при загрузке файла", e);
		}
	}

	/**
	 * Получает изображение из S3 хранилища
	 */
	public byte[] downloadImage(String key) {
		try {
			S3Object s3Object = s3Client.getObject(bucketName, key);
			S3ObjectInputStream inputStream = s3Object.getObjectContent();
			return IOUtils.toByteArray(inputStream);
		} catch (AmazonS3Exception | IOException e) {
			throw new RuntimeException("Ошибка при скачивании файла", e);
		}
	}

	/**
	 * Генерирует presigned URL для временного доступа к файлу
	 */
	public String generatePresignedUrl(String key, int expirationMinutes) {
		Date expiration = new Date();
		long expTimeMillis = expiration.getTime();
		expTimeMillis += 1000 * 60 * expirationMinutes;
		expiration.setTime(expTimeMillis);

		GeneratePresignedUrlRequest generatePresignedUrlRequest =
				new GeneratePresignedUrlRequest(bucketName, key)
						.withMethod(HttpMethod.GET)
						.withExpiration(expiration);

		URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
		return url.toString();
	}

	/**
	 * Удаляет изображение из S3 хранилища
	 */
	public void deleteImage(String key) {
		s3Client.deleteObject(bucketName, key);
	}

	/**
	 * Получает список изображений пользователя
	 */
	public List<String> getImages(String path) {
		ListObjectsV2Request req = new ListObjectsV2Request()
				.withBucketName(bucketName)
				.withPrefix(path);

		ListObjectsV2Result result = s3Client.listObjectsV2(req);
		return result.getObjectSummaries().stream()
				.map(S3ObjectSummary::getKey)
				.collect(Collectors.toList());
	}

	private String generateKey(String path, String fileExtension) {
		return path + "/" +
				UUID.randomUUID() +
				fileExtension;
	}

	private String getFileExtension(String filename) {
		if (filename == null || filename.lastIndexOf(".") == -1) {
			return "";
		}
		return filename.substring(filename.lastIndexOf("."));
	}

}
