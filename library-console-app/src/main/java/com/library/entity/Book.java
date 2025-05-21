package com.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Название книги
    private String title;

    // Автор книги
    private String author;

    // Статус бронирования
    private boolean isReserved;

    public Book(String title, String author, boolean isReserved) {
        this.title = title;
        this.author = author;
        this.isReserved = isReserved;
    }

    @Override
    public String toString() {
        return "id: " + id.toString() + " / Название: " + title + " / Автор: " + author + " / В наличии: " + (isReserved ? "На руках" : "Доступна");
    }

}
