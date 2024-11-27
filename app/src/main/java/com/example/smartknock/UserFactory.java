package com.example.smartknock;

class UserFactory {
    public static User createUser(String id, String name, String email,  String password,String userType) {
        switch (userType) {
            case "PrimaryUser":
                return new User(id, name, email, userType, password, new PrimaryUserBehavior(email,password));
            case "SecondaryUser":
                return new User(id, name, email, userType, password, new SecondaryUserBehavior());
            default:
                throw new IllegalArgumentException("Invalid user type");
        }
    }
}