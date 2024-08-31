import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class EmailHandler {
    public ArrayList<Email> list;
    private Session session;
    private Store store;

    public void updateList() throws MessagingException, IOException
    {
        list = fetchEmails();
    }

    public ArrayList<Email> getList() {
        return list;
    }

    public void connect(String host, String user, String password) throws MessagingException, IOException {
        Properties props = new Properties();
        props.put("mail.store.protocol", "imaps");
        props.put("mail.imap.host", host);
        props.put("mail.imap.port", "993");
        props.put("mail.imaps.ssl.enable", "true");

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(user, password);
            }
        });

        store = session.getStore("imaps");
        store.connect(host, user, password);
    }

    public ArrayList<Email> fetchEmails() throws MessagingException, IOException {
        ArrayList<Email> emailList = new ArrayList<>();
        Folder inbox = store.getFolder("INBOX");
        inbox.open(Folder.READ_ONLY);

        Message[] messages = inbox.getMessages();

        for (int i = 0; i < 50; i++)  {
            Message message = messages[i];
            Email email = new Email(
                    InternetAddress.toString(message.getFrom()),
                    message.getSubject(),
                    message.getContent().toString()
            );
            emailList.add(email);
        }

        inbox.close(false);

        return emailList;
    }

    public void sendEmail(String user, String password, String to, String subject, String body) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // Change to your SMTP server
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }
}