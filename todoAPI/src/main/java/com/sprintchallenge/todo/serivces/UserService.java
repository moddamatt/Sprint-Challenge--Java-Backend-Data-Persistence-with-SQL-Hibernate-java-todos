package com.sprintchallenge.todo.serivces;

import com.sprintchallenge.todo.models.Todo;
import com.sprintchallenge.todo.models.User;
import java.util.List;

public interface UserService {

    List<User> findAll();

    User findUserById(long id);

    void delete(long id);

    User save(User user);

    User update(User user, long id);

    Todo addTodo(Todo todo, long id);
}
