package com.library.util;

import lombok.Data;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.library.entity.Book;
import com.library.entity.Issue;
import com.library.entity.User;

public class HibernateUtil {

    // Единственный экземпляр
    private static final SessionFactory sessionFactory;

    static {
        try {
            // Загрузка конфигурации и моделей
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(Book.class)
                    .addAnnotatedClass(Issue.class)
                    .buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Инициализация SessionFactory завершилась ошибкой: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
