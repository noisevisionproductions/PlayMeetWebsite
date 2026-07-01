package com.noisevisionproduction.playmeetwebsite.service.dataEncryption;

import com.noisevisionproduction.playmeetwebsite.utils.LogsPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class KeyLoaderForEncryptionService extends LogsPrint {

    private String activeKeyId;
    private final Map<String, byte[]> keyRegistry = new HashMap<>();
    private byte[] legacyKey;
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

            // Loading active key
            String activeProperty = properties.getProperty("encryption.keys.active");
            if (activeProperty != null && activeProperty.contains(":")) {
                String[] parts = activeProperty.split(":");
                activeProperty = parts[0];
                keyRegistry.put(activeKeyId, Base64.getDecoder().decode(parts[1]));
            }

            // Loading historical keys
            String historicProperty = properties.getProperty("encryption.keys.historic");
            if (historicProperty != null && !historicProperty.isEmpty()) {
                String[] historicKeys = historicProperty.split(",");
                for (String historicKey : historicKeys) {
                    if (historicKey.contains(":")) {
                        String[] parts = historicKey.split(":");
                        keyRegistry.put(parts[0], Base64.getDecoder().decode(parts[1]));
                    }
                }
            }

            // Legacy key
            String oldKeyString = properties.getProperty("encryption_key");
            if (oldKeyString != null) {
                this.legacyKey = oldKeyString.getBytes(StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            logError("Error loading key from keys.properties", e);
        }
    }

    public byte[] getKey(String keyId) {
        return keyRegistry.get(keyId);
    }

    public String getActiveKeyId() {
        return activeKeyId;
    }

    public byte[] getLegacyKey() {
        return this.legacyKey;
    }
}

