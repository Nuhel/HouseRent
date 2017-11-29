package com.example.nuhel.houserent.View.PopUps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

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

    private Button post_ad_button;

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

            post_ad_button = view.findViewById(R.id.post_ad_button);


            ArrayAdapter<String> adapter =
                    new ArrayAdapter<String>(context,
                            android.R.layout.simple_spinner_item, country);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spinner_house_type.setOnItemSelectedListener(this);
            spinner_house_type.setAdapter(adapter);


            spinner_rentType.setOnItemSelectedListener(this);
            spinner_rentType.setAdapter(adapter);
        }
    }


    public Button getPostButton() {
        return post_ad_button;
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

        if (parent.getId() == R.id.spinner_rentType) {
            Toast.makeText(context, "Rent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "House", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
