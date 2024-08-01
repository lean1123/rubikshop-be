package com.example.rubikShopApi.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FireBaseConfig {

	@SuppressWarnings("deprecation")
	@Bean
	public FirebaseApp firebaseApp() throws IOException {
		if (FirebaseApp.getApps().isEmpty()) {
			InputStream serviceAccount = getClass().getClassLoader()
					.getResourceAsStream("keyjson/rubikshop-fe-firebase-adminsdk-et9uf-613991055e.json");

			if (serviceAccount == null) {
				throw new IOException("Could not find the service account key file.");
			}

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

			return FirebaseApp.initializeApp(options);
		} else {
			return FirebaseApp.getInstance();
		}
	}
}
