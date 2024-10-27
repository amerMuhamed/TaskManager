package com.store.store.service;

import com.store.store.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    public void saveUser(User theUser);

    public void updateUser(User theUser);

    public List<User> getAllUsersWithRoles();

    public User findById(int theId);

    public List<User> findByUserId(String name);

    public void deleteById(int id);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    public List<User> getAllUsers();
}