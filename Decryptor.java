import java.io.File;
import java.util.Scanner;

public class Decryptor {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        String folderPath = "C:\\SecuritySimulation";
        File folder = new File(folderPath);

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path: " + folderPath);
            scanner.close();
            return;
        }

        System.out.print("Enter decryption key: ");
        String inputKey = scanner.nextLine();

        if (!inputKey.equals("NoOneWasHere")) {
            System.out.println("Invalid key. Decryption denied.");
            scanner.close();
            return;
        }

        try {
            FileEncryptor.decryptFolderWithPassword(folder, inputKey);
            System.out.println("Folder decrypted successfully!");
        } catch (Exception e) {
            System.out.println("Decryption failed: " + e.getMessage());
            e.printStackTrace();
        }

        scanner.close();
    }
}
