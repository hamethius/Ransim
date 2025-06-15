import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.util.*;

public class FileEncryptor {
    private static final String ALGO = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16;

    public static void encryptFolder(File folder, String key) throws Exception {
        process(folder, key, true);
    }

    public static void decryptFolder(File folder, String key) throws Exception {
        process(folder, key, false);
    }

    private static void process(File file, String key, boolean encrypt) throws Exception {
        if (file.isDirectory()) {
            for (File f : Objects.requireNonNull(file.listFiles())) process(f, key, encrypt);
            
        } else {
            byte[] content = Files.readAllBytes(file.toPath());
            byte[] iv = new byte[IV_SIZE];
            Cipher cipher = Cipher.getInstance(ALGO);
            SecretKeySpec secret = new SecretKeySpec(Arrays.copyOf(key.getBytes(), 16), "AES");

            if (encrypt) {
                new SecureRandom().nextBytes(iv);
                cipher.init(Cipher.ENCRYPT_MODE, secret, new IvParameterSpec(iv));
                byte[] encrypted = cipher.doFinal(content);
                Files.write(file.toPath(), join(iv, encrypted));
            } else {
                System.arraycopy(content, 0, iv, 0, IV_SIZE);
                byte[] encrypted = Arrays.copyOfRange(content, IV_SIZE, content.length);
                cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
                Files.write(file.toPath(), cipher.doFinal(encrypted));
            }
        }
    }

    private static byte[] join(byte[] a, byte[] b) {
        byte[] r = new byte[a.length + b.length];
        System.arraycopy(a, 0, r, 0, a.length);
        System.arraycopy(b, 0, r, a.length, b.length);
        return r;
    }
}
