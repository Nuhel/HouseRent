package com.example.nuhel.houserent.Controller;

import com.google.firebase.auth.FirebaseAuth;
import com.roger.catloadinglibrary.CatLoadingView;

/**
 * Created by Nuhel on 10/12/2017.
 */

public class GetFirebaseAuthInstance {

    // [START declare_auth]
    private static FirebaseAuth mAuth = null;
    // [END declare_auth]


    private static CatLoadingView cat;

    public static FirebaseAuth getFirebaseAuthInstance() {
        mAuth = FirebaseAuth.getInstance();
        return mAuth;
    }


}
