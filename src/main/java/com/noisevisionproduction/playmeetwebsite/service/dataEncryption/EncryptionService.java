package com.noisevisionproduction.playmeetwebsite.service.dataEncryption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Service
public class EncryptionService {

    private final byte[] key;

    @Autowired
    public EncryptionService(KeyLoaderForEncryptionService keyLoader) {
        this.key = keyLoader.getKey();
    }

    public String decrypt(String encryptedValue) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(this.key, 16), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOf(this.key, cipher.getBlockSize()));
        byte[] encValue = Base64.getDecoder().decode(encryptedValue);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decryptedValue = cipher.doFinal(encValue);
        return new String(decryptedValue, StandardCharsets.UTF_8);
    }
}
