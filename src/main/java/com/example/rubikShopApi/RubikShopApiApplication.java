package com.example.rubikShopApi;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.rubikShopApi.config.StorageProperties;
import com.example.rubikShopApi.service.IStorageService;

@SpringBootApplication
@EnableConfigurationProperties({StorageProperties.class})
public class RubikShopApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RubikShopApiApplication.class, args);
	}
	
	// thêm cấu hình Storage
		@Bean
		CommandLineRunner init(IStorageService storageService) {
			return (args) -> {
				storageService.init();
			};
		}

		@Bean
		public MultipartResolver multipartResolver() {
			StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
			return resolver;
		}

		@Bean
		public Cloudinary cloudinary() {

			Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap("cloud_name", "dcoh5j8ue", "api_key",
					"786829317551111", "api_secret", "uTiv30AuC5IVHH3qhTSN4WSu6l8", "secure", true));
			return cloudinary;
		}

}
