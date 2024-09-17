package com.noisevisionproduction.playmeetwebsite.service.dataEncryption;

import com.noisevisionproduction.playmeetwebsite.model.UserModel;
import com.noisevisionproduction.playmeetwebsite.utils.LogsPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Service
public class EncryptionService extends LogsPrint {

    private final byte[] key;

    @Autowired
    public EncryptionService(KeyLoaderForEncryptionService keyLoader) {
        this.key = keyLoader.getKey();
    }

    public String encrypt(String valueToEnc) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(Arrays.copyOf(this.key, 16), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(Arrays.copyOf(this.key, cipher.getBlockSize()));
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(valueToEnc.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(cipherText);
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

    public void encryptUserData(UserModel userModel) {
        try {
            if (userModel.getName() != null) {
                userModel.setName(encrypt(userModel.getName()));
            }
            if (userModel.getLocation() != null) {
                userModel.setLocation(encrypt(userModel.getLocation()));
            }
            if (userModel.getAboutMe() != null) {
                userModel.setAboutMe(encrypt(userModel.getAboutMe()));
            }
        } catch (Exception e) {
            logError("Encryption error: ", e);
        }
    }

    public void decryptUserData(UserModel userModel) {
        try {
            if (userModel.getName() != null) {
                userModel.setName(decrypt(userModel.getName()));
            }
            if (userModel.getAge() != null) {
                userModel.setAge(decrypt(userModel.getAge()));
            }
            if (userModel.getLocation() != null) {
                userModel.setLocation(decrypt(userModel.getLocation()));
            }
            if (userModel.getGender() != null) {
                userModel.setGender(decrypt(userModel.getGender()));
            }
            if (userModel.getAboutMe() != null) {
                userModel.setAboutMe(decrypt(userModel.getAboutMe()));
            }
        } catch (Exception e) {
            logError("Decryption error: ", e);
        }
    }
}
