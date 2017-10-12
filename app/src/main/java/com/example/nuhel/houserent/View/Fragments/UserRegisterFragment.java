package com.example.nuhel.houserent.View.Fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nuhel.houserent.Adapter.RecyclerViewAdapter;
import com.example.nuhel.houserent.R;

public class UserRegisterFragment extends Fragment {


    private RecyclerView recyclerView;
    private View view;
    private RecyclerViewAdapter adapter;
    private EditText emailEditText;
    private EditText phoneEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText re_enter_passwordEditText;

    private Button signUpBtn;

    private int lastLengthOfEmail = 0;


    private int[] activeColors = {Color.parseColor("#6adcc8"), Color.parseColor("#5dcfc0"), Color.parseColor("#50c3b8")};
    private GradientDrawable activeGradient = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, activeColors);

    private int[] deactiveColors = {Color.parseColor("#4a5b70"), Color.parseColor("#4a5b70")};
    private GradientDrawable deactiveGradient = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, deactiveColors);

    private int radius = 20;

    public UserRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
            signUpBtn.setActivated(true);
            signUpBtn.setBackground(activeGradient);
        } else {
            //signUpBtn.setActivated(false);
            signUpBtn.setBackground(deactiveGradient);
        }

    }

    private void initViews(LayoutInflater inflater, ViewGroup container) {
        view = view == null ? inflater.inflate(R.layout.user_registration_layout, container, false) : view;

        usernameEditText = (EditText) view.findViewById(R.id.nameEdittext);

        passwordEditText = (EditText) view.findViewById(R.id.passeditText);

        re_enter_passwordEditText = (EditText) view.findViewById(R.id.repasseditText);

        phoneEditText = (EditText) view.findViewById(R.id.phoneeditText);

        emailEditText = (EditText) view.findViewById(R.id.emailEdittext);

        signUpBtn = (Button) view.findViewById(R.id.signupbtn);
        activeGradient.setCornerRadius(radius);
        deactiveGradient.setCornerRadius(radius);

        signUpBtn.setBackground(deactiveGradient);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    //Toast.makeText(view.getContext(), GetFirebaseAuthInstance.getFirebaseAuthInstance(view.getContext()).toString(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(view.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                }
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
}