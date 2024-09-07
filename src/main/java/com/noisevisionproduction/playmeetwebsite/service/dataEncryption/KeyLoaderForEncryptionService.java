package com.noisevisionproduction.playmeetwebsite.service.dataEncryption;

import com.noisevisionproduction.playmeetwebsite.LogsPrint;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@Service
public class KeyLoaderForEncryptionService extends LogsPrint {

    private byte[] key;

    public KeyLoaderForEncryptionService() {
        loadKeyFromProperties();
    }

    private void loadKeyFromProperties() {
        Properties properties = new Properties();
        try (InputStream inputStream = new ClassPathResource("keys.properties").getInputStream()) {
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
