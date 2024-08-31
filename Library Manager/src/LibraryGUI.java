import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.List;
import java.util.Vector;
import java.sql.SQLException;

public class LibraryGUI extends JFrame {
    private JTabbedPane tabbedPane;
    private JTable transactionsTable;
    private JTable bookTable;
    private JTable patronTable;
    private Library library;

    public LibraryGUI(Library library) {
        this.library = library;
        setTitle("Library Management System");
        setSize(1200, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        createAddBookTab();
        createAvailableBooksTab();
        createUpdateBookTab();
        createDeleteBookTab();

        createAddPatronTab();
        createPatronsListTab();
        createUpdatePatronTab();
        createRemovePatronTab();

        createIssueBookTab();
        createReturnBookTab();
        createTransactionsTab();

        refreshPatronsTable();
        refreshTransactionsTable();
        refreshAvailableBooksTable();
    }

    private void createAddBookTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel titleLabel = new JLabel("Title:");
        JTextField titleField = new JTextField();
        JLabel authorLabel = new JLabel("Author:");
        JTextField authorField = new JTextField();
        JButton addButton = new JButton("Add Book");

        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(authorLabel);
        panel.add(authorField);
        panel.add(new JLabel()); // Empty cell
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook(titleField.getText(), authorField.getText());
                refreshAvailableBooksTable();
            }
        });

        tabbedPane.addTab("Add Book", panel);
    }

    private void createDeleteBookTab()
    {
        // Create delete book tab
        JPanel deleteBookPanel = new JPanel();
        deleteBookPanel.setLayout(new GridLayout(3, 2));

        deleteBookPanel.add(new JLabel("Book ID to Delete:"));
        JTextField bookIdField = new JTextField();
        deleteBookPanel.add(bookIdField);

        JButton deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int bookId = Integer.parseInt(bookIdField.getText());
                    library.deleteBook(bookId);
                    refreshAvailableBooksTable();
                    JOptionPane.showMessageDialog(LibraryGUI.this, "Book deleted successfully.");
                } catch (NumberFormatException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LibraryGUI.this, "Failed to delete book.");
                }
            }
        });

        deleteBookPanel.add(deleteButton);

        tabbedPane.addTab("Delete Books", deleteBookPanel);
    }

    private void createUpdateBookTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel idLabel = new JLabel("ID to Change:");
        JTextField idField = new JTextField();
        JLabel titleLabel = new JLabel("New Title:");
        JTextField titleField = new JTextField();
        JLabel authorLabel = new JLabel("New Author:");
        JTextField authorField = new JTextField();
        JButton addButton = new JButton("Update Book");

        panel.add(idLabel);
        panel.add(idField);
        panel.add(titleLabel);
        panel.add(titleField);
        panel.add(authorLabel);
        panel.add(authorField);
        panel.add(new JLabel());
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    library.updateBook(Integer.parseInt(idField.getText()), titleField.getText(), authorField.getText());
                    JOptionPane.showMessageDialog(LibraryGUI.this, "Updated book successfully!");
                    refreshAvailableBooksTable();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        tabbedPane.addTab("Update Book", panel);
    }

    private void createAddPatronTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("Email:");
        JTextField emailField = new JTextField();
        JButton addPatronButton = new JButton("Add Patron");

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(new JLabel());
        panel.add(addPatronButton);

        addPatronButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPatron(nameField.getText(), emailField.getText());
                refreshPatronsTable();
            }
        });

        tabbedPane.addTab("Add Patrons", panel);
    }

    private void createRemovePatronTab()
    {
        // Create delete book tab
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        panel.add(new JLabel("Patron ID to Remove:"));
        JTextField patronIDField = new JTextField();
        panel.add(patronIDField);

        JButton deleteButton = new JButton("Remove Patron");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int patronId = Integer.parseInt(patronIDField.getText());
                    library.removePatron(patronId);
                    refreshPatronsTable();
                    JOptionPane.showMessageDialog(LibraryGUI.this, "Patron removed successfully.");
                } catch (NumberFormatException | SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(LibraryGUI.this, "Failed to remove patron.");
                }
            }
        });

        panel.add(deleteButton);

        tabbedPane.addTab("Remove Patron", panel);
    }

    private void createUpdatePatronTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        JLabel idLabel = new JLabel("ID to Change:");
        JTextField idField = new JTextField();
        JLabel nameLabel = new JLabel("New Name:");
        JTextField nameField = new JTextField();
        JLabel emailLabel = new JLabel("New Email:");
        JTextField emailField = new JTextField();
        JButton addButton = new JButton("Update Patron");

        panel.add(idLabel);
        panel.add(idField);
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(new JLabel());
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    library.updatePatron(Integer.parseInt(idField.getText()), nameField.getText(), emailField.getText());
                    JOptionPane.showMessageDialog(LibraryGUI.this, "Updated patron successfully!");
                    refreshPatronsTable();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        tabbedPane.addTab("Update Patron", panel);
    }

    private void createIssueBookTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel bookIdLabel = new JLabel("Book ID:");
        JTextField bookIdField = new JTextField();
        JLabel patronIdLabel = new JLabel("Patron ID:");
        JTextField patronIdField = new JTextField();
        JButton issueButton = new JButton("Issue Book");

        panel.add(bookIdLabel);
        panel.add(bookIdField);
        panel.add(patronIdLabel);
        panel.add(patronIdField);
        panel.add(new JLabel()); // Empty cell
        panel.add(issueButton);

        issueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                issueBook(bookIdField.getText(), patronIdField.getText());
                refreshTransactionsTable();
            }
        });

        tabbedPane.addTab("Issue Books", panel);
    }

    private void createAvailableBooksTab() {
        JPanel panel = new JPanel(new BorderLayout());

        bookTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(bookTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        panel.add(refreshButton, BorderLayout.SOUTH);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshAvailableBooksTable();
            }
        });

        tabbedPane.addTab("Available Books", panel);
        refreshAvailableBooksTable();
    }

    private void createPatronsListTab() {
        JPanel panel = new JPanel(new BorderLayout());

        patronTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(patronTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        panel.add(refreshButton, BorderLayout.SOUTH);

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshPatronsTable();
            }
        });

        tabbedPane.addTab("Patrons List", panel);
        refreshPatronsTable();
    }

    private void createTransactionsTab() {
        JPanel transactionsPanel = new JPanel(new BorderLayout());

        transactionsTable = new JTable(new DefaultTableModel(
                new Object[]{"Transaction ID", "Book ID", "Patron ID", "Issue Date", "Return Date"}, 0
        ));
        JScrollPane scrollPane = new JScrollPane(transactionsTable);
        transactionsPanel.add(scrollPane, BorderLayout.CENTER);

        JButton printButton = new JButton("Print as TXT");
        transactionsPanel.add(printButton, BorderLayout.SOUTH);

        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                library.printTransactionInfo();
                JOptionPane.showMessageDialog(LibraryGUI.this, "Printed successfully!");
            }
        });

        tabbedPane.addTab("Transactions List", transactionsPanel);
        refreshTransactionsTable();
    }

    private void createReturnBookTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel bookIdLabel = new JLabel("Book ID:");
        JTextField bookIdField = new JTextField();
        JLabel patronIdLabel = new JLabel("Patron ID:");
        JTextField patronIdField = new JTextField();
        JButton issueButton = new JButton("Return Book");

        panel.add(bookIdLabel);
        panel.add(bookIdField);
        panel.add(patronIdLabel);
        panel.add(patronIdField);
        panel.add(new JLabel()); // Empty cell
        panel.add(issueButton);

        issueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                returnBook(bookIdField.getText(), patronIdField.getText());
                refreshTransactionsTable();
            }
        });

        tabbedPane.addTab("Return Books", panel);
    }

    private void addBook(String title, String author) {
        try {
            library.addBook(title, author);
            JOptionPane.showMessageDialog(this, "Book added successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid year format.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add book.");
        }
    }

    private void addPatron(String name, String email) {
        try {
            library.addPatron(name, email);
            JOptionPane.showMessageDialog(this, "Patron added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to add patron.");
        }
    }

    private void issueBook(String bookId, String patronId) {
        try {
            library.issueBook(Integer.parseInt(bookId), Integer.parseInt(patronId));
            JOptionPane.showMessageDialog(this, "Book issued successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to issue book.");
        }
    }

    private void returnBook(String bookId, String patronId) {
        try {
            library.returnBook(Integer.parseInt(bookId), Integer.parseInt(patronId));
            JOptionPane.showMessageDialog(this, "Book returned successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid ID format.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to return book.");
        }
    }

    private void refreshAvailableBooksTable() {
        try {
            ResultSet rs = library.getAvailableBooks();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            DefaultTableModel model = new DefaultTableModel();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            model.setColumnIdentifiers(columnNames);

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }
            model.setDataVector(data, columnNames);
            bookTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void refreshTransactionsTable() {
        List<Transaction> transactions = library.getAllTransactions();
        DefaultTableModel model = (DefaultTableModel) transactionsTable.getModel();
        model.setRowCount(0); // Clear existing data

        for (Transaction transaction : transactions) {
            model.addRow(new Object[]{
                    transaction.getId(),
                    transaction.getBook(),
                    transaction.getPatron(),
                    transaction.getIssueDate(),
                    transaction.getReturnDate()
            });
        }
    }

    private void refreshPatronsTable() {
        try {
            ResultSet rs = library.getPatrons();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            DefaultTableModel model = new DefaultTableModel();
            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            model.setColumnIdentifiers(columnNames);

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.add(rs.getObject(i));
                }
                data.add(row);
            }
            model.setDataVector(data, columnNames);
            patronTable.setModel(model);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
