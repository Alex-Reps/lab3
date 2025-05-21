package com.library.service;

import com.library.entity.User;

public class AuthService {

    private final UserService userService;

    public AuthService() {
        this.userService = new UserService();
    }

    // Авторизация пользователя
    public User authenticate(String nickname) {
        User user = userService.getUserByNickname(nickname);
        if (user != null) {
            return user;
        }
        throw new IllegalArgumentException("Пользователь не найден!");
    }

    // Регистрация пользователя
    public User register(String firstName, String lastName, String middleName, String nickname) {
        User user = new User(firstName, lastName, middleName, nickname, User.Role.READER);
        userService.saveUser(user);
        return user;
    }
}
