package com.example.rubikShopApi.service;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageService {

	void init();

	void delete(String storeFileName) throws IOException;

	Path load(String fileName);

	Resource loadAsResource(String fileName);

	void store(MultipartFile file, String storeFileName);

	String getStorageFileName(MultipartFile file, String id);

}
