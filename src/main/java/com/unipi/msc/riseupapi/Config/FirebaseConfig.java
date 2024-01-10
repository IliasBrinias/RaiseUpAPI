package com.unipi.msc.riseupapi.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {
    private final ResourceLoader resourceLoader;
    @Bean
    public FirebaseApp initializeFirebaseApp() throws IOException {
        String path = resourceLoader.getResource("classpath:firebase_private_key.json").getFile().getAbsolutePath();
        FileInputStream serviceAccount = new FileInputStream(path);

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build();

        return FirebaseApp.initializeApp(options);
    }
}
