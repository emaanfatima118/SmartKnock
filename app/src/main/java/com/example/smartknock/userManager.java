package com.example.smartknock;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
public class userManager {

        private static userManager instance;
        private final FirebaseAuth firebaseAuth;

        // Private constructor for Singleton pattern
        private userManager() {
            firebaseAuth = FirebaseAuth.getInstance(); // Initialize Firebase Authentication
        }

    // Get the single instance of UserManager
        public static synchronized userManager getInstance() {
            if (instance == null) {
                instance = new userManager(); // Create instance if it doesn't exist
            }
            return instance; // Return the existing or new instance
        }

        /**
         * Log in the user using email and password.
         *
         * @param email       User's email
         * @param password    User's password
         * @param onComplete  Listener to handle success or failure
         */
        public void login(String email, String password, OnCompleteListener<AuthResult> onComplete) {
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(onComplete);
        }
        /**
         * Register a new user using email and password.
         *
         * @param email       User's email
         * @param password    User's password
         * @param onComplete  Listener to handle success or failure
         */
        public void register(String email, String password, OnCompleteListener<AuthResult> onComplete) {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(onComplete);
        }

        /**
         * Log out the current user.
         */
        public void logout() {
            firebaseAuth.signOut();
        }

        /**
         * Check if a user is currently logged in.
         *
         * @return True if a user is logged in, false otherwise.
         */
        public boolean isLoggedIn() {
            return firebaseAuth.getCurrentUser() != null;
        }

        /**
         * Get the currently logged-in user.
         *
         * @return FirebaseUser object or null if no user is logged in.
         */
        public FirebaseUser getCurrentUser() {
            return firebaseAuth.getCurrentUser();
        }


}
