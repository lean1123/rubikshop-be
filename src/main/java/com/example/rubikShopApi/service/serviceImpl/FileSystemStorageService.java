package com.example.rubikShopApi.service.serviceImpl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.rubikShopApi.config.StorageProperties;
import com.example.rubikShopApi.exception.StorageException;
import com.example.rubikShopApi.service.IStorageService;

@Service
public class FileSystemStorageService implements IStorageService {
	private final Path rootLocation;

	@Autowired
	public FileSystemStorageService(StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public String getStorageFileName(MultipartFile file, String id) {
		String ext = FilenameUtils.getExtension(file.getOriginalFilename());
		return "p" + id + "." + ext;
	}

	@Override
	public void store(MultipartFile file, String storeFileName) {
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file");
			}

//			Lay duong dan tuyet doi
			Path destinationFile = this.rootLocation.resolve(Paths.get(storeFileName)).normalize().toAbsolutePath();

			if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
				throw new StorageException("Couldn't store file outside current directory!");
			}

			try (InputStream input = file.getInputStream()) {
				Files.copy(input, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new StorageException("Failed to store file: ", e);
		}
	}

	@Override
	public Resource loadAsResource(String fileName) {
		try {
			Path file = load(fileName);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			throw new StorageException("Couldn't not read file: " + fileName);
		} catch (Exception e) {
			// TODO: handle exception
			throw new StorageException("Couldn't not read file: " + fileName);
		}
	}

	@Override
	public Path load(String fileName) {
		// TODO Auto-generated method stub
		return rootLocation.resolve(fileName);
	}

	@Override
	public void delete(String storeFileName) throws IOException {
		Path destinationFile = rootLocation.resolve(Paths.get(storeFileName)).normalize().toAbsolutePath();
		Files.delete(destinationFile);
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
			System.out.println(rootLocation.toString());
		} catch (Exception e) {
			// TODO: handle exception
			throw new StorageException("Couldn't read file: " + e);
		}
	}

}
