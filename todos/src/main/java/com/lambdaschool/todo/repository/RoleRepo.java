package com.lambdaschool.todo.repository;

import com.lambdaschool.todo.models.Roles;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Roles, Long> {
}
