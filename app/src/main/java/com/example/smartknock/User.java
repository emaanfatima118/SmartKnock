package com.example.smartknock;

public class User {
    private String id;
    private String name;
    private String email;
    private String userType;
    private String password; // Added password attribute

    private UserBehavior userBehavior;
    // Constructor

    public User(String id, String name, String email,  String password,String userType, UserBehavior userBehavior) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.password = password;
        this.userBehavior = userBehavior;
    }


    // No-arg constructor (required for Firestore)
    public User() {}

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

    public String getUserType() {
        return userType;
    }

    public String getPassword() {
        return password;
    }


        public void setBehavior(UserBehavior behavior) {
            this.userBehavior = behavior;
        }

        public void executeBehavior() {
            userBehavior.executeFunctionality();
        }

}
