package com.lambdaschool.todo.services;

import com.lambdaschool.todo.models.Todo;
import com.lambdaschool.todo.views.TodoCount;

import java.util.ArrayList;
import java.util.List;

public interface TodoService {
    List<Todo> findAll();

    Todo findTodoById(long id);

    List<Todo> findByUserName (String username);

    void delete(long id);

    Todo save(Todo todo);

    Todo update(Todo todo, long id);

    ArrayList<TodoCount> getCountTodos();
}
