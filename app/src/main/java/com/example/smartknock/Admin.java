package com.example.smartknock;

import java.util.ArrayList;
import java.util.List;

public class Admin {

    private String id;
    private String name;
    private String email;
    private String password; // Added password attribute
   // private List<User> users; // List to store associated users

    // Constructor
    public Admin(String id,String name, String email, String password) {
        this.id=id;
        this.name = name;
        this.email = email;
        this.password = password;
        //this.users = new ArrayList<>(); // Initialize user list
    }

    // No-arg constructor (required for Firestore)
    public Admin() {
        //this.users = new ArrayList<>(); // Ensure list is initialized
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public String getid() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public List<User> getUsers() {
//        return users;
//    }
//
//    public void setUsers(List<User> users) {
//        this.users = users;
//    }
//
//    public void addUser(User user) {
//        this.users.add(user); // Add a user to the list
//    }
}
/*
public class Admin {
    private String id;
    private String name;
    private String email;
    private String password; // Added password attribute

    // Constructor
    public Admin(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password; // Set password
    }

    // No-arg constructor (required for Firestore)
    public Admin() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
*/