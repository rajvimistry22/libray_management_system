
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

class Library {
    private Map<Integer, Book> books = new HashMap<>();
    private Map<Integer, Book> issuedBooks = new HashMap<>();
    private Map<Integer, String> issuedBooksHistory = new HashMap<>();

    static class Book {
        String author;
        String title;
        boolean isAvailable;

        Book(String author, String title) {
            this.author = author;
            this.title = title;
            this.isAvailable = true;
        }
    }

    Library() {
        // Add some default books
        addBook(111, "K. Trivedi", "Mathematics");
        addBook(112, "V.K. Jain", "Data Structure");
        addBook(114, "Harsh Bothra", "Java E-Book");
        addBook(115, "N.G. Shivratri", "System Design");
    }

    void addBook(int id, String author, String title) {
        books.put(id, new Book(author, title));
    }

    String listBooks() {
        if (books.isEmpty()) {
            return "No books available.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("NUMBER\t\tTITLE OF BOOK\t\tBOOK ID\t\tAUTHOR NAME\n\n");
        int count = 1;
        for (Map.Entry<Integer, Book> entry : books.entrySet()) {
            Book book = entry.getValue();
            sb.append(count).append(". \t\t")
              .append(book.title).append("\t\t\t")
              .append(entry.getKey()).append("\t\t")
              .append(book.author).append("\n");
            count++;
        }
        return sb.toString();
    }

    String searchBooks(String query) {
        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for (Map.Entry<Integer, Book> entry : books.entrySet()) {
            Book book = entry.getValue();
            if (book.title.toLowerCase().contains(query.toLowerCase()) || 
                book.author.toLowerCase().contains(query.toLowerCase())) {
                sb.append("BOOK ID: ").append(entry.getKey()).append("\n")
                  .append("TITLE: ").append(book.title).append("\n")
                  .append("AUTHOR: ").append(book.author).append("\n\n");
                found = true;
            }
        }
        return found ? sb.toString() : "No matching books found.";
    }

    String issueBook(int id, String userName) {
        Book book = books.get(id);
        if (book != null && book.isAvailable) {
            book.isAvailable = false;
            issuedBooks.put(id, book);
            issuedBooksHistory.put(id, userName);
            return "Book issued successfully.";
        } else {
            return book == null ? "Book not found." : "Book already issued.";
        }
    }

    String returnBook(int id) {
        Book book = issuedBooks.get(id);
        if (book != null) {
            book.isAvailable = true;
            issuedBooks.remove(id);
            issuedBooksHistory.remove(id);
            return "Book returned successfully.";
        } else {
            return "Book not found in issued books.";
        }
    }

    String getIssuedBooksHistory() {
        if (issuedBooksHistory.isEmpty()) {
            return "No books have been issued yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, String> entry : issuedBooksHistory.entrySet()) {
            Book book = books.get(entry.getKey());
            sb.append("BOOK ID: ").append(entry.getKey()).append("\n")
              .append("TITLE: ").append(book.title).append("\n")
              .append("ISSUED TO: ").append(entry.getValue()).append("\n\n");
        }
        return sb.toString();
    }

    String getBookDetails(int id) {
        Book book = books.get(id);
        if (book != null) {
            return "BOOK ID: " + id + "\nTITLE: " + book.title + "\nAUTHOR: " + book.author + 
                   "\nSTATUS: " + (book.isAvailable ? "Available" : "Issued");
        } else {
            return "Book not found.";
        }
    }
}

public class LibraryManagementUI extends JFrame {
    private Library library;
    private JTextArea textArea;
    private JTextField bookIdField, authorField, titleField, searchField, userNameField;
    private JButton addButton, listButton, issueButton, returnButton, searchButton, detailsButton, historyButton;

    public LibraryManagementUI() {
        library = new Library();

        // Set up the JFrame
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set a background image
        JLabel background = new JLabel(new ImageIcon("background.jpg"));
        setContentPane(background);
        background.setLayout(new BorderLayout());

        // Create UI components
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridBagLayout());
        inputPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Book ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Book ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        bookIdField = new JTextField(15);
        inputPanel.add(bookIdField, gbc);

        // Author Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Author Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        authorField = new JTextField(15);
        inputPanel.add(authorField, gbc);

        // Title
        gbc.gridx = 0;
        gbc.gridy = 2;
        inputPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        titleField = new JTextField(15);
        inputPanel.add(titleField, gbc);

        // Search
        gbc.gridx = 0;
        gbc.gridy = 3;
        inputPanel.add(new JLabel("Search:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        searchField = new JTextField(15);
        inputPanel.add(searchField, gbc);

        // User Name (for issuing)
        gbc.gridx = 0;
        gbc.gridy = 4;
        inputPanel.add(new JLabel("User Name:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        userNameField = new JTextField(15);
        inputPanel.add(userNameField, gbc);

        // Buttons
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout());

        addButton = new JButton("Add Book");
        listButton = new JButton("List Books");
        issueButton = new JButton("Issue Book");
        returnButton = new JButton("Return Book");
        searchButton = new JButton("Search Books");
        detailsButton = new JButton("Book Details");
        historyButton = new JButton("Issue History");

        buttonPanel.add(addButton);
        buttonPanel.add(listButton);
        buttonPanel.add(issueButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(detailsButton);
        buttonPanel.add(historyButton);

        inputPanel.add(buttonPanel, gbc);

        // Text area for results
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        background.add(inputPanel, BorderLayout.NORTH);
        background.add(scrollPane, BorderLayout.CENTER);

        // Add action listeners
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(bookIdField.getText());
                    String author = authorField.getText();
                    String title = titleField.getText();
                    library.addBook(id, author, title);
                    textArea.setText("Book added successfully.");
                    clearFields();
                } catch (NumberFormatException ex) {
                    textArea.setText("Invalid book ID. Please enter a valid number.");
                }
            }
        });

        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(library.listBooks());
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(library.searchBooks(searchField.getText()));
            }
        });

        issueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(bookIdField.getText());
                    String userName = userNameField.getText();
                    textArea.setText(library.issueBook(id, userName));
                    clearFields();
                } catch (NumberFormatException ex) {
                    textArea.setText("Invalid book ID. Please enter a valid number.");
                }
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(bookIdField.getText());
                    textArea.setText(library.returnBook(id));
                    clearFields();
                } catch (NumberFormatException ex) {
                    textArea.setText("Invalid book ID. Please enter a valid number.");
                }
            }
        });

        detailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(bookIdField.getText());
                    textArea.setText(library.getBookDetails(id));
                } catch (NumberFormatException ex) {
                    textArea.setText("Invalid book ID. Please enter a valid number.");
                }
            }
        });

        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(library.getIssuedBooksHistory());
            }
        });
    }

    private void clearFields() {
        bookIdField.setText("");
        authorField.setText("");
        titleField.setText("");
        searchField.setText("");
        userNameField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryManagementUI ui = new LibraryManagementUI();
            ui.setVisible(true);
        });
    }
}
