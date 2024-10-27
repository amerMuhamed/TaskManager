package com.store.store.dao;

import java.util.List;
import java.util.Optional;

import com.store.store.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserDAORepository extends JpaRepository<User,Integer> {

    @Query("SELECT u FROM User u JOIN FETCH u.roles ")
   public List<User> getAllUsersWithRoles();
    public List<User> findByUserId(String name);
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);

}
