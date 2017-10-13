package com.example.nuhel.houserent.Controller;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Nuhel on 8/17/2017.
 */

public class GetFirebaseInstance {
    private static FirebaseDatabase firebaseDatabase;

    public static FirebaseDatabase GetInstance() {
        if (firebaseDatabase == null) {
            firebaseDatabase = FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
            return firebaseDatabase;
        }
        return firebaseDatabase;
    }
}
