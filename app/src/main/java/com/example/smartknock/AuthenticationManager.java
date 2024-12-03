package com.example.smartknock;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationManager {
    private final FirebaseAuth auth;
    private final AuthenticationHelper authhelp;

    public AuthenticationManager() {
        this.auth = FirebaseAuth.getInstance();
        this.authhelp = new AuthenticationHelper();
    }
    // Admin Login Logic
    public void loginAdmin(String email, String password, LoginCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            authhelp.getAdminById(userId, new DatabaseCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    callback.onLoginResult(true, "Admin login successful");
                                }

                                @Override
                                public void onFailure(String error) {
                                    callback.onLoginResult(false, "Admin not found: " + error);
                                }
                            });
                        }
                    } else {
                        callback.onLoginResult(false, "Authentication failed: " + task.getException().getMessage());
                    }
                });
    }

    // User Login Logic
    public void loginUser(String email, String password, LoginCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            String userId = user.getUid();
                            authhelp.getUserById(userId, new DatabaseCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    callback.onLoginResult(true, "User login successful");
                                }

                                @Override
                                public void onFailure(String error) {
                                    callback.onLoginResult(false, "User not found: " + error);
                                }
                            });
                        }
                    } else {
                        callback.onLoginResult(false, "Authentication failed: " + task.getException().getMessage());
                    }
                });
    }
    public void signupUser(String name, String email, String password,  boolean isPrimaryUser, SignupCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = auth.getCurrentUser().getUid();


                            String userType = isPrimaryUser ? "PrimaryUser" : "SecondaryUser";
                            User user = UserFactory.createUser(userId, name, email, password, userType);

                            authhelp.saveUser(userId, user, new DatabaseCallback() {
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
                        callback.onSignupResult(false, "Authentication failed: " + task.getException().getMessage());
                    }
                });
    }
}



