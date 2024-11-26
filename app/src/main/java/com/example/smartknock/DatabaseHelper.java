package com.example.smartknock;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
public class DatabaseHelper {

        private DatabaseReference databaseReference;
        private final FirebaseFirestore db;
        public DatabaseHelper(String path)
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference(path);
            db = FirebaseFirestore.getInstance();
        }

        public DatabaseHelper()
        {
            db = FirebaseFirestore.getInstance();
        }
        public void writeData(String key, Object value) {
            databaseReference.child(key).setValue(value);
        }
    public DatabaseHelper(FirebaseFirestore db) {
        this.db = db;
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
    }
    public DatabaseReference getDatabaseReference(String path) {
        return databaseReference.child(path); // Return the reference to the specified path
    }
    public CollectionReference getCollectionReference(String collectionName) {
        return db.collection(collectionName);
    }

}

