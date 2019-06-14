package com.sprintchallenge.todo.serivces;

import com.sprintchallenge.todo.models.Todo;

public interface TodoService
{
    Todo update(Todo todo, long id);
}
