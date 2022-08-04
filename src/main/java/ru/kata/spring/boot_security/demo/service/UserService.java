package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {

    void addUser(User user);

    void deleteUserById(int id);

    List<User> getAllUsers();

    User getUserById(int id);

    User getUserByEmail(String email);

    void updateUser(User user);

    List<Role> listRoles();

    Role getRoleByName(String roleName);


}
