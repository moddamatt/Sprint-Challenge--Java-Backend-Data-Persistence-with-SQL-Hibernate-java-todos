package com.sprintchallenge.todo.repository;

import com.sprintchallenge.todo.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>
{
    User findByUsername(String username);
}
