package com.lambdaschool.todo.controllers;

import com.lambdaschool.todo.models.Todo;
import com.lambdaschool.todo.models.User;
import com.lambdaschool.todo.services.TodoService;
import com.lambdaschool.todo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private TodoService todoService;

    @GetMapping(value = "/mine", produces = {"application/json"})
    public ResponseEntity<?> getData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User data=userService.findUserByName(authentication.getName());
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value="",consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addUser(@Valid @RequestBody User newUser) throws URISyntaxException{
        newUser=userService.save(newUser);
        HttpHeaders responseHeaders= new HttpHeaders();
        URI newUserURI=ServletUriComponentsBuilder.fromCurrentRequest().path("/{userid}").buildAndExpand(newUser.getUserid()).toUri();
        responseHeaders.setLocation(newUserURI);

        return new ResponseEntity<>(null,responseHeaders, HttpStatus.CREATED);
    }

    @PostMapping(value="/todo/{userid}",consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addTodo(@Valid @RequestBody Todo todo, @PathVariable long userid){
        todo.setUser(userService.findUserById(userid));
        todo=todoService.save(todo);

        return new ResponseEntity<>(todo,HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/users/userid/{userid}")
    public ResponseEntity<?> removeUser(@PathVariable long userid){
        userService.delete(userid);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

    @PostMapping(value = "/todo/{todoid}", consumes = {"application/json"}, produces = {"application/json"})
    public ResponseEntity<?> addTodoToSelf(@Valid @RequestBody Todo todo, @PathVariable long todoid){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        User user=userService.findUserByName(authentication.getName());
        todo.setUser(user);
        todo=todoService.save(todo);
        user=userService.newTodo(user,todo);
        userService.save(user);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }

}