import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.file.Files;
import java.security.SecureRandom;

public class FileEncryptor {

    private static void encryptFile(File inputFile, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);

        byte[] inputBytes = Files.readAllBytes(inputFile.toPath());
        byte[] outputBytes = cipher.doFinal(inputBytes);

        try (FileOutputStream fos = new FileOutputStream(inputFile)) {
            fos.write(iv); // write IV at beginning
            fos.write(outputBytes);
        }
    }

    private static void decryptFile(File inputFile, SecretKey key) throws Exception {
        byte[] fileBytes = Files.readAllBytes(inputFile.toPath());

        byte[] iv = new byte[16];
        System.arraycopy(fileBytes, 0, iv, 0, 16);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        byte[] encryptedData = new byte[fileBytes.length - 16];
        System.arraycopy(fileBytes, 16, encryptedData, 0, encryptedData.length);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decryptedData = cipher.doFinal(encryptedData);

        try (FileOutputStream fos = new FileOutputStream(inputFile)) {
            fos.write(decryptedData);
        }
    }

    private static void encryptRecursively(File file, SecretKey key) throws Exception {
        if (file.isFile()) {
            encryptFile(file, key);
        } else {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    encryptRecursively(child, key);
                }
            } else {
                System.err.println("Warning: Cannot list files for directory " + file.getAbsolutePath());
            }
        }
    }

    private static void decryptRecursively(File file, SecretKey key) throws Exception {
        if (file.isFile()) {
            decryptFile(file, key);
        } else {
            File[] children = file.listFiles();
            if (children != null) {
                for (File child : children) {
                    decryptRecursively(child, key);
                }
            } else {
                System.err.println("Warning: Cannot list files for directory " + file.getAbsolutePath());
            }
        }
    }

    public static void encryptFolderWithPassword(File folder, String password) throws Exception {
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Provided folder does not exist or is not a directory");
        }
        SecretKey key = getKeyFromPassword(password);
        encryptRecursively(folder, key);
    }

    public static void decryptFolderWithPassword(File folder, String password) throws Exception {
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("Provided folder does not exist or is not a directory");
        }
        SecretKey key = getKeyFromPassword(password);
        decryptRecursively(folder, key);
    }

    private static SecretKey getKeyFromPassword(String password) throws Exception {
        byte[] keyBytes = password.getBytes("UTF-8");
        byte[] keyPadded = new byte[16];
        System.arraycopy(keyBytes, 0, keyPadded, 0, Math.min(keyBytes.length, 16));
        return new SecretKeySpec(keyPadded, "AES");
    }
}
