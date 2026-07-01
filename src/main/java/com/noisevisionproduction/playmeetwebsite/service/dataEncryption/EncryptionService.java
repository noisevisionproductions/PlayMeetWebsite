package com.noisevisionproduction.playmeetwebsite.service.dataEncryption;

import com.noisevisionproduction.playmeetwebsite.model.EncryptedField;
import com.noisevisionproduction.playmeetwebsite.model.UserModel;
import com.noisevisionproduction.playmeetwebsite.utils.LogsPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

@Service
public class EncryptionService extends LogsPrint {

    private final KeyLoaderForEncryptionService keyLoader;
    private final SecureRandom secureRandom;
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    @Autowired
    public EncryptionService(KeyLoaderForEncryptionService keyLoader) {
        this.keyLoader = keyLoader;
        this.secureRandom = new SecureRandom();
    }

    public String encrypt(String valueToEnc) throws Exception {
        if (valueToEnc == null || valueToEnc.isEmpty()) {
            return valueToEnc;
        }

        String activeKeyId = keyLoader.getActiveKeyId();
        byte[] activeKey = keyLoader.getKey(activeKeyId);

        byte[] iv = new byte[16];
        secureRandom.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec keySpec = new SecretKeySpec(Arrays.copyOf(activeKey, 16), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] cryptogram = cipher.doFinal(valueToEnc.getBytes(StandardCharsets.UTF_8));
        byte[] keyIdBytes = activeKeyId.getBytes(StandardCharsets.UTF_8);

        ByteBuffer byteBuffer = ByteBuffer.allocate(1 + keyIdBytes.length + 16 + cryptogram.length);
        byteBuffer.put((byte) keyIdBytes.length);
        byteBuffer.put(keyIdBytes);
        byteBuffer.put(iv);
        byteBuffer.put(cryptogram);

        return Base64.getEncoder().encodeToString(byteBuffer.array());
    }

    public String decrypt(String encryptedValue) throws Exception {
        if (encryptedValue == null || encryptedValue.isEmpty()) {
            return encryptedValue;
        }

        byte[] decoded = Base64.getDecoder().decode(encryptedValue);
        boolean isNewFormat = false;
        String keyId;
        byte[] keyToUse = null;
        int keyIdLength = decoded[0];

        if (keyIdLength > 0 && keyIdLength < 20 && decoded.length > (1 + keyIdLength + 16)) {
            keyId = new String(decoded, 1, keyIdLength, StandardCharsets.UTF_8);
            keyToUse = keyLoader.getKey(keyId);

            if (keyToUse != null) {
                isNewFormat = true;
            }
        }

        Cipher cipher = Cipher.getInstance(ALGORITHM);

        if (isNewFormat) {
            byte[] iv = new byte[16];
            System.arraycopy(decoded, 1 + keyIdLength, iv, 0, 16);

            int cryptogramLength = decoded.length - (1 + keyIdLength + 16);
            byte[] cryptogram = new byte[cryptogramLength];
            System.arraycopy(decoded, 1 + keyIdLength + 16, cryptogram, 0, cryptogramLength);

            SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(keyToUse, 16), "AES");
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            byte[] decryptedValue = cipher.doFinal(cryptogram);
            return new String(decryptedValue, StandardCharsets.UTF_8);
        } else {
            byte[] legacyKey = keyLoader.getLegacyKey();
            SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(legacyKey, 16), "AES");

            IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOf(legacyKey, 16));

            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            byte[] decryptedValue = cipher.doFinal(decoded);
            return new String(decryptedValue, StandardCharsets.UTF_8);
        }
    }

    private void processAnnotatedFields(Object model, boolean isEncryption) {
        if (model == null) {
            return;
        }

        Field[] fields = model.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(EncryptedField.class) && field.getType().equals(String.class)) {
                try {
                    field.setAccessible(true);
                    String currentValue = (String) field.get(model);

                    if (currentValue != null && !currentValue.isEmpty()) {
                        String newValue;
                        if (isEncryption) {
                            newValue = encrypt(currentValue);
                        } else {
                            newValue = decrypt(currentValue);
                        }

                        field.set(model, newValue);
                    }
                } catch (Exception e) {
                    logError("Error processing field: " + field.getName(), e);
                }
            }
        }
    }

    public void encryptUserData(UserModel userModel) {
        processAnnotatedFields(userModel, true);
    }

    public void decryptUserData(UserModel userModel) {
        processAnnotatedFields(userModel, false);
    }

    public void encryptModel(Object anyModel) {
        processAnnotatedFields(anyModel, true);
    }

    public void decryptModel(Object anyModel) {
        processAnnotatedFields(anyModel, false);
    }
}
