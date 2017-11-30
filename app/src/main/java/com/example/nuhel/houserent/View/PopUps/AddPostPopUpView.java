package com.example.nuhel.houserent.View.PopUps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nuhel.houserent.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Nuhel on 11/20/2017.
 */

public class AddPostPopUpView implements
        AdapterView.OnItemSelectedListener, OnMapReadyCallback {

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

    private FragmentManager manager;


    private GoogleMap mMap;
    private Button search_place;

    private Marker marker;

    public AddPostPopUpView(Context context, FragmentManager manager) {
        this.manager = manager;
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


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    SupportMapFragment mapFragment = (SupportMapFragment) manager
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                    init();
                } else {
                    ActivityCompat.requestPermissions(((Activity) context), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                }


            } else {
                SupportMapFragment mapFragment = (SupportMapFragment) manager
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

            }


            search_place = view.findViewById(R.id.search_place);

        }
    }


    public Button getPostButton() {
        return post_ad_button;
    }


    public Button getSearchButton() {
        return search_place;
    }

    public GoogleMap getmMap() {
        return mMap;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng latLng = new LatLng(24.8998373f, 91.826884f);
        updateMap(latLng, "Sylhet");


        Toast.makeText(context, "On map ready", Toast.LENGTH_SHORT).show();
    }


    public void updateMap(LatLng latLng, String name) {
        if (marker != null) {
            marker.remove();
        }


        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        marker = mMap.addMarker(new MarkerOptions().position(latLng).title(name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
    }
}
