package com.example.myproject.dao;

import com.example.myproject.entity.Post;
import com.example.myproject.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PostDao extends CrudRepository<Post, Integer> {
    public List<Post> findAllByUser(User user);
    public Optional<Post> findById(Integer id);
    public void deleteAllByUser(User user);
}
