package com.lambdaschool.todo.repository;

import com.lambdaschool.todo.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
}
