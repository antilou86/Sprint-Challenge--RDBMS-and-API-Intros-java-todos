package com.lambdaschool.todo.services;

import com.lambdaschool.todo.models.Todo;
import com.lambdaschool.todo.repository.TodoRepo;
import com.lambdaschool.todo.views.TodoCount;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service(value="todoService")
public class TodoServiceImpl implements TodoService{
    private static final Logger logger = LoggerFactory.getLogger(TodoServiceImpl.class);

    @Autowired
    private TodoRepo todorepo;

    @Override
    public List<Todo> findAll() {
        List<Todo> list=new ArrayList<>();
        todorepo.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Todo findTodoById(long id) {
        return todorepo.findById(id).orElseThrow(()-> new EntityNotFoundException(Long.toString(id)));
    }

    @Override
    public List<Todo> findByUserName(String username) {
        List<Todo> list = new ArrayList<>();
        todorepo.findAll().iterator().forEachRemaining(list::add);

        list.removeIf(todo->!todo.getUser().getUsername().equalsIgnoreCase(username));
        return list;
    }

    @Transactional
    @Override
    public void delete(long id) {
        if(todorepo.findById(id).isPresent()){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(todorepo.findById(id).get().getUser().getUsername().equalsIgnoreCase(authentication.getName())){
                todorepo.deleteById(id);
                logger.info("Todo Deleted");
            }else{
                throw new EntityNotFoundException(Long.toString(id) + " " + authentication.getName());
            }
        }else{
            throw new EntityNotFoundException(Long.toString(id));
        }
    }

    @Transactional
    @Override
    public Todo save(Todo todo) {
        return todorepo.save(todo);
    }

    @Transactional
    @Override
    public Todo update(Todo todo, long id) {
        Todo newTodo = todorepo.findById(id).orElseThrow(()->new EntityNotFoundException(Long.toString(id)));
        if(todo.getDescription()!=null)
            newTodo.setDescription(todo.getDescription());
        if(todo.getUser()!=null)
            newTodo.setUser(todo.getUser());
        if(todo.getDatestarted()!=null)
            newTodo.setDatestarted(todo.getDatestarted());
        logger.info("Creating a Todo");
        return todorepo.save(newTodo);
    }

    @Override
    public ArrayList<TodoCount> getCountTodos() {
        return null;
    }
}
