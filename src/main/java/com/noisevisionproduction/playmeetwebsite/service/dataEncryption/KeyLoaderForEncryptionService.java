package com.noisevisionproduction.playmeetwebsite.service.dataEncryption;

import com.noisevisionproduction.playmeetwebsite.utils.LogsPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class KeyLoaderForEncryptionService extends LogsPrint {

    private byte[] key;
    private final ClassPathResource classPathResource;

    @Autowired
    public KeyLoaderForEncryptionService(@Value("${encryption.keys:keys.properties}") String filePath) {
        this.classPathResource = new ClassPathResource(filePath);
        loadKeyFromProperties();
    }

    public void loadKeyFromProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = classPathResource.getInputStream()) {
            properties.load(inputStream);
            String keyString = properties.getProperty("encryption_key");
            this.key = keyString.getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            logError("Error loading key from keys.properties", e);
        }
    }

    public byte[] getKey() {
        return this.key;
    }
}

