package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashData {


    public String get_SHA_SecurePassword(String passwordToHash, String shaType) throws NoSuchAlgorithmException {

        byte[] salt = getSalt();
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance(shaType);
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return generatedPassword;

    }

    //Add salt
    public byte[] getSalt() throws NoSuchAlgorithmException
    {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    public String get_SHA_1_SecurePassword(String passwordToHash) throws NoSuchAlgorithmException {
        return get_SHA_SecurePassword(passwordToHash,"SHA-1");
    }

    public String get_SHA_256_SecurePassword(String passwordToHash) throws NoSuchAlgorithmException {
        return get_SHA_SecurePassword(passwordToHash,"SHA-256");
    }

    public String get_SHA_384_SecurePassword(String passwordToHash) throws NoSuchAlgorithmException {
        //Use MessageDigest md = MessageDigest.getInstance("SHA-384");
        return get_SHA_SecurePassword(passwordToHash,"SHA-384");
    }

    public String get_SHA_512_SecurePassword(String passwordToHash) throws NoSuchAlgorithmException {
        //Use MessageDigest md = MessageDigest.getInstance("SHA-512");
        return get_SHA_SecurePassword(passwordToHash,"SHA-512");
    }
}
