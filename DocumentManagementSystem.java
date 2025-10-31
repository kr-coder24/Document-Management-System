import java.awt.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class DocumentManagementSystem extends JFrame {
    private JButton uploadButton, deleteButton, viewButton, editButton;
    private JList<String> documentList;
    private JPanel mainPanel, buttonPanel;
    private File documentsFolder;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu, adminMenu;
    private JMenuItem uploadMenuItem, deleteMenuItem, exitMenuItem, deleteUserMenuItem;

    public DocumentManagementSystem() {
        setTitle("Enterprise Document Management System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setIconImage(new ImageIcon("icon.png").getImage());

        documentsFolder = new File("documents");
        if (!documentsFolder.exists()) {
            documentsFolder.mkdirs();
        }

        createMenuBar();
        createMainPanel();
        loadDocumentList();
        add(mainPanel, BorderLayout.CENTER);
    }

    private void createMenuBar() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("File");
        uploadMenuItem = new JMenuItem("Upload Document");
        deleteMenuItem = new JMenuItem("Delete Document");
        exitMenuItem = new JMenuItem("Exit");
        uploadMenuItem.addActionListener(e -> uploadDocument());
        deleteMenuItem.addActionListener(e -> deleteDocument());
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(uploadMenuItem);
        fileMenu.add(deleteMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);

        editMenu = new JMenu("Edit");

        adminMenu = new JMenu("Admin");
        deleteUserMenuItem = new JMenuItem("Delete User");
        deleteUserMenuItem.addActionListener(e -> deleteUser());
        adminMenu.add(deleteUserMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(adminMenu);
        setJMenuBar(menuBar);
    }

    private void createMainPanel() {
        mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        documentList = new JList<>();
        documentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        documentList.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane documentListScrollPane = new JScrollPane(documentList);
        documentListScrollPane.setPreferredSize(new Dimension(400, 600));

        uploadButton = createButton("Upload", "upload_icon.png", "Upload a new document");
        deleteButton = createButton("Delete", "delete_icon.png", "Delete an existing document");
        viewButton = createButton("View", "view_icon.png", "View a selected document");
        editButton = createButton("Edit", "edit_icon.png", "Edit a selected document");

        buttonPanel = new JPanel(new GridLayout(1, 4, 20, 0));
        buttonPanel.add(uploadButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(editButton);

        mainPanel.add(documentListScrollPane, BorderLayout.WEST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, String iconPath, String toolTip) {
        JButton button = new JButton(text, new ImageIcon(iconPath));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setToolTipText(toolTip);
        button.addActionListener(e -> {
            switch (text) {
                case "Upload":
                    uploadDocument();
                    break;
                case "Delete":
                    deleteDocument();
                    break;
                case "View":
                    viewDocument();
                    break;
                case "Edit":
                    editDocument();
                    break;
            }
        });
        return button;
    }

    private void loadDocumentList() {
        File[] files = documentsFolder.listFiles((dir, name) -> name.endsWith(".meta"));
        java.util.List<String> documentNames = new java.util.ArrayList<>();

        if (files != null && files.length > 0) {
            for (File file : files) {
                Map<String, String> metadata = readMetadata(file);
                String documentName = metadata.get("filename");
                String category = metadata.get("category");
                String tags = metadata.get("tags");
                documentNames.add(String.format("%s (Category: %s, Tags: %s)", documentName, category, tags));
            }
        } else {
            documentNames.add("No documents found.");
        }
        documentList.setListData(documentNames.toArray(new String[0]));
    }

    private void uploadDocument() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select a Document to Upload");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            File destination = new File(documentsFolder, selectedFile.getName());
            try (InputStream in = new FileInputStream(selectedFile); OutputStream out = new FileOutputStream(destination)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }

                Map<String, String> metadata = new HashMap<>();
                metadata.put("filename", selectedFile.getName());
                metadata.put("category", JOptionPane.showInputDialog(this, "Enter the document category:"));
                metadata.put("topic", JOptionPane.showInputDialog(this, "Enter the document topic:"));
                metadata.put("tags", JOptionPane.showInputDialog(this, "Enter the document tags (comma-separated):"));
                saveMetadata(new File(documentsFolder, selectedFile.getName() + ".meta"), metadata);
                loadDocumentList();
                JOptionPane.showMessageDialog(this, "Document uploaded successfully.");
            } catch (IOException e) {
                showErrorMessage("Error uploading document: " + e.getMessage());
            }
        }
    }

    private void viewDocument() {
        int selectedIndex = documentList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String documentName = (String) documentList.getSelectedValue();
            int categoryIndex = documentName.indexOf("(Category:");
            int tagsIndex = documentName.indexOf("Tags:");
            String fileName = documentName.substring(0, categoryIndex).trim();
            String category = documentName.substring(categoryIndex + 10, tagsIndex - 1).trim();
            String tags = documentName.substring(tagsIndex + 6).trim();

            File documentFile = new File(documentsFolder, fileName);
            File metadataFile = new File(documentsFolder, fileName + ".meta");
            if (documentFile.exists() && metadataFile.exists()) {
                Map<String, String> metadata = readMetadata(metadataFile);
                String content = readFileContent(documentFile);

                // Create the metadata table
                String[] columnNames = {"Property", "Value"};
                String[][] data = {
                        {"Document", metadata.get("filename")},
                        {"Category", category},
                        {"Topic", metadata.get("topic")},
                        {"Tags", tags}
                };

                JTable metadataTable = new JTable(new DefaultTableModel(data, columnNames) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
                metadataTable.setFont(new Font("Arial", Font.PLAIN, 14));
                metadataTable.setRowHeight(24);
                JScrollPane metadataScrollPane = new JScrollPane(metadataTable);

                // Create the document content text area
                JTextArea textArea = new JTextArea(content);
                textArea.setEditable(false);
                textArea.setFont(new Font("Arial", Font.PLAIN, 14));
                JScrollPane contentScrollPane = new JScrollPane(textArea);

                // Create the main panel and add components
                JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                mainPanel.add(metadataScrollPane, BorderLayout.NORTH);
                mainPanel.add(contentScrollPane, BorderLayout.CENTER);

                // Create and show the dialog
                JDialog viewDialog = new JDialog(this, "View Document", true);
                viewDialog.setSize(800, 600);
                viewDialog.add(mainPanel);
                viewDialog.setLocationRelativeTo(this);
                viewDialog.setVisible(true);
            } else {
                showErrorMessage("Document or metadata file not found.");
            }
        } else {
            showErrorMessage("No document selected.");
        }
    }

    private void deleteDocument() {
        int selectedIndex = documentList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String documentName = (String) documentList.getSelectedValue();
            int categoryIndex = documentName.indexOf("(Category:");
            String fileName = documentName.substring(0, categoryIndex).trim();
            File documentFile = new File(documentsFolder, fileName);
            File metadataFile = new File(documentsFolder, fileName + ".meta");
            if (documentFile.exists() && metadataFile.exists()) {
                int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this document?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    if (documentFile.delete() && metadataFile.delete()) {
                        loadDocumentList();
                        JOptionPane.showMessageDialog(this, "Document deleted successfully.");
                    } else {
                        showErrorMessage("Error deleting document.");
                    }
                }
            } else {
                showErrorMessage("Document or metadata file not found.");
            }
        } else {
            showErrorMessage("No document selected.");
        }
    }

    private void editDocument() {
        int selectedIndex = documentList.getSelectedIndex();
        if (selectedIndex >= 0) {
            String documentName = (String) documentList.getSelectedValue();
            int categoryIndex = documentName.indexOf("(Category:");
            String fileName = documentName.substring(0, categoryIndex).trim();
            File documentFile = new File(documentsFolder, fileName);
            File metadataFile = new File(documentsFolder, fileName + ".meta");
            if (documentFile.exists() && metadataFile.exists()) {
                Map<String, String> metadata = readMetadata(metadataFile);
                String content = readFileContent(documentFile);

                // Metadata editing table
                String[] columnNames = {"Property", "Value"};
                String[][] data = {
                        {"Category", metadata.get("category")},
                        {"Topic", metadata.get("topic")},
                        {"Tags", metadata.get("tags")}
                };

                DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
                JTable metadataTable = new JTable(tableModel);
                metadataTable.setFont(new Font("Arial", Font.PLAIN, 14));
                metadataTable.setRowHeight(24);
                JScrollPane metadataScrollPane = new JScrollPane(metadataTable);

                // Document content text area
                JTextArea contentArea = new JTextArea(content);
                contentArea.setFont(new Font("Arial", Font.PLAIN, 14));
                JScrollPane contentScrollPane = new JScrollPane(contentArea);

                // Main panel with metadata table and content area
                JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
                mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                mainPanel.add(metadataScrollPane, BorderLayout.NORTH);
                mainPanel.add(contentScrollPane, BorderLayout.CENTER);

                // Show confirm dialog with the edit panel
                int result = JOptionPane.showConfirmDialog(this, mainPanel, "Edit Document", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter(documentFile))) {
                        writer.write(contentArea.getText());
                    } catch (IOException e) {
                        showErrorMessage("Error saving document: " + e.getMessage());
                        return;
                    }

                    // Update metadata from the table
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        String key = (String) tableModel.getValueAt(i, 0);
                        String value = (String) tableModel.getValueAt(i, 1);
                        metadata.put(key.toLowerCase(), value);
                    }

                    saveMetadata(metadataFile, metadata);
                    loadDocumentList();
                    JOptionPane.showMessageDialog(this, "Document edited successfully.");
                }
            } else {
                showErrorMessage("Document or metadata file not found.");
            }
        } else {
            showErrorMessage("No document selected.");
        }
    }

    private void deleteUser() {
        String username = JOptionPane.showInputDialog(this, "Enter the username of the user to delete:");
        if (username != null && !username.isEmpty()) {
            // Implement user deletion logic here
            JOptionPane.showMessageDialog(this, "User deleted successfully (placeholder).");
        }
    }

    private Map<String, String> readMetadata(File metadataFile) {
        Map<String, String> metadata = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(metadataFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    metadata.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            showErrorMessage("Error reading metadata: " + e.getMessage());
        }
        return metadata;
    }

    private void saveMetadata(File metadataFile, Map<String, String> metadata) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(metadataFile))) {
            for (Map.Entry<String, String> entry : metadata.entrySet()) {
                writer.write(entry.getKey() + ": " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            showErrorMessage("Error saving metadata: " + e.getMessage());
        }
    }

    private String readFileContent(File file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            showErrorMessage("Error reading document: " + e.getMessage());
        }
        return content.toString();
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DocumentManagementSystem().setVisible(true));
    }
}