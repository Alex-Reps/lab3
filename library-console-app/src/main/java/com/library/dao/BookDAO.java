package com.library.dao;

import com.library.entity.Book;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.library.util.HibernateUtil;

import java.util.List;

public class BookDAO {

    public void saveBook(Book book) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(book);
            transaction.commit();
        }
    }

    public List<Book> getAllBooks() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Book order by id", Book.class).list();
        }
    }

    public List<Book> getAllBookNotReserved() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Book where isReserved = false", Book.class).list();
        }
    }

    public Book getBookById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Book.class, id);
        }
    }

    public void updateBookById(Long id, String author, String title) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Book book = session.get(Book.class, id);
            book.setAuthor(author);
            book.setTitle(title);
            session.saveOrUpdate(book);
            transaction.commit();
        }
    }

    public boolean deleteBookIfNoActiveIssues(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Проверяем наличие активных выдач для книги
            List<?> activeIssues = session.createQuery(
                            "from Issue where book.id = :bookId and returnDate is null")
                    .setParameter("bookId", id)
                    .list();

            if (!activeIssues.isEmpty()) {
                // Есть активные брони, нельзя удалять
                return false;
            }

            Transaction transaction = session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book != null) {
                session.delete(book);
            }
            transaction.commit();
            return true;
        }
    }

    public void deleteBook(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Book book = session.get(Book.class, id);
            if (book != null) {
                session.delete(book);
                transaction.commit();
            }
        }
    }
}
