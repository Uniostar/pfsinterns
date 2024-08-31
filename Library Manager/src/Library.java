import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Library
{
    private DatabaseManager dbManager;

    public Library(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void addBook(String title, String author) throws SQLException {
        String query = "INSERT INTO books (title, author) VALUES (?, ?)";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(int bookId, String title, String author) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "UPDATE books SET title = ?, author = ? WHERE id = ?");
        stmt.setString(1, title);
        stmt.setString(2, author);
        stmt.setInt(3, bookId);
        stmt.executeUpdate();
        connection.close();
    }

    public void deleteBook(int bookId) throws SQLException {
        String sql = "DELETE FROM books WHERE id = ?";
        Connection connection = dbManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, bookId);
        stmt.executeUpdate();
        connection.close();
    }

    public void addPatron(String name, String email) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO patrons (name, email) VALUES (?, ?)");
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.executeUpdate();
        connection.close();
    }

    public void updatePatron(int id, String name, String email) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "UPDATE patrons SET name = ?, email = ? WHERE id = ?");
        stmt.setString(1, name);
        stmt.setString(2, email);
        stmt.setInt(3, id);
        stmt.executeUpdate();
        connection.close();
    }

    public void removePatron(int id) throws SQLException {
        Connection connection = dbManager.getConnection();
        PreparedStatement stmt = connection.prepareStatement(
                "DELETE FROM patrons WHERE id = ?");
        stmt.setInt(1, id);
        stmt.executeUpdate();
        connection.close();
    }

    public void returnBook(int bookId, int userId) throws SQLException {
        Connection connection = dbManager.getConnection();
        String insertReturnTransaction = "UPDATE transactions SET return_date = NOW() WHERE book_id = ? AND patron_id = ?";
        String updateAvailability = "UPDATE books SET is_available = TRUE WHERE id = ?";

        try (PreparedStatement stmtTransaction = connection.prepareStatement(insertReturnTransaction);
             PreparedStatement stmtUpdate = connection.prepareStatement(updateAvailability)) {

            stmtTransaction.setInt(1, bookId);
            stmtTransaction.setInt(2, userId);
            stmtTransaction.executeUpdate();

            stmtUpdate.setInt(1, bookId);
            stmtUpdate.executeUpdate();
        }

        connection.close();
    }

    public void issueBook(int bookId, int userId) throws SQLException {
        Connection connection = dbManager.getConnection();
        String insertTransaction = "INSERT INTO transactions (book_id, patron_id, issue_date) VALUES (?, ?, NOW())";
        String updateAvailability = "UPDATE books SET is_available = FALSE WHERE id = ?";

        try (PreparedStatement stmtTransaction = connection.prepareStatement(insertTransaction);
             PreparedStatement stmtUpdate = connection.prepareStatement(updateAvailability)) {

            stmtTransaction.setInt(1, bookId);
            stmtTransaction.setInt(2, userId);
            stmtTransaction.executeUpdate();

            stmtUpdate.setInt(1, bookId);
            stmtUpdate.executeUpdate();
        }

        connection.close();
    }

    public ResultSet getAvailableBooks() throws SQLException {
        Connection connection = dbManager.getConnection();
        Statement stmt = connection.createStatement();
        String query = "SELECT id, title, author FROM books WHERE is_available = TRUE";
        return stmt.executeQuery(query);
    }

    public ResultSet getPatrons() throws SQLException {
        Connection connection = dbManager.getConnection();
        Statement stmt = connection.createStatement();
        return stmt.executeQuery("SELECT * FROM patrons");
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";

        try (Connection connection = dbManager.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int transactionId = rs.getInt("id");
                int bookId = rs.getInt("book_id");
                int patronId = rs.getInt("patron_id");
                Timestamp issueDate = rs.getTimestamp("issue_date");
                Timestamp returnDate = rs.getTimestamp("return_date");

                transactions.add(new Transaction(transactionId, findBook(bookId), findPatron(patronId), issueDate, returnDate));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return transactions;
    }

    public Book findBook(int id) throws SQLException {
        return dbManager.getBook(id);
    }

    public Patron findPatron(int id) throws SQLException {
        return dbManager.getPatron(id);
    }

    public void printTransactionInfo() {
        String fileName = "src/TransactionReport.txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            Connection connection = dbManager.getConnection();
            Statement stmt = connection.createStatement();
            String query = "SELECT * FROM transactions";
            ResultSet rs = stmt.executeQuery(query);

            writer.write("Transaction Report");
            writer.newLine();
            writer.write("=================");
            writer.newLine();
            writer.newLine();

            while (rs.next()) {
                int transactionId = rs.getInt("id");
                int bookId = rs.getInt("book_id");
                int memberId = rs.getInt("patron_id");
                String transactionDate = rs.getString("issue_date");
                String returnDate = rs.getString("return_date");

                writer.write(STR."Transaction ID: \{transactionId}");
                writer.newLine();
                writer.write(STR."Book ID: \{bookId}");
                writer.newLine();
                writer.write(STR."Member ID: \{memberId}");
                writer.newLine();
                writer.write(STR."Transaction Date: \{transactionDate}");
                writer.newLine();
                writer.write(STR."Return Date: \{returnDate != null ? returnDate : "Not Returned"}");
                writer.newLine();
                writer.write("-----------------------------");
                writer.newLine();
            }

            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
