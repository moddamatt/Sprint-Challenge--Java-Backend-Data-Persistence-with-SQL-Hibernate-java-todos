package com.sprintchallenge.todo;

import com.sprintchallenge.todo.models.Role;
import com.sprintchallenge.todo.models.Todo;
import com.sprintchallenge.todo.models.User;
import com.sprintchallenge.todo.models.UserRoles;
import com.sprintchallenge.todo.repository.RoleRepository;
import com.sprintchallenge.todo.repository.TodoRepository;
import com.sprintchallenge.todo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Transactional
@Component
public class SeedData implements CommandLineRunner
{
    RoleRepository rolerepos;
    UserRepository userrepos;
    TodoRepository todorepos;

    public SeedData(RoleRepository rolerepos, UserRepository userrepos, TodoRepository todorepos)
    {
        this.rolerepos = rolerepos;
        this.userrepos = userrepos;
        this.todorepos = todorepos;
    }

    @Override
    public void run(String[] args) throws Exception
    {
        Role r1 = new Role("admin");
        Role r2 = new Role("user");

        ArrayList<UserRoles> admins = new ArrayList<>();
        admins.add(new UserRoles(new User(), r1));
        admins.add(new UserRoles(new User(), r2));

        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(), r2));

        rolerepos.save(r1);
        rolerepos.save(r2);

        User u1 = new User("barnbarn", "ILuvM4th!", users);
        User u2 = new User("admin", "password", admins);
        User u3 = new User("Bob", "password", users);
        User u4 = new User("Jane", "password", users);

        // the date and time string should get coverted to a datetime Java data type. This is done in the constructor!
        u4.getTodoslist().add(new Todo("Finish java-orders-swagger", u4));
        u4.getTodoslist().add(new Todo("Feed the turtles", u4));
        u4.getTodoslist().add(new Todo("Complete the sprint challenge", u4));

        u3.getTodoslist().add(new Todo("Walk the dogs", u3));
        u3.getTodoslist().add(new Todo("provide feedback to my instructor", u3));

        userrepos.save(u1);
        userrepos.save(u2);
        userrepos.save(u3);
        userrepos.save(u4);
    }
}
