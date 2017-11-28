package com.example.nuhel.houserent.View.PopUps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.nuhel.houserent.R;

/**
 * Created by Nuhel on 11/20/2017.
 */

public class AddPostPopUpView implements
        AdapterView.OnItemSelectedListener {

    String[] country = {"India", "USA", "China", "Japan", "Other",};
    private ImageButton closebtn;
    private ImageButton gellaryPicker;
    private LayoutInflater inflater;
    private View view;
    private Context context;
    private Spinner spinner_rentType;
    private Spinner spinner_house_type;
    private Spinner spinner3;

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

            spinner_rentType = view.findViewById(R.id.spinner_rentType);
            spinner_house_type = view.findViewById(R.id.spinner_house_type);


            ArrayAdapter aa = new ArrayAdapter(context, android.R.layout.simple_spinner_item, country);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_house_type.setOnItemSelectedListener(this);
            spinner_house_type.setAdapter(aa);

            //spinner_rentType.setOnItemSelectedListener(this);
            //spinner_rentType.setAdapter(aa);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
