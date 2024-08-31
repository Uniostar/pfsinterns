public class Email {
    private final String sender;
    private final String subject;
    private final String body;

    public Email(String sender, String subject, String body) {
        this.sender = sender;
        this.subject = subject;
        this.body = body;
    }

    @Override
    public String toString() {
        return STR."From: \{sender}\nSubject: \{subject}\n\n\{body}";
    }
}
