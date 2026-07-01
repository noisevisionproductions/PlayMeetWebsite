package com.noisevisionproduction.playmeetwebsite.service.dataEncryption;

import com.noisevisionproduction.playmeetwebsite.model.EncryptedField;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class EncryptionServiceRebuildTest {

    private EncryptionService encryptionService;

    private final String ACTIVE_KEY_ID = "v2";
    private byte[] ACTIVE_KEY;
    private final String HISTORIC_KEY_ID = "v1";
    private byte[] HISTORIC_KEY;
    private final byte[] LEGACY_KEY = "1234567890123456".getBytes(StandardCharsets.UTF_8);

    @BeforeEach
    public void setup() throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        ACTIVE_KEY = new byte[32];
        HISTORIC_KEY = new byte[32];
        secureRandom.nextBytes(ACTIVE_KEY);
        secureRandom.nextBytes(HISTORIC_KEY);

        KeyLoaderForEncryptionService keyLoader = new KeyLoaderForEncryptionService("keys.properties");

        Field activeKeyIdField = KeyLoaderForEncryptionService.class.getDeclaredField("activeKeyId");
        Field keyRegistryField = KeyLoaderForEncryptionService.class.getDeclaredField("keyRegistry");
        Field legacyKeyField = KeyLoaderForEncryptionService.class.getDeclaredField("legacyKey");

        activeKeyIdField.setAccessible(true);
        keyRegistryField.setAccessible(true);
        legacyKeyField.setAccessible(true);
        activeKeyIdField.set(keyLoader, ACTIVE_KEY_ID);

        @SuppressWarnings("unchecked")
        Map<String, byte[]> registry = (Map<String, byte[]>) keyRegistryField.get(keyLoader);
        registry.put(ACTIVE_KEY_ID, ACTIVE_KEY);
        registry.put(HISTORIC_KEY_ID, HISTORIC_KEY);

        legacyKeyField.set(keyLoader, LEGACY_KEY);

        encryptionService = new EncryptionService(keyLoader);
    }

    /*
     * Checks if active key is used and the binary structure is exactly:
     * [1 byte length] + [key ID] + [16 bytes IV] + [cryptogram]
     * */
    @Test
    @DisplayName("Should encrypt data using binary serialization and random IV")
    public void testNewEncryptionAndRandomIv() throws Exception {
        String dataToEncrypt = "TestSecretData_" + UUID.randomUUID();

        String result1 = encryptionService.encrypt(dataToEncrypt);
        String result2 = encryptionService.encrypt(dataToEncrypt);

        assertNotNull(result1);
        assertNotEquals(dataToEncrypt, result1, "Data should be encrypted");
        assertNotEquals(result1, result2, "IV must be random, so two encryptions should not match");

        byte[] rawBytes = Base64.getDecoder().decode(result1);

        int idLength = rawBytes[0];
        assertEquals(ACTIVE_KEY_ID.length(), idLength, "First byte should be the length of the key ID");

        String extractedId = new String(rawBytes, 1, idLength, StandardCharsets.UTF_8);
        assertEquals(ACTIVE_KEY_ID, extractedId, "Key ID in the byte array should match the active key");

        byte[] iv = new byte[16];
        System.arraycopy(rawBytes, 1 + idLength, iv, 0, 16);

        boolean isZeroed = true;
        for (byte b : iv) {
            if (b != 0) {
                isZeroed = false;
                break;
            }
        }
        assertFalse(isZeroed, "IV should not be empty");

        int cryptoStart = 1 + idLength + 16;
        int cryptoLen = rawBytes.length - cryptoStart;
        byte[] cryptogram = new byte[cryptoLen];
        System.arraycopy(rawBytes, cryptoStart, cryptogram, 0, cryptoLen);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(ACTIVE_KEY, 16), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        String decryptedData = new String(cipher.doFinal(cryptogram), StandardCharsets.UTF_8);
        assertEquals(dataToEncrypt, decryptedData, "Manual decryption failed, cryptogram is wrong");
    }

    /*
     * Checks if service can still decrypt old database entries
     * */
    @Test
    @DisplayName("Should correctly decrypt legacy format payload")
    public void testLegacyDecryptionWorks() throws Exception {
        String oldData = "LegacyUserPassword_" + UUID.randomUUID();

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(LEGACY_KEY, 16), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(Arrays.copyOf(LEGACY_KEY, 16));
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] oldCryptogram = cipher.doFinal(oldData.getBytes(StandardCharsets.UTF_8));
        String oldBase64 = Base64.getEncoder().encodeToString(oldCryptogram);

        String decrypted = encryptionService.decrypt(oldBase64);
        assertEquals(oldData, decrypted, "Service failed to fallback and decrypt legacy data");
    }

    /*
     *  Checks if older keys defined in the historic property are loaded and used
     * */
    @Test
    @DisplayName("Should decrypt payloads using historical keys based on key ID")
    public void testHistoricKeyDecryption() throws Exception {
        String historicalData = "OldDataThatNeedsV1_" + UUID.randomUUID().toString();

        byte[] keyIdBytes = HISTORIC_KEY_ID.getBytes(StandardCharsets.UTF_8);
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec secretKeySpec = new SecretKeySpec(Arrays.copyOf(HISTORIC_KEY, 16), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] cryptogram = cipher.doFinal(historicalData.getBytes(StandardCharsets.UTF_8));
        byte[] fullPayload = new byte[1 + keyIdBytes.length + 16 + cryptogram.length];

        fullPayload[0] = (byte) keyIdBytes.length;
        System.arraycopy(keyIdBytes, 0, fullPayload, 1, keyIdBytes.length);
        System.arraycopy(iv, 0, fullPayload, 1 + keyIdBytes.length, 16);
        System.arraycopy(cryptogram, 0, fullPayload, 1 + keyIdBytes.length + 16, cryptogram.length);

        String payloadBase64 = Base64.getEncoder().encodeToString(fullPayload);

        String decrypted = encryptionService.decrypt(payloadBase64);
        assertEquals(historicalData, decrypted, "Service failed to use historical key for decryption");
    }

    /*
     * Verifies that the reflection logic usees @EncryptedField dynamically
     * */
    @Test
    @DisplayName("Should process @EncryptedField on any object using Reflection")
    public void testCustomAnnotationReflectionProcessing() {
        class TestModel {
            @EncryptedField
            private String secretNotes = "Secret note";

            private String publicName = "John Doe";

            public String getSecretNotes() {
                return secretNotes;
            }

            public String getPublicName() {
                return publicName;
            }
        }

        TestModel testModel = new TestModel();

        encryptionService.encryptModel(testModel);

        assertNotEquals("Secret note", testModel.getSecretNotes(), "Annotated field should be encrypted");
        assertTrue(testModel.getSecretNotes().length() > 20, "Encrypted string should be long");
        assertEquals("John Doe", testModel.getPublicName(), "Unannotated field should not change");

        encryptionService.decryptModel(testModel);
        assertEquals("Secret note", testModel.getSecretNotes(), "Annotated field should be decrypted back to normal");
    }
}
