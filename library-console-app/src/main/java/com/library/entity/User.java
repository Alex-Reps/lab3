package com.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Имя пользователя
    private String firstName;

    private String lastName;

    private String middleName;

    // Никнейм для входа в систему
    @Column(unique = true, nullable = false)
    private String nickname;

    // Роль (admin или reader)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User(String firstName, String lastName, String middleName, String nickname, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.nickname = nickname;
        this.role = role;
    }

    public enum Role {
        ADMIN,
        READER
    }
}