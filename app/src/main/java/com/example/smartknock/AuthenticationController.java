package com.example.smartknock;
public class AuthenticationController {
    private final AuthenticationManager authmanager ;

    public AuthenticationController() {
        this.authmanager = new AuthenticationManager();
    }
    // Delegate Signup to UserAuthentication
    public void signupUser(String name, String email, String password, boolean isPrimaryUser, SignupCallback callback) {
        authmanager.signupUser(name, email, password,  isPrimaryUser, callback);
    }
    // Delegate Login to UserAuthentication
    public void loginUser(String email, String password, LoginCallback callback) {
        authmanager.loginUser(email, password, callback);
        //  authmanager.loginAdmin(email, password, callback);
    }
    public void loginAdmin(String email, String password, LoginCallback callback) {
        // authmanager.loginUser(email, password, callback);
        authmanager.loginAdmin(email, password, callback);
    }
}

//public class AuthenticationController {
//    private final AuthenticationManager authmanager ;
//
//    public AuthenticationController() {
//        this.authmanager = new AuthenticationManager();
//    }
//    // Delegate Signup to UserAuthentication
//    public void signupUser(String name, String email, String password, boolean isAdmin, String deviceId, String pin, boolean isPrimaryUser, SignupCallback callback) {
//        authmanager.signupUser(name, email, password, isAdmin, deviceId, pin, isPrimaryUser, callback);
//    }
//    // Delegate Login to UserAuthentication
//    public void loginUser(String email, String password, LoginCallback callback) {
//        authmanager.loginUser(email, password, callback);
//    }
//}
