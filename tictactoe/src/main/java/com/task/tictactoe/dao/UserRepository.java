package com.task.tictactoe.dao;


import com.task.tictactoe.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByNameOrEmail(String name, String email);
}
