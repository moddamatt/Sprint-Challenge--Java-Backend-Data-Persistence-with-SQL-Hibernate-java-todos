package com.sprintchallenge.todo.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "todos")
public class Todo extends Auditable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long todoid;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"todoslist"})
    @JoinColumn(name = "userid")
    private User user;

    @OneToMany(mappedBy = "todo",
            cascade = CascadeType.ALL)
    @JsonIgnoreProperties("todo")
    private List<UserTodos> userTodos = new ArrayList<>();

    public Todo()
    {
    }

    public Todo(String description, User user)
    {
        this.description = description;
        this.user = user;
    }


    public Todo(String description, List<UserTodos> userTodos)
    {
        this.description = description;
        this.userTodos = userTodos;
    }

    public Todo(String description)
    {
        this.description = description;
    }

    public List<UserTodos> getUserTodos()
    {
        return userTodos;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public void setUserTodos(List<UserTodos> userTodos)
    {
        this.userTodos = userTodos;
    }

    public long getTodoid()
    {
        return todoid;
    }

    public void setTodoid(long todoid)
    {
        this.todoid = todoid;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

}