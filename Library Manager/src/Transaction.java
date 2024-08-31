import java.util.Date;

public class Transaction
{
    private int id;
    private Book book;
    private Patron patron;
    private Date issueDate;
    private Date returnDate;

    // Constructor for issuing a book
    public Transaction(int id, Book book, Patron patron, Date issueDate, Date returnDate)
    {
        this.id = id;
        this.book = book;
        this.patron = patron;
        this.issueDate = issueDate;
        this.returnDate = returnDate;
    }

    public int getId() { return id; }
    public Book getBook() { return book; }
    public Patron getPatron() { return patron; }
    public Date getIssueDate() { return issueDate; }
    public Date getReturnDate() { return returnDate; }

    @Override
    public String toString()
    {
        return STR."Transaction: \{book.getTitle()} issued to \{patron.getName()}";
    }
}
