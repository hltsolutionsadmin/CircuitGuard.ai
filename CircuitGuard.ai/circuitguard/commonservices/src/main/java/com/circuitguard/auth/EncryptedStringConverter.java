package com.circuitguard.auth;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    private static final String SECRET = "1234567890abcdef"; // Must be 16/24/32 chars

    private final Cipher encryptCipher;
    private final Cipher decryptCipher;

    public EncryptedStringConverter() {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET.getBytes(), "AES");
            encryptCipher = Cipher.getInstance("AES");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key);

            decryptCipher = Cipher.getInstance("AES");
            decryptCipher.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize AES ciphers", e);
        }
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            return Base64.getEncoder().encodeToString(
                    encryptCipher.doFinal(attribute.getBytes(StandardCharsets.UTF_8))
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Encryption failed for value: " + attribute, e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            return new String(
                    decryptCipher.doFinal(Base64.getDecoder().decode(dbData)),
                    StandardCharsets.UTF_8
            );
        } catch (Exception e) {
            throw new IllegalArgumentException("Decryption failed for value: " + dbData, e);
        }
    }
}
