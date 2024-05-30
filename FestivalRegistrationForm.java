import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;


public class FestivalRegistrationForm extends JFrame {
    private JTextField nameField;
    private JTextField memberNumberField;
    private JComboBox<String> countryComboBox;
    private JRadioButton musicFestivalButton;
    private JRadioButton artFestivalButton;
    private JRadioButton filmFestivalButton;
    private JRadioButton foodFestivalButton;
    private JButton submitButton;
    private JButton clearButton;
    private JTable dataTable;
    private DefaultTableModel tableModel;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/databasegui"; 
    private static final String DB_USER = "root"; 
    private static final String DB_PASSWORD = "123"; 

    public FestivalRegistrationForm() {
        setTitle("Festival Registration Form");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create components
        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(20);

        JLabel memberNumberLabel = new JLabel("Member Number:");
        memberNumberField = new JTextField(20);

        JLabel countryLabel = new JLabel("Country:");
        String[] countries = {"Indonesia", "Dubai", "Germany", "France", "Japan", "Australia", "Brazil", "India", "China", "South Africa"};
        countryComboBox = new JComboBox<>(countries);
        countryComboBox.setMaximumRowCount(5); // To enable scrolling

        JLabel festivalTypeLabel = new JLabel("Festival Type:");
        musicFestivalButton = new JRadioButton("Music Festival");
        artFestivalButton = new JRadioButton("Art Festival");
        filmFestivalButton = new JRadioButton("Film Festival");
        foodFestivalButton = new JRadioButton("Food Festival");

        
        ButtonGroup festivalTypeGroup = new ButtonGroup();
        festivalTypeGroup.add(musicFestivalButton);
        festivalTypeGroup.add(artFestivalButton);
        festivalTypeGroup.add(filmFestivalButton);
        festivalTypeGroup.add(foodFestivalButton);

        submitButton = new JButton("Submit");
        clearButton = new JButton("Clear");

        
        String[] columnNames = {"Name", "Member Number", "Country", "Festival Type"};
        tableModel = new DefaultTableModel(columnNames, 0);
        dataTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(dataTable);

        
        dataTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        dataTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        dataTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        dataTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        dataTable.getColumnModel().getColumn(3).setPreferredWidth(150);

        
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(nameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(memberNumberLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(memberNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(countryLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(countryComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(festivalTypeLabel, gbc);

        JPanel festivalTypePanel = new JPanel();
        festivalTypePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        festivalTypePanel.add(musicFestivalButton);
        festivalTypePanel.add(artFestivalButton);
        festivalTypePanel.add(filmFestivalButton);
        festivalTypePanel.add(foodFestivalButton);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(festivalTypePanel, gbc);

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(submitButton);
        buttonPanel.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        add(scrollPane, gbc);

        
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String memberNumber = memberNumberField.getText();
                String country = (String) countryComboBox.getSelectedItem();
                String festivalType = "";

                if (musicFestivalButton.isSelected()) {
                    festivalType = "Music Festival";
                } else if (artFestivalButton.isSelected()) {
                    festivalType = "Art Festival";
                } else if (filmFestivalButton.isSelected()) {
                    festivalType = "Film Festival";
                } else if (foodFestivalButton.isSelected()) {
                    festivalType = "Food Festival";
                }

                
                tableModel.addRow(new Object[]{name, memberNumber, country, festivalType});

                
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                    String sql = "INSERT INTO festival_registration (name, member_number, country, festival_type) VALUES (?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        pstmt.setString(1, name);
                        pstmt.setString(2, memberNumber);
                        pstmt.setString(3, country);
                        pstmt.setString(4, festivalType);
                        pstmt.executeUpdate();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(FestivalRegistrationForm.this, "Failed to insert data into database!", "Error", JOptionPane.ERROR_MESSAGE);
                }

                
                nameField.setText("");
                memberNumberField.setText("");
                countryComboBox.setSelectedIndex(0);
                festivalTypeGroup.clearSelection();
            }
        });

        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                tableModel.setRowCount(0);
            }
        });
    }

    public static void main(String[] args) {
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to load JDBC driver!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FestivalRegistrationForm().setVisible(true);
            }
        });
    }
}