import java.sql.*;

public class DatabaseManager {
    private Connection connection;
    private String dbUrl, user, password;
    public DatabaseManager(String dbUrl, String user, String password) throws SQLException {
        this.connection = DriverManager.getConnection(dbUrl, user, password);
        this.dbUrl = dbUrl;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, user, password);
    }

    public Book getBook(int id) throws SQLException {
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Book(rs.getInt("id"), rs.getString("title"), rs.getString("author"), rs.getBoolean("is_available"));
            }
        }
        return null;
    }

    public Patron getPatron(int id) throws SQLException {
        connection = getConnection();
        String sql = "SELECT * FROM patrons WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Patron(rs.getInt("id"), rs.getString("name"), rs.getString("email"));
            }
        }
        closeConnection(connection);
        return null;
    }

    public void deletePatron(int id) throws SQLException {
        connection = getConnection();
        String sql = "DELETE FROM patrons WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        closeConnection(connection);
    }

    public void closeConnection(Connection connection) throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
