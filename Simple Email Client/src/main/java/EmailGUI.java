import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class EmailGUI {
    private EmailHandler emailHandler;
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextArea emailArea;
    private JTextField recipientField;
    private JTextField subjectField;
    private JTextArea bodyArea;

    public EmailGUI(EmailHandler emailHandler) {
        this.emailHandler = emailHandler;
    }
    public void showGUI()
    {
        loginPage();
    }
    private void loginPage()
    {
        frame = new JFrame("Login Page");
        frame.setSize(400, 300);
        JPanel loginPanel = new JPanel(new GridLayout(3, 2));
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginActionListener());

        loginPanel.add(new JLabel("Email:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel(""));
        loginPanel.add(loginButton);

        frame.add(loginPanel);
        frame.setVisible(true);
    }
    private void afterLoginPage()
    {
        JFrame frame = new JFrame("Email Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JTabbedPane tabbedPane = new JTabbedPane();

        readEmailTab(tabbedPane);
        sendEmailTab(tabbedPane);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }
    private void readEmailTab(JTabbedPane tabbedPane)
    {
        JPanel inboxPanel = new JPanel(new BorderLayout());
        emailArea = new JTextArea();
        emailArea.setEditable(false);
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new RefreshActionListener());

        inboxPanel.add(new JScrollPane(emailArea), BorderLayout.CENTER);
        inboxPanel.add(refreshButton, BorderLayout.SOUTH);

        tabbedPane.add("Inbox", inboxPanel);
    }
    private void sendEmailTab(JTabbedPane tabbedPane)
    {
        JPanel sendPanel = new JPanel(new GridLayout(4, 2));
        recipientField = new JTextField();
        subjectField = new JTextField();
        bodyArea = new JTextArea();
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendActionListener());

        sendPanel.add(new JLabel("To:"));
        sendPanel.add(recipientField);
        sendPanel.add(new JLabel("Subject:"));
        sendPanel.add(subjectField);
        sendPanel.add(new JLabel("Body:"));
        sendPanel.add(new JScrollPane(bodyArea));
        sendPanel.add(new JLabel(""));
        sendPanel.add(sendButton);

        tabbedPane.add("Send Email", sendPanel);
    }

    private class LoginActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try
            {
                String user = usernameField.getText();
                String password = new String(passwordField.getPassword());

                emailHandler.connect("imap.gmail.com", user, password);

                JOptionPane.showMessageDialog(frame, "Login successful!");

                frame.setVisible(false);

                afterLoginPage();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, STR."Login failed: \{ex.getMessage()}", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private class RefreshActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                emailHandler.updateList();
                ArrayList<Email> emails = emailHandler.getList();
                emailArea.setText("");
                for (Email email : emails) {
                    emailArea.append(STR."\{email.toString()}\n\n");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, STR."Failed to fetch emails: \{ex.getMessage()}", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    private class SendActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String from = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String to = recipientField.getText();
                String subject = subjectField.getText();
                String body = bodyArea.getText();
                emailHandler.sendEmail(from, password, to, subject, body);
                JOptionPane.showMessageDialog(frame, "Email sent successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, STR."Failed to send email: \{ex.getMessage()}", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
