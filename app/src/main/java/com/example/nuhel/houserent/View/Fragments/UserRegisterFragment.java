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


        view = view == null ? inflater.inflate(R.layout.user_registration_layout, container, false) : view;
        phoneEditText = (EditText) view.findViewById(R.id.phoneeditText);


        emailEditText = (EditText) view.findViewById(R.id.emailEdittext);
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

                if (isRequiredGiven()) {
                    signUpBtn.setActivated(true);
                    signUpBtn.setBackground(activeGradient);
                } else {
                    signUpBtn.setActivated(false);
                    signUpBtn.setBackground(deactiveGradient);
                }
            }
        });


        activeGradient.setCornerRadius(radius);
        deactiveGradient.setCornerRadius(radius);

        signUpBtn = (Button) view.findViewById(R.id.signupbtn);
        signUpBtn.setBackground(deactiveGradient);
        phoneEditText = (EditText) view.findViewById(R.id.phoneeditText);

        passwordEditText = (EditText) view.findViewById(R.id.passeditText);
        re_enter_passwordEditText = (EditText) view.findViewById(R.id.repasseditText);

        usernameEditText = (EditText) view.findViewById(R.id.loginUserNmaeorEmailEdittext);

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
        return android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches();
    }

    private boolean isPhoneNumValid() {
        return Patterns.PHONE.matcher(phoneEditText.getText().toString()).matches();
    }

    private boolean isUserNameOk() {
        return usernameEditText.getText().length() > 5;
    }

    private boolean isPassWordOk() {
        return passwordEditText.getText().length() > 6 && passwordEditText.getText().toString().equals(re_enter_passwordEditText.getText().toString());
    }

    private boolean isRequiredGiven() {
        return isEmailValid() && isPhoneNumValid() && isPassWordOk();
    }
}
