package com.example.nuhel.houserent.View.Fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nuhel.houserent.Adapter.ViewPagerAdapter;
import com.example.nuhel.houserent.R;

/**
 * Created by Nuhel on 10/11/2017.
 */

public class RegistrationLoginFragment extends Fragment {


    private View view;

    private Button singupBtn, singinBtn;


    private ViewPager viewPager;


    private int[] activeColors = {Color.parseColor("#6adcc8"), Color.parseColor("#5dcfc0"), Color.parseColor("#50c3b8")};
    private GradientDrawable activeGradiant = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, activeColors);

    private int[] deactiveColors = {Color.parseColor("#4a5b70"), Color.parseColor("#4a5b70")};
    private GradientDrawable deactiveGradiant = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, deactiveColors);

    public RegistrationLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = view == null ? inflater.inflate(R.layout.accountmanagement, container, false) : view;

        viewPager = (ViewPager) view.findViewById(R.id.accountViewpager);
        viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager()));

        singupBtn = (Button) view.findViewById(R.id.singupbtn);
        singinBtn = (Button) view.findViewById(R.id.singinbtn);

        activeGradiant.setCornerRadius(20);
        deactiveGradiant.setCornerRadius(20);


        singupBtn.setBackground(activeGradiant);
        singinBtn.setBackground(deactiveGradiant);


        singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singupBtn.setBackground(activeGradiant);
                singinBtn.setBackground(deactiveGradiant);

            }
        });


        singinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singinBtn.setBackground(activeGradiant);
                singupBtn.setBackground(deactiveGradiant);
            }
        });


        return view;
    }


}