package com.example.nuhel.houserent.Controller;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roger.catloadinglibrary.CatLoadingView;

/**
 * Created by Nuhel on 10/12/2017.
 */

public class GetFirebaseAuthInstance {

    // [START declare_auth]

    static FirebaseAuth mAuth = null;
    // [END declare_auth]

    static FirebaseUser user;
    private static CatLoadingView cat;

    public static FirebaseUser getFirebaseAuthInstance(final Context context, String email, String pass, final CatLoadingView mView, final TryInterface tryInterface) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mView.dismiss();
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
                            String userEmail = mAuth.getCurrentUser().getEmail().toString();
                            Toast.makeText(context, "Sucsesfully LogedIn\nWelcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                            tryInterface.makeToast();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();

                        }
                    }
                });
        return user;
    }

    public static FirebaseAuth getmAuth() {
        mAuth = mAuth == null ? FirebaseAuth.getInstance() : mAuth;
        return mAuth;
    }

}
