package com.example.smartknock;

public class userdataholder {
    String id,name,password,email,role;
    public userdataholder(String id,String password, String name, String email, String role) {
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    public String getId() {
        return id;
    }

}