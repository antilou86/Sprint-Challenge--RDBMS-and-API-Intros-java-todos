package com.lambdaschool.todo.repository;

import org.springframework.data.repository.CrudRepository;

public interface TodoRepo extends CrudRepository<TodoRepo, Long> {
}
