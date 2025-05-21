package com.library.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "issues")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Issue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Пользователь, взявший книгу
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Книга, которую взяли
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    // Дата выдачи книги
    private LocalDate issueDate;

    // Дата возврата книги
    private LocalDate returnDate;
}
