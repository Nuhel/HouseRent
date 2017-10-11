package com.example.nuhel.houserent.View.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
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
    private AutoCompleteTextView ac = null;

    private int lastLengthOfemail = 0;

    private String[] domains = {
            "@gmail.com",
            "@yahoo.com"
    };

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
