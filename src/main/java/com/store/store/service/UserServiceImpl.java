package com.store.store.service;

import com.store.store.dao.UserDAORepository;
import com.store.store.entity.Role;
import com.store.store.entity.User;
import com.store.store.excepions.DuplicateUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private UserDAORepository userDaoRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserDAORepository userDaoRepository) {
        this.userDaoRepository = userDaoRepository;
    }

    @Transactional
    @Override
    public void updateUser(User user) {
        // Encrypt the user's password before saving
        user.setPassword("{bcrypt}" + passwordEncoder.encode(user.getPassword()));

        Optional<User> existingUserByEmail = userDaoRepository.findByEmail(user.getEmail());

        // Check for existing user by phone
        Optional<User> existingUserByPhone = userDaoRepository.findByPhone(user.getPhone());

        // Check for users with the same username (userId)
        List<User> usersWithSameUsername = userDaoRepository.findByUserId(user.getUserId());

        // If a user exists with the same email and it's not the same user we're updating, throw exception
        if (existingUserByEmail.isPresent() && existingUserByEmail.get().getId() != user.getId()) {
            throw new DuplicateUserException("A user with this email already exists.");
        }

        // If a user exists with the same phone and it's not the same user we're updating, throw exception
        if (existingUserByPhone.isPresent() && existingUserByPhone.get().getId() != user.getId()) {
            throw new DuplicateUserException("A user with this phone number already exists.");
        }
        // Check if there is any user with the same username but different id
        else if (usersWithSameUsername.stream().anyMatch(u -> u.getId() != user.getId())) {
            throw new DuplicateUserException("A user with this username already exists.");
        }
        userDaoRepository.save(user);
    }

    @Transactional
    @Override
    public void saveUser(User user) {
        // Encrypt the user's password before saving
        user.setPassword("{bcrypt}" + passwordEncoder.encode(user.getPassword()));

        Optional<User> existingUserByEmail = userDaoRepository.findByEmail(user.getEmail());

        // Check for existing user by phone
        Optional<User> existingUserByPhone = userDaoRepository.findByPhone(user.getPhone());

        // Check for users with the same username (userId)
        List<User> usersWithSameUsername = userDaoRepository.findByUserId(user.getUserId());

        // If a user exists with the same email and it's not the same user we're updating, throw exception
        if (existingUserByEmail.isPresent() && existingUserByEmail.get().getId() != user.getId()) {
            throw new DuplicateUserException("A user with this email already exists.");
        }

        // If a user exists with the same phone and it's not the same user we're updating, throw exception
        if (existingUserByPhone.isPresent() && existingUserByPhone.get().getId() != user.getId()) {
            throw new DuplicateUserException("A user with this phone number already exists.");
        }
        // Check if there is any user with the same username but different id
        else if (usersWithSameUsername.stream().anyMatch(u -> u.getId() != user.getId())) {
            throw new DuplicateUserException("A user with this username already exists.");
        } else {

            List<String> list = new ArrayList<>();
            list.add("ROLE_USER");
            if (user.getSelectedRoles().isEmpty()) {
                user.setSelectedRoles(list);
            }
            // Create a new Role object for each selected role
            for (String roleName : user.getSelectedRoles()) {
                Role role = new Role(roleName);
                user.addRole(role);              // Add the role using the addRole method
            }
        }

        // Save the user along with the roles
        userDaoRepository.save(user);
    }

    @Override
    public List<User> getAllUsersWithRoles() {
        return userDaoRepository.getAllUsersWithRoles();
    }

    @Override
    public User findById(int theId) {
        Optional<User> result = userDaoRepository.findById(theId);
        User theUser = null;
        if (result.isPresent()) {
            theUser = result.get();
        } else {
            throw new RuntimeException("Did not find User id - " + theId);
        }
        return theUser;
    }

    @Override
    public List<User> findByUserId(String name) {
        return userDaoRepository.findByUserId(name);
    }

    @Override
    public void deleteById(int id) {
        userDaoRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDaoRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userDaoRepository.findByPhone(phone);
    }

    @Override
    public List<User> getAllUsers() {
        return userDaoRepository.findAll();
    }


}
