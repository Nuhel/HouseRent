package com.example.nuhel.houserent.Controller;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Nuhel on 10/12/2017.
 */

public class GetFirebaseAuthInstance {

    // [START declare_auth]

    static FirebaseAuth mAuth;
    // [END declare_auth]

    static FirebaseUser user;

    public static FirebaseUser getFirebaseAuthInstance(final Context context) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword("nuheljft@gmail.com", "123456789")
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            user = mAuth.getCurrentUser();
                          //  Toast.makeText(context, user.getDisplayName(), Toast.LENGTH_SHORT).show();
                          //  Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();

                            if (user != null) {
                                Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, mAuth.getCurrentUser().getEmail().toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(context, "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();


                        }

                        // ...
                    }
                });
// [END sign_in_with_email]
        return user;
    }

}
