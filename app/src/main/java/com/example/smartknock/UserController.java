package com.example.smartknock;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;



import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

// UserController class managing user operations
public class UserController {

    private final FirebaseAuth auth;
    private final DatabaseHelper dbHelper;

    public UserController() {
        this.auth = FirebaseAuth.getInstance();
        this.dbHelper = new DatabaseHelper();
    }

    public void signupUser(
            String name,
            String email,
            String password,
            boolean isAdmin,
            String deviceId,
            String pin,
            boolean isPrimaryUser,
            SignupCallback callback
    ) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();

                        if (isAdmin) {
                            // Admin creation flow
                            Admin admin = new Admin(userId, name, email, password);
                            dbHelper.saveAdmin(userId, admin, new DatabaseCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    callback.onSignupResult(true, message);
                                }

                                @Override
                                public void onFailure(String error) {
                                    callback.onSignupResult(false, error);
                                }
                            });
                        } else {
                            // User creation flow
                            String userType = isPrimaryUser ? "PrimaryUser" : "SecondaryUser";
                            //   UserBehavior behavior = isPrimaryUser ? new PrimaryUserBehavior(email, password) : new SecondaryUserBehavior();

                            User user;

                            user = UserFactory.createUser(userId, name, email, password, userType);

                            dbHelper.saveUser(userId, user, new DatabaseCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    callback.onSignupResult(true, "User created: " + message);
                                }

                                @Override
                                public void onFailure(String error) {
                                    callback.onSignupResult(false, error);
                                }
                            });
                        }
                    } else {
                        // Authentication failed
                        callback.onSignupResult(false, task.getException().getMessage());
                    }
                });
    }
    public void loginUser(String email, String password, LoginCallback callback) {
        // Authenticate the user with Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // After successful authentication, get the current user
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();

                            // Use DatabaseHelper to fetch user data
                            dbHelper.getUserById(userId, new DatabaseCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    // Handle successful user fetch
                                    callback.onLoginResult(true, message);
                                }

                                @Override
                                public void onFailure(String error) {
                                    // Handle failure to find user
                                    dbHelper.getAdminById(userId, new DatabaseCallback() {
                                        @Override
                                        public void onSuccess(String adminMessage) {
                                            // Handle successful admin fetch
                                            callback.onLoginResult(true, adminMessage);
                                        }

                                        @Override
                                        public void onFailure(String adminError) {
                                            // User not found in both 'users' and 'admin'
                                            callback.onLoginResult(false, "No user or admin data found: " + adminError);
                                        }
                                    });
                                }
                            });
                        }
                    } else {
                        // Authentication failed
                        callback.onLoginResult(false, "Authentication failed: " + task.getException().getMessage());
                    }
                });
    }


}

//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.firebase.auth.AuthResult;
//
//public class UserController {
////    private userManager userManager;
////    public userController() {
////        userManager = userManager.getInstance(); // Use Singleton instance
////    }
//   private static userManager userManager;
//
//    private UserController(userManager userManager) {
//        this.userManager = userManager; // Assign via constructor
//    }
//
//    public static UserController create() {
//        if (userManager == null) {
//            userManager = userManager.getInstance(); // Initialize userManager if it's null
//        }
//        return new UserController(userManager);
//    }
//
//    public void login(String username, String password, OnCompleteListener<AuthResult> onComplete) {
//        userManager.login(username, password, onComplete); // Pass the listener
//    }
//
//    public void register(String username, String password, OnCompleteListener<AuthResult> onComplete) {
//        userManager.register(username, password, onComplete); // Pass the listener
//    }
//
//    public void logout() {
//            userManager.logout();
//        }
//
//        public boolean isUserLoggedIn() {
//            return userManager.isLoggedIn();
//        }
//
////        public String getCurrentUserRole() {
////            return userManager.getUserRole();
////        }
//    }
//
//
