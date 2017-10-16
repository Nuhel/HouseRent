package com.example.nuhel.houserent.Controller;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Nuhel on 10/12/2017.
 */

public class GetFirebaseAuthInstance {
    public static FirebaseAuth getFirebaseAuthInstance() {
        return FirebaseAuth.getInstance();
    }
}
