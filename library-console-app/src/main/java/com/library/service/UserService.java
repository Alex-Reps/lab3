package com.library.service;

import com.library.dao.UserDAO;
import com.library.entity.User;

import java.util.List;

public class UserService {

    private final UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO();
    }

    public List<User> listUsers() {
        return userDAO.getAllUsers();
    }

    public User getUserByNickname(String nickname) {
        return userDAO.getUserByNickname(nickname);
    }

    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

    public void updateUser(User user) {userDAO.updateUser(user);}

    public void deleteUser(Long id) {
        userDAO.deleteUser(id);
    }
}
