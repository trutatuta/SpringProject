package com.example.myproject.dao;

import com.example.myproject.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserDao extends CrudRepository<User, Integer> {
    public User findByUsername(String username);

}
