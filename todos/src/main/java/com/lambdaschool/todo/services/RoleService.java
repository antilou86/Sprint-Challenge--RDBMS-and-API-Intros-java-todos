package com.lambdaschool.todo.services;

import com.lambdaschool.todo.models.Roles;

import java.util.List;

public interface RoleService {

        List<Roles> findAll();

        Roles findRoleById(long id);

        void delete(long id);

        Roles save(Roles role);
    }

