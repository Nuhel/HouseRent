package com.example.nuhel.houserent.View.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nuhel.houserent.Controller.FragmentControllerAfterUserLog_Reg;
import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.roger.catloadinglibrary.CatLoadingView;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserLoginFragment extends Fragment {


    private static FirebaseAuth mAuth = null;
    private static FirebaseUser user;
    private CircleImageView imageView;
    private View view;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signinBtn;
    private int lastLengthOfemail = 0;
    private CatLoadingView mView;

    public UserLoginFragment() {
        // Required empty public constructor
    }

    public static UserLoginFragment newInstance(Bundle bundle) {
        UserLoginFragment userLoginFragment = new UserLoginFragment();
        userLoginFragment.setArguments(bundle);
        return userLoginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = view == null ? inflater.inflate(R.layout.user_login_layout, container, false) : view;



        emailEditText = view.findViewById(R.id.userloginEmailEdittext);
        passwordEditText = view.findViewById(R.id.userloginpasseditText);

        imageView = view.findViewById(R.id.userPhoto);

        signinBtn = view.findViewById(R.id.userloginsigninbtn);
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                autoCompliteEmailForm();
            }
        });
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String pass = passwordEditText.getText().toString();

                if (!email.equals("") && !pass.equals("")) {
                    mView = new CatLoadingView();
                    mView.show(getFragmentManager(), "Loading.........");
                    mAuth = GetFirebaseAuthInstance.getFirebaseAuthInstance();

                    mAuth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener((Activity) view.getContext(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    mView.dismiss();
                                    if (task.isSuccessful()) {
                                        user = mAuth.getCurrentUser();
                                        String userEmail = mAuth.getCurrentUser().getEmail().toString();
                                        Toast.makeText(view.getContext(), "Sucsesfully LogedIn\nWelcome " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
                                        Glide.with(view.getContext()).load(user.getPhotoUrl()).into(imageView);
                                        ((FragmentControllerAfterUserLog_Reg) getActivity()).setFrag();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(view.getContext(), "Cannot Sign in. Please check the form and try again.", Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                }

            }
        });


        return view;
    }

    private void autoCompliteEmailForm() {
        String text = emailEditText.getText().toString();
        int length = text.length();
        if (lastLengthOfemail < length) {
            if (length > 1) {
                text = text.substring(length - 2);
                text = text.toLowerCase();
                char[] lastChars = text.toCharArray();
                if (lastChars[0] == '@') {
                    if (lastChars[1] == 'g') {
                        emailEditText.append("mail.com");
                    } else if (lastChars[1] == 'h') {
                        emailEditText.append("otmail.com");
                    } else if (lastChars[1] == 'l') {
                        emailEditText.append("ive.com");
                    } else if (lastChars[1] == 'y') {
                        emailEditText.append("ahoo.com");
                    }
                }
            }
        }
        lastLengthOfemail = length;
    }

}
