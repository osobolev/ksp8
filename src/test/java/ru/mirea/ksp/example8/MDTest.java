package ru.mirea.ksp.example8;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;

public class MDTest {

    private static final BCrypt.Hasher hasher = BCrypt.with(LongPasswordStrategies.truncate(BCrypt.Version.VERSION_2A));

    private static String hash(String password) {
        return hasher.hashToString(12, password.toCharArray());
    }

    public static void main(String[] args) throws Exception {
        System.out.println(hash("qwerty123"));
        System.out.println(hash("qwerty123"));
//        MessageDigest md = MessageDigest.getInstance("SHA-256");
//        byte[] digest = md.digest("qwerty123".getBytes(StandardCharsets.UTF_8));
//        System.out.println(new BinaryToTextEncoding.Hex().encode(digest, ByteOrder.nativeOrder()));
    }
}
