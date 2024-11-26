package com.example.smartknock;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;

public class userController {
//    private userManager userManager;
//    public userController() {
//        userManager = userManager.getInstance(); // Use Singleton instance
//    }
   private static userManager userManager;

    private userController(userManager userManager) {
        this.userManager = userManager; // Assign via constructor
    }

    public static userController create() {
        if (userManager == null) {
            userManager = userManager.getInstance(); // Initialize userManager if it's null
        }
        return new userController(userManager);
    }

    public void login(String username, String password, OnCompleteListener<AuthResult> onComplete) {
        userManager.login(username, password, onComplete); // Pass the listener
    }

    public void register(String username, String password, OnCompleteListener<AuthResult> onComplete) {
        userManager.register(username, password, onComplete); // Pass the listener
    }

    public void logout() {
            userManager.logout();
        }

        public boolean isUserLoggedIn() {
            return userManager.isLoggedIn();
        }

//        public String getCurrentUserRole() {
//            return userManager.getUserRole();
//        }
    }


