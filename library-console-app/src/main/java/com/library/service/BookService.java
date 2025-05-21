package com.library.service;

import com.library.dao.BookDAO;
import com.library.entity.Book;
import com.library.dao.IssueDAO;

import java.util.List;

public class BookService {

    private final BookDAO bookDAO;

    public BookService() {
        this.bookDAO = new BookDAO();
    }

    public void addBook(Book book) {
        bookDAO.saveBook(book);
    }

    public boolean deleteBookIfNoActiveIssues(Long id) {return bookDAO.deleteBookIfNoActiveIssues(id);}

    public List<Book> listBooks() {
        return bookDAO.getAllBooks();
    }

    public List<Book> getAllBookNotReserved() {return bookDAO.getAllBookNotReserved();}

    public void updateBook(Book book) {
        bookDAO.saveBook(book); // saveBook использует saveOrUpdate, подходит и для обновлений
    }

    public void updateBookById(Long id, String author, String title) {bookDAO.updateBookById(id, author, title);}

    public Book getBook(Long id) {
        return bookDAO.getBookById(id);
    }

    public void deleteBook(Long id) {

        bookDAO.deleteBook(id);
    }
}
