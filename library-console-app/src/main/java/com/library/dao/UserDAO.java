package com.library.dao;

import com.library.entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import com.library.util.HibernateUtil;

import java.util.List;

public class UserDAO {

    public void saveUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            if (getUserByNickname(user.getNickname()) == null) {
                session.saveOrUpdate(user);
            } else {
                System.out.println("Пользователь с таким ником уже существует...");
            }

            transaction.commit();
        }
    }

    public void updateUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
        }
    }

    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User", User.class).list();
        }
    }

    public User getUserByNickname(String nickname) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User where nickname = :nickname", User.class)
                    .setParameter("nickname", nickname)
                    .uniqueResult();
        }
    }

    public void deleteUser(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            if (user != null) {
                session.remove(user);
                transaction.commit();
            }
        }
    }
}
