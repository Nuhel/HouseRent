package com.example.nuhel.houserent.View.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nuhel.houserent.Controller.FragmentControllerAfterUserLog_Reg;
import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.roger.catloadinglibrary.CatLoadingView;

public class UserRegisterFragment extends Fragment {

    private static FirebaseUser user;
    private static UserProfileChangeRequest profileChangeRequest;
    private static FirebaseAuth mAuth;
    //IDeclaring view
    private View view;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText re_enter_passwordEditText;
    //IDeclaring signUpButton
    private Button signUpButton;
    private int lastLengthOfEmail = 0;
    private int[] activeColors = {Color.parseColor("#6adcc8"), Color.parseColor("#5dcfc0"), Color.parseColor("#50c3b8")};
    private GradientDrawable activeGradient = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, activeColors);
    private int[] deactiveColors = {Color.parseColor("#4a5b70"), Color.parseColor("#4a5b70")};
    private GradientDrawable deactiveGradient = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, deactiveColors);
    private int radius = 20;
    private CatLoadingView mView;

    private FragmentControllerAfterUserLog_Reg fragmentControllerAfterUserLogReg;

    public UserRegisterFragment() {
        // Required empty public constructor
    }


    public static UserRegisterFragment newInstance(Bundle bundle) {

        UserRegisterFragment userRegisterFragment = new UserRegisterFragment();
        userRegisterFragment.setArguments(bundle);
        return userRegisterFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentControllerAfterUserLogReg = (FragmentControllerAfterUserLog_Reg) getArguments().getSerializable("serializable");
        initViews(inflater, container);
        setTextChangeListener();
        return view;
    }

    private void autoCompliteEmailForm() {
        String text = emailEditText.getText().toString();
        int length = text.length();
        if (lastLengthOfEmail < length) {
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
        lastLengthOfEmail = length;
    }

    private boolean isEmailValid() {
        boolean isOk = Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches();
        if (isOk) {
            emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ricon, 0);
        } else {
            if (emailEditText.getText().length() > 0) {
                emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.wicon, 0);
            } else {
                emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
        return isOk;
    }

    private boolean isPhoneNumValid() {
        boolean isOk = Patterns.PHONE.matcher(phoneEditText.getText().toString()).matches();
        if (isOk) {
            phoneEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ricon, 0);
        } else {
            if (phoneEditText.getText().length() > 0) {
                phoneEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.wicon, 0);
            } else {
                phoneEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
        return isOk;
    }

    private boolean isUserNameOk() {
        boolean isOk = usernameEditText.getText().length() > 5;
        if (isOk) {
            usernameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ricon, 0);
        } else {
            if (usernameEditText.getText().length() > 0) {
                usernameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.wicon, 0);
            } else {
                usernameEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        }
        return isOk;
    }

    private boolean isPassWordOk() {

        boolean isPassOk, isRePassOk;

        isPassOk = passwordEditText.getText().length() > 6;
        isRePassOk = passwordEditText.getText().toString().equals(re_enter_passwordEditText.getText().toString()) && isPassOk;

        if (isPassOk) {
            passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ricon, 0);
        } else {
            if (passwordEditText.getText().length() > 0) {
                passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.wicon, 0);
            } else {
                passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        }

        if (isRePassOk) {
            re_enter_passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ricon, 0);
        } else {
            if (re_enter_passwordEditText.getText().length() > 0) {
                re_enter_passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.wicon, 0);
            } else {
                re_enter_passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }
        }

        return isPassOk && isRePassOk;
    }

    private void setUpRegisreButton() {
        boolean isEmailValid = isEmailValid();
        boolean isPhoneNumValid = isPhoneNumValid();
        boolean isPassWordOk = isPassWordOk();
        boolean isUserNameOk = isUserNameOk();


        if (isEmailValid && isPhoneNumValid && isPassWordOk && isUserNameOk) {
            signUpButton.setActivated(true);
            signUpButton.setBackground(activeGradient);
        } else {
            signUpButton.setActivated(false);
            signUpButton.setBackground(deactiveGradient);
        }

    }

    private void initViews(LayoutInflater inflater, ViewGroup container) {
        view = view == null ? inflater.inflate(R.layout.user_registration_layout, container, false) : view;

        usernameEditText = view.findViewById(R.id.nameEdittext);

        passwordEditText = view.findViewById(R.id.passeditText);

        re_enter_passwordEditText = view.findViewById(R.id.repasseditText);

        phoneEditText = view.findViewById(R.id.phoneeditText);

        emailEditText = view.findViewById(R.id.emailEdittext);

        signUpButton = view.findViewById(R.id.signupbtn);
        activeGradient.setCornerRadius(radius);
        deactiveGradient.setCornerRadius(radius);

        signUpButton.setBackground(deactiveGradient);
        signUpButton.setActivated(false);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

    }

    private void setTextChangeListener() {
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                setUpRegisreButton();
            }
        });


        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setUpRegisreButton();

            }
        });

        re_enter_passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setUpRegisreButton();

            }
        });

        phoneEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setUpRegisreButton();

            }
        });

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
                setUpRegisreButton();

            }
        });
    }


    private void signUp() {

        mView = new CatLoadingView();
        mView.show(getFragmentManager(), "Loading.........");
        String email = emailEditText.getText().toString();
        final String displayName = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        final String phoneNUmber = phoneEditText.getText().toString();

        mAuth = GetFirebaseAuthInstance.getFirebaseAuthInstance();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) view.getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(view.getContext(), "Authentication",
                                    Toast.LENGTH_SHORT).show();
                            user = mAuth.getCurrentUser();
                            DatabaseReference db = GetFirebaseInstance.GetInstance().getReference("User");
                            String id = user.getUid();
                            db.child(id).child("phone").setValue(phoneNUmber);
                            profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(displayName).build();
                            user.updateProfile(profileChangeRequest).addOnCompleteListener((Activity) view.getContext(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mView.dismiss();
                                        Toast.makeText(view.getContext(), "Done", Toast.LENGTH_SHORT).show();
                                    } else {
                                        mView.dismiss();
                                        Toast.makeText(view.getContext(), "Fail", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.

                        }

                    }
                });
    }



}