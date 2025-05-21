package com.library.dao;

import com.library.entity.Issue;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.library.util.HibernateUtil;

import java.util.List;
public class IssueDAO {

    public void saveIssue(Issue issue) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(issue);
            transaction.commit();
        }
    }

    public List<Issue> getAllIssues() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Issue", Issue.class).list();
        }
    }

    public void deleteIssue(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Issue issue = session.get(Issue.class, id);
            if (issue != null) {
                session.delete(issue);
                transaction.commit();
            }
        }
    }

    public List<Issue> getActiveIssuesByUser(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "from Issue where user.id = :userId and returnDate is null", Issue.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public void deleteIssuesByUserId(Long userId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();

            // Загружаем все активные и завершённые брони пользователя
            List<Issue> issues = session.createQuery(
                            "FROM Issue WHERE user.id = :userId", Issue.class)
                    .setParameter("userId", userId)
                    .list();

            // Удаляем по одной
            for (Issue issue : issues) {
                session.remove(issue);
            }

            tx.commit();
        }
    }

    public List<Issue> getAllByBookId(Long bookId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Issue where book.id = :bookId", Issue.class)
                    .setParameter("bookId", bookId)
                    .list();
        }
    }
}
