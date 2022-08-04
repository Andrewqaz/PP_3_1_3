package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private RoleDao roleDao;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public void addUser(User user) {
        Set<Role> roles = new HashSet<>();
        for (Role r : user.getRoles()) {
            roles.add(roleDao.getByName(r.getName()));
        }
        user.setRoles(roles);
        userDao.addUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public void deleteUserById(int id) {
        userDao.deleteUserById(id);
    }

    @Override
    public User getUserById(int id) {
        return userDao.getUserById(id);
    }

    @Override
    public User getUserByEmail(String login) {
        return userDao.getUserByEmail(login);
    }

    @Override
    public void updateUser(User user) {
        Set<Role> roles = new HashSet<>();
        for (Role r : user.getRoles()) {
            roles.add(roleDao.getByName(r.getName()));
        }
        user.setRoles(roles);
        userDao.updateUser(user);
    }

    @Override
    public List<Role> listRoles() {
        return roleDao.findAll();
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleDao.getByName(roleName);
    }
}
