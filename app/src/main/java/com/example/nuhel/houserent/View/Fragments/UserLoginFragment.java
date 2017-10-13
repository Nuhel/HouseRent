package com.example.nuhel.houserent.View.Fragments;
//uttom kumar saha

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.R;
import com.roger.catloadinglibrary.CatLoadingView;

public class UserLoginFragment extends Fragment {


    private View view;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signinBtn;
    private int lastLengthOfemail = 0;
    private CatLoadingView mView;



    public UserLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = view == null ? inflater.inflate(R.layout.user_login_layout, container, false) : view;
        emailEditText = (EditText) view.findViewById(R.id.userloginEmailEdittext);
        passwordEditText = (EditText) view.findViewById(R.id.userloginpasseditText);

        signinBtn = (Button) view.findViewById(R.id.userloginsigninbtn);
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
                    GetFirebaseAuthInstance.getFirebaseAuthInstance(view.getContext(), email, pass, mView);


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
