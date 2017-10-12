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

    private Button signupBtn;

    private int lastLengthOfemail = 0;


    private int Redius = 20;

    private int[] activeColors = {Color.parseColor("#6adcc8"), Color.parseColor("#5dcfc0"), Color.parseColor("#50c3b8")};
    private GradientDrawable activeGradiant = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, activeColors);

    private int[] deactiveColors = {Color.parseColor("#4a5b70"), Color.parseColor("#4a5b70")};
    private GradientDrawable deactiveGradiant = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, deactiveColors);


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
                    signupBtn.setActivated(true);
                    signupBtn.setBackground(activeGradiant);
                } else {
                    signupBtn.setActivated(false);
                    signupBtn.setBackground(deactiveGradiant);
                }
            }
        });

        activeGradiant.setCornerRadius(Redius);
        deactiveGradiant.setCornerRadius(Redius);

        signupBtn = (Button) view.findViewById(R.id.signupbtn);
        signupBtn.setBackground(deactiveGradiant);

        phoneEditText = (EditText) view.findViewById(R.id.phoneeditText);


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

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPhoneValid(CharSequence email) {
        return Patterns.PHONE.matcher(email).matches();
    }

    private boolean isRequiredGiven() {
        return isEmailValid(emailEditText.getText().toString()) && isPhoneValid(phoneEditText.getText().toString());
    }
}
