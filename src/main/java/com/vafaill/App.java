package com.vafaill;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class App {
    private static JPanel contentPanel;
    private static JTextField usernameField;
    private static JPasswordField passwordField;
    private static JButton playButton; // playButton'u değişken olarak tanımladık
    private static JButton settingsButton; // settingsButton'u değişken olarak tanımladık

    public static void main(String[] args) {
        // **************************************************************************************************************************************************************************************
        // Swing arayüzü oluşturuluyor
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Ana pencere oluşturuluyor
                JFrame frame = new JFrame("MulTable");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(800, 500);
                frame.setLocationRelativeTo(null);

                try {
                    // Swing görünümü ayarlanıyor
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Ana panel oluşturuluyor
                JPanel mainPanel = new JPanel(new BorderLayout());
                frame.getContentPane().add(mainPanel);

                // Yan panel oluşturuluyor
                JPanel sidebarPanel = new JPanel();
                sidebarPanel.setBackground(new Color(0, 0, 0));
                sidebarPanel.setPreferredSize(new Dimension(200, 0));
                sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));

                JButton loginButton = createButton("Login");
                playButton = createButton("Oyna"); // playButton'u oluşturduk
                playButton.setEnabled(false); // Başlangıçta devre dışı bıraktık
                JButton reportsButton = createButton("Raporlar"); // reporstButton'u oluşturduk
                settingsButton = createButton("Ayarlar"); // settingsButton'u oluşturduk
                settingsButton.setEnabled(false); // Başlangıçta devre dışı bıraktık

                // Yan panel düğmeleri ekleniyor
                sidebarPanel.add(loginButton);
                sidebarPanel.add(playButton);
                sidebarPanel.add(reportsButton);
                sidebarPanel.add(settingsButton);

                // İçerik paneli oluşturuluyor
                contentPanel = new JPanel();
                contentPanel.setLayout(new CardLayout());
                contentPanel.setBackground(new Color(10, 20, 10));

                // İçerik panelleri oluşturuluyor
                JPanel loginContentPanel = createLoginContentPanel();
                JPanel playContentPanel = createGameContentPanel();
                JPanel reportsContentPanel = createReportsContentPanel();
                JPanel settingsContentPanel = createSettingsContentPanel();

                // İçerik panelleri ana panel'e ekleniyor
                contentPanel.add(loginContentPanel, "login");
                contentPanel.add(playContentPanel, "play");
                contentPanel.add(reportsContentPanel, "reports");
                contentPanel.add(settingsContentPanel, "settings");

                // Ana panel'e yan panel ve içerik paneli ekleniyor
                mainPanel.add(sidebarPanel, BorderLayout.WEST);
                mainPanel.add(contentPanel, BorderLayout.CENTER);

                // Butonlara dinleyiciler ekleniyor
                loginButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showContentPanel("login");
                    }
                });

                playButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showContentPanel("play");
                    }
                });

                reportsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showContentPanel("reports");
                    }
                });

                settingsButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showContentPanel("settings");
                    }
                });

                // Pencere görünür yapılıyor
                frame.setVisible(true);
            }
        });
    }

    // **************************************************************************************************************************************************************************************
    // Buton oluşturmak için yardımcı metot
    static JButton createButton(String text) {
        final JButton button = new JButton(text);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 30, 30));
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                JButton sourceButton = (JButton) evt.getSource();
                sourceButton.setBackground(new Color(50, 50, 50));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                JButton sourceButton = (JButton) evt.getSource();
                sourceButton.setBackground(new Color(0, 30, 30));
            }
        });
        return button;
    }

    // **************************************************************************************************************************************************************************************
    // Giriş içerik panelini oluşturmak için yardımcı metot
    static JPanel createLoginContentPanel() {
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(10, 20, 10));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(usernameLabel, constraints);

        usernameField = new JTextField(20);
        constraints.gridx = 1;
        constraints.gridy = 0;
        panel.add(usernameField, constraints);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);
        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(passwordLabel, constraints);

        passwordField = new JPasswordField(20);
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(passwordField, constraints);

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String roleFromFile = "";

                // Kullanıcı adı ve parola boş ise hata mesajı gösteriliyor
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please enter both username and password.", "Login Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Csv dosyasından kullanıcı bilgileri okunuyor ve kontrol ediliyor. Doğru ise
                    // giriş yapılıyor.

                    File login_info = new File("login_info.csv");

                    // Check if options.csv exists, create one if it doesn't
                    if (!login_info.exists()) {
                        try {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(login_info));
                            writer.write("admin,admin,ebeveyn\r\n" +
                                    "user0,user0,user\r\n" +
                                    "user1,user1,user\r\n" +
                                    "user2,user2,user\r\n"); // Write default values to the file
                            writer.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    String csvFile = "login_info.csv";
                    String line;
                    boolean loginSuccessful = false;

                    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                        while ((line = br.readLine()) != null) {
                            String[] loginInfo = line.split(",");
                            String usernameFromFile = loginInfo[0];
                            String passwordFromFile = loginInfo[1];
                            roleFromFile = loginInfo[2];

                            // Kullanıcı adı ve parola doğru ise giriş başarılı. Rol ebeveyn ise settings
                            // butonu aktif ediliyor. Rol user ise play butonu aktif ediliyor.
                            if (username.equals(usernameFromFile) && password.equals(passwordFromFile)) {
                                loginSuccessful = true;
                                break;
                            }
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                    if (loginSuccessful) {
                        String message = "Username: " + username + "\nRole: " + roleFromFile + "\n\nLogin Successful";
                        // Giriş başarılı mesajı gösteriliyor
                        JOptionPane.showMessageDialog(panel, message, "Login Successful",
                                JOptionPane.INFORMATION_MESSAGE);

                        // Rol ebeveyn ise settings butonu aktif ediliyor. Rol user ise play butonu
                        // aktif ediliyor.
                        if (roleFromFile.equals("ebeveyn")) {
                            settingsButton.setEnabled(true);
                            playButton.setEnabled(false);
                        } else if (roleFromFile.equals("user")) {
                            playButton.setEnabled(true);
                            settingsButton.setEnabled(false);
                        }
                    } else {
                        // Giriş başarısız
                        JOptionPane.showMessageDialog(panel, "Parola ya da kullanıcı adı yanlış.", "Login Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 2;
        panel.add(loginButton, constraints);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                // Kullanıcı adı ve parola boş ise hata mesajı gösteriliyor
                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(panel, "Please enter both username and password.",
                            "Registration Error",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // Kullanıcıyı login_info.csv dosyasına kaydetme işlemi yapılıyor
                    String csvFile = "login_info.csv";

                    try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile, true))) {
                        bw.write(username + "," + password + ",user");
                        bw.newLine();
                        bw.flush();

                        JOptionPane.showMessageDialog(panel, "Registration Successful", "Registration",
                                JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(panel, "Error occurred during registration.",
                                "Registration Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        constraints.gridx = 1;
        constraints.gridy = 3;
        panel.add(registerButton, constraints);

        return panel;
    }

    // **************************************************************************************************************************************************************************************
    // Oyun içerik panelini oluşturmak için yardımcı metot
    static JPanel createGameContentPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(10, 20, 10));

        // Panel başlığını oluşturma
        JLabel titleLabel = new JLabel("Çarpım Tablosu Alıştırması");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        // Oyun panelini oluşturma
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.setBackground(new Color(10, 20, 10));
        panel.add(gamePanel, BorderLayout.CENTER);

        // Başla butonunu oluşturma
        JButton startButton = new JButton("Başla");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setPreferredSize(new Dimension(120, 40));
        startButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        gamePanel.add(startButton, BorderLayout.CENTER);

        // Zamanı gösteren bir etiket oluşturma
        final JLabel timerLabel = new JLabel("Geçen Süre: 0 saniye");
        timerLabel.setForeground(Color.WHITE);
        timerLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gamePanel.add(timerLabel, BorderLayout.SOUTH);

        // Başla butonunun ActionListener'ını oluşturma
        startButton.addActionListener(new ActionListener() {
            private long startTime; // Start time of the game
            private Timer timer; // Timer instance
            private boolean isTimerRunning = false; // Is the timer running?

            @Override
            public void actionPerformed(ActionEvent e) {
                // options.csv dosyasından a, b ve N değerlerini okuma kısmı
                int a = 1; // Minimum çarpım değeri
                int b = 10; // Maksimum çarpım değeri
                int N = 10; // Soru sayısı

                File optionsFile = new File("options.csv");

                // Check if options.csv exists, create one if it doesn't
                if (!optionsFile.exists()) {
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(optionsFile));
                        writer.write("1,2,3"); // Write default values to the file
                        writer.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                // Zamanlayıcı (timer) oluşturma ve başlatma
                timer = new Timer(1000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                        timerLabel.setText("Geçen Süre: " + elapsedTime + " saniye");
                    }
                });

                startTime = System.currentTimeMillis(); // Record the start time
                timer.start();

                try {
                    BufferedReader reader = new BufferedReader(new FileReader("options.csv"));
                    String line;
                    boolean isFirstLine = true;
                    while ((line = reader.readLine()) != null) {
                        if (!isFirstLine) {
                            String[] parts = line.split(",");
                            if (parts.length == 3) {
                                a = Integer.parseInt(parts[0].trim());
                                b = Integer.parseInt(parts[1].trim());
                                N = Integer.parseInt(parts[2].trim());
                            }
                        }
                        isFirstLine = false;
                    }
                    reader.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panel, "Ayarları okurken bir hata oluştu.", "Hata",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }

                // Alıştırma sorularını gösteren bir dialog oluşturma kısmı
                int score = 0;

                // Zamanlayıcı (timer) oluşturma
                Timer timer = new Timer(1000, new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (isTimerRunning) {
                            return;
                        }
                        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000;
                        // Zamanı ekranda görüntüleme
                        System.out.println("Geçen süre: " + elapsedTime + " saniye");
                    }
                });

                startTime = System.currentTimeMillis(); // Record the start time
                timer.start();

                for (int i = 0; i < N; i++) {
                    int num1 = (int) (Math.random() * (b - a + 1)) + a;
                    int num2 = (int) (Math.random() * (b - a + 1)) + a;
                    int result = num1 * num2;
                    String input = JOptionPane.showInputDialog(panel, num1 + " x " + num2 + " =");
                    if (input != null && input.trim().equals(String.valueOf(result))) {
                        score++;
                    }
                }

                timer.stop(); // Stop the timer

                JOptionPane.showMessageDialog(panel,
                        "Puanınız: " + score + " Geçen Süre: " + (System.currentTimeMillis() - startTime) / 1000,
                        "Sonuç", JOptionPane.PLAIN_MESSAGE);

                // Puanı ve diğer bilgileri reports.xlsx dosyasına kaydetme kısmı
                try {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = now.format(formatter);

                    Workbook workbook;
                    Sheet sheet;
                    File file = new File("reports.xlsx");

                    if (file.exists()) {
                        FileInputStream fis = new FileInputStream(file);
                        workbook = WorkbookFactory.create(fis);
                        sheet = workbook.getSheetAt(0);
                    } else {
                        workbook = new XSSFWorkbook();
                        sheet = workbook.createSheet("Reports");
                        Row headerRow = sheet.createRow(0);
                        headerRow.createCell(0).setCellValue("Kullanıcı Adı");
                        headerRow.createCell(1).setCellValue("Tarih");
                        headerRow.createCell(2).setCellValue("Puan");
                        headerRow.createCell(3).setCellValue("Geçen Süre");
                    }

                    int rowCount = sheet.getLastRowNum();
                    Row newRow = sheet.createRow(rowCount + 1);
                    newRow.createCell(0).setCellValue(usernameField.getText());
                    newRow.createCell(1).setCellValue(formattedDateTime);
                    newRow.createCell(2).setCellValue(score);
                    newRow.createCell(3).setCellValue((System.currentTimeMillis() - startTime) / 1000);

                    FileOutputStream fos = new FileOutputStream(file);
                    workbook.write(fos);
                    workbook.close();
                    fos.close();

                    JOptionPane.showMessageDialog(panel, "Rapor başarıyla kaydedildi.", "Bilgi",
                            JOptionPane.INFORMATION_MESSAGE);

                    // Zamanlayıcıyı durdurma ve sıfırlama
                    timer.stop();
                    timerLabel.setText("Geçen Süre: 0 saniye");

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        return panel;
    }

    // **************************************************************************************************************************************************************************************
    // Rapora içerik panelini oluşturmak için yardımcı metot
    private static JPanel createReportsContentPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(10, 20, 10));

        JLabel titleLabel = new JLabel("Çarpım Tablosu Alıştırması");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);

        JPanel reportPanel = new JPanel(new BorderLayout());
        reportPanel.setBackground(new Color(10, 20, 10));
        panel.add(reportPanel, BorderLayout.CENTER);

        JButton showReportButton = new JButton("Raporu Göster");
        showReportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        showReportButton.setPreferredSize(new Dimension(120, 40));
        showReportButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        reportPanel.add(showReportButton, BorderLayout.CENTER);

        showReportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Workbook workbook1;
                Sheet sheet1;
                File file1 = new File("reports.xlsx");

                if (!file1.exists()) {
                    try (FileOutputStream fileOut = new FileOutputStream(file1)) {
                        workbook1 = new XSSFWorkbook();
                        sheet1 = workbook1.createSheet("Reports");
                        Row headerRow = sheet1.createRow(0);
                        headerRow.createCell(0).setCellValue("Kullanıcı Adı");
                        headerRow.createCell(1).setCellValue("Tarih");
                        headerRow.createCell(2).setCellValue("Puan");
                        headerRow.createCell(3).setCellValue("Geçen Süre");

                        workbook1.write(fileOut);
                        System.out.println("Excel file created successfully.");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

                try {

                    // Read data from the reports.xlsx file
                    FileInputStream file = new FileInputStream("reports.xlsx");
                    Workbook workbook = new XSSFWorkbook(file);
                    Sheet sheet = workbook.getSheetAt(0); // Assuming the data is in the first sheet

                    // Create the table model with column names
                    DefaultTableModel tableModel = new DefaultTableModel();
                    tableModel.addColumn("Kullanıcı Adı");
                    tableModel.addColumn("Tarih");
                    tableModel.addColumn("Puan");
                    tableModel.addColumn("Geçen Süre");

                    // check if the sheet has more than 1 row

                    if (sheet.getLastRowNum() > 0) {
                        // Iterate over the rows and extract the data
                        for (Row row : sheet) {
                            // Assuming the data starts from the second row (index 1)
                            if (row.getRowNum() > 0) {
                                String username = row.getCell(0).getStringCellValue();
                                String date = row.getCell(1).getStringCellValue();
                                double score = row.getCell(2).getNumericCellValue();
                                double elapsedTime = row.getCell(3).getNumericCellValue();

                                // Add the data to the table model
                                tableModel.addRow(new Object[] { username, date, score, elapsedTime });
                            }
                        }

                        // Create the table and add it to a scroll pane
                        JTable table = new JTable(tableModel);
                        JScrollPane scrollPane = new JScrollPane(table);

                        // Show the table in a dialog
                        JOptionPane.showMessageDialog(panel, scrollPane, "Rapor", JOptionPane.PLAIN_MESSAGE);

                    } else {
                        JOptionPane.showMessageDialog(panel, "Rapor dosyası boş.", "Bilgi",
                                JOptionPane.INFORMATION_MESSAGE);
                    }

                    // Close the workbook and file
                    workbook.close();
                    file.close();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panel, "Rapor dosyasını okurken bir hata oluştu.", "Hata",
                            JOptionPane.ERROR_MESSAGE);
                    System.out.println("Error 2.");
                    ex.printStackTrace();
                }
            }
        });

        return panel;
    }

    // **************************************************************************************************************************************************************************************
    // Ayarlar içerik panelini oluşturmak için yardımcı metot
    private static JPanel createSettingsContentPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(10, 20, 10));

        // Panel başlığını oluşturma
        JLabel titleLabel = new JLabel("Lütfen ayarları a, b ve N değerlerini girerek tanımlayın:");
        JLabel exampleLabel = new JLabel("Örnek: 1, 10, 10");
        titleLabel.setForeground(Color.WHITE);
        exampleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 15));
        exampleLabel.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 16));
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(10, 20, 10));
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(exampleLabel, BorderLayout.SOUTH);
        panel.add(titlePanel, BorderLayout.NORTH);

        // Ayarlar panelini oluşturma
        JPanel settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBackground(new Color(10, 20, 10));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);

        // Aralık girişlerini içeren text field'ları oluşturma
        final JTextField aField = new JTextField(10);
        final JTextField bField = new JTextField(10);
        final JTextField nField = new JTextField(10);

        // Ayarları kaydet butonunu oluşturma
        JButton saveSettingsButton = new JButton("Ayarları Kaydet");
        saveSettingsButton.setPreferredSize(new Dimension(200, 40));
        saveSettingsButton.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        // Ayarları kaydet butonunun ActionListener'ını oluşturma
        saveSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Girişleri options.csv dosyasına kaydetme kısmı
                String a = aField.getText();
                String b = bField.getText();
                String n = nField.getText();
                String csvContent = a + "," + b + "," + n;

                try {
                    FileWriter writer = new FileWriter("options.csv");
                    writer.write(csvContent);
                    writer.close();
                    JOptionPane.showMessageDialog(panel, "Ayarlar kaydedildi.", "Başarılı", JOptionPane.PLAIN_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(panel, "Ayarları kaydederken bir hata oluştu.", "Hata",
                            JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        });

        // Text field'lar ve kaydet butonunu ayarlar paneline ekleme
        constraints.gridx = 0;
        constraints.gridy = 0;
        // Beyaz renkte yeni bir JLabel
        settingsPanel.add(new JLabel("a:"), constraints);
        settingsPanel.setBackground(new Color(100, 100, 100));
        constraints.gridx = 1;
        constraints.gridy = 0;
        settingsPanel.add(aField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        settingsPanel.add(new JLabel("b:"), constraints);
        constraints.gridx = 1;
        constraints.gridy = 1;
        settingsPanel.add(bField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        settingsPanel.add(new JLabel("N:"), constraints);
        constraints.gridx = 1;
        constraints.gridy = 2;
        settingsPanel.add(nField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        settingsPanel.add(saveSettingsButton, constraints);

        panel.add(settingsPanel, BorderLayout.CENTER);
        return panel;
    }

    // **************************************************************************************************************************************************************************************
    // İçerik panelini göstermek için yardımcı metot
    private static void showContentPanel(String panelName) {
        CardLayout cardLayout = (CardLayout) contentPanel.getLayout();
        cardLayout.show(contentPanel, panelName);
    }

}
