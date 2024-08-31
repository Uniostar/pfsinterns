public class Book {
    private int id;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(int id, String title, String author, boolean isAvailable) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isAvailable = isAvailable;
    }

    public void setAvailable(boolean available) {
        this.isAvailable = available;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isAvailable() { return isAvailable; }

    // Additional constructor for cases without an ID (e.g., when adding a new book)
    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }
}