package com.accountplace.api.security.crypto;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility class for cryptographic operations, including encryption and decryption using AES.
 * This class provides methods for encrypting and decrypting text using a symmetric encryption algorithm (AES).
 */
@Component
public class CryptoUtils {

    /**
     * Encrypts a given plaintext using the provided AES secret key.
     *
     * @param plaintext The plain text to be encrypted.
     * @param key The AES secret key used for encryption.
     * @return The encrypted text encoded in Base64 format.
     * @throws Exception If an error occurs during the encryption process.
     */
    public String encrypt(String plaintext, SecretKey key) throws Exception {
        // Initialize cipher for AES encryption
        Cipher cipher = Cipher.getInstance("AES");

        // Set cipher mode to encryption and initialize with the provided key
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Encrypt the plaintext and encode the result to Base64
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * Decrypts a given ciphertext using the provided AES secret key.
     *
     * @param ciphertext The encrypted text in Base64 format to be decrypted.
     * @param key The AES secret key used for decryption.
     * @return The decrypted plain text.
     * @throws Exception If an error occurs during the decryption process.
     */
    public String decrypt(String ciphertext, SecretKey key) throws Exception {
        // Initialize cipher for AES decryption
        Cipher cipher = Cipher.getInstance("AES");

        // Set cipher mode to decryption and initialize with the provided key
        cipher.init(Cipher.DECRYPT_MODE, key);

        // Decode the ciphertext from Base64 and decrypt it
        byte[] decodedBytes = Base64.getDecoder().decode(ciphertext);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);

        // Return the decrypted text as a string
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }
}
