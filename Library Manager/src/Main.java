import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try {
            //Add your own MYSQL root name and password
            //Run mysql.txt on MYSQl workspace to start
            DatabaseManager databaseManager = new DatabaseManager("jdbc:mysql://localhost:3306/library_db", "root", "password");
            Library library = new Library(databaseManager);

            // Setup GUI
            SwingUtilities.invokeLater(() -> {
                LibraryGUI gui = new LibraryGUI(library);
                gui.setVisible(true);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
