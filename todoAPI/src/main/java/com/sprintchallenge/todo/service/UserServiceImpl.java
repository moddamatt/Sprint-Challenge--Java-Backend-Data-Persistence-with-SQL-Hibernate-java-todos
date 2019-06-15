package com.sprintchallenge.todo.service;

import com.sprintchallenge.todo.model.Todo;
import com.sprintchallenge.todo.model.User;
import com.sprintchallenge.todo.model.UserRoles;
import com.sprintchallenge.todo.repository.RoleRepository;
import com.sprintchallenge.todo.repository.TodoRepository;
import com.sprintchallenge.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service(value = "userService")
public class UserServiceImpl implements UserDetailsService, UserService
{

    @Autowired
    private UserRepository userrepos;

    @Autowired
    private RoleRepository rolerepos;

    @Autowired
    private TodoRepository todoRepos;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = userrepos.findByUsername(username);
        if (user == null)
        {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getAuthority());
    }

    public User findUserById(long id) throws EntityNotFoundException
    {
        return userrepos.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Long.toString(id)));
    }

    public List<User> findAll()
    {
        List<User> list = new ArrayList<>();
        userrepos.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public void delete(long id)
    {
        if (userrepos.findById(id).isPresent())
        {
            userrepos.deleteById(id);
        }
        else
        {
            throw new EntityNotFoundException(Long.toString(id));
        }
    }

    @Transactional
    @Override
    public User save(User user)
    {
        User newUser = new User();
        newUser.setUsername(user.getUsername());

        ArrayList<UserRoles> newRoles = new ArrayList<>();
        for (UserRoles ur : user.getUserRoles())
        {
            newRoles.add(new UserRoles(newUser, ur.getRole()));
        }
        newUser.setUserRoles(newRoles);

        ArrayList<Todo> newTodos = new ArrayList<>();
        for (Todo t : user.getTodoslist())
        {
            newTodos.add(new Todo(t.getDescription(), newUser));
        }
        newUser.setTodoslist(newTodos);

        return userrepos.save(newUser);
    }

    @Transactional
    @Override
    public User update(User user, long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userrepos.findByUsername(authentication.getName());

        if (currentUser != null)
        {
            if (id == currentUser.getUserid())
            {
                if (user.getUsername() != null)
                {
                    currentUser.setUsername(user.getUsername());
                }

                if (user.getPassword() != null)
                {
                    currentUser.setPassword(user.getPassword());
                }

                if (user.getUserRoles().size() > 0)
                {

                    rolerepos.deleteUserRolesByUserId(currentUser.getUserid());


                    for (UserRoles ur : user.getUserRoles())
                    {
                        rolerepos.insertUserRoles(id, ur.getRole().getRoleid());
                    }
                }

                return userrepos.save(currentUser);
            }
            else
            {
                throw new EntityNotFoundException(Long.toString(id) + " Not current user");
            }
        }
        else
        {
            throw new EntityNotFoundException(authentication.getName());
        }

    }

    @Transactional
    @Override
    public Todo addTodo(Todo todo, long id)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = userrepos.findByUsername(authentication.getName());
        Todo newTodo = new Todo();
        newTodo.setDescription(todo.getDescription());

        ArrayList<Todo> newTodos = new ArrayList<>();
        for (Todo t : currentUser.getTodoslist())
        {
            todoRepos.insertUserTodos(t.getTodoid(), currentUser.getUserid());
        }
        currentUser.setTodoslist(newTodos);

        return todoRepos.save(newTodo);
    }

}