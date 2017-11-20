package com.example.nuhel.houserent.View.PopUps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.example.nuhel.houserent.R;

/**
 * Created by Nuhel on 11/20/2017.
 */

public class AddPostPopUpView {

    private ImageButton closebtn;
    private ImageButton gellaryPicker;
    private LayoutInflater inflater;
    private View view;
    private Context context;

    public AddPostPopUpView(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        init();
    }

    private void init() {

        if (view == null) {
            view = inflater.inflate(R.layout.addpostpopup, null);
            gellaryPicker = view.findViewById(R.id.imageaddBtn);
            closebtn = view.findViewById(R.id.popUpClose);
        }
    }


    public ImageButton getGellaryPickerbtn() {
        return gellaryPicker;
    }

    public ImageButton getClosebtn() {
        return closebtn;
    }

    public View getView() {
        return view;
    }
}
