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
import android.widget.TextView;

import com.example.nuhel.houserent.Adapter.ViewPagerAdapter;
import com.example.nuhel.houserent.R;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;
/**
 * Created by Nuhel on 10/11/2017.
 */

public class RegistrationLoginFragment extends Fragment {


    private View view;
    private Button signupBtn, signinBtn;
    private ViewPager viewPager;
    private TextView signinTextView, signupTextView;
    private PageIndicatorView pageIndicatorView;
    private ViewPagerAdapter viewPagerAdapter;
    private int Redius = 20;


    private int[] activeColors = {Color.parseColor("#6adcc8"), Color.parseColor("#5dcfc0"), Color.parseColor("#50c3b8")};
    private GradientDrawable activeGradient = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, activeColors);

    private int[] deactiveColors = {Color.parseColor("#4a5b70"), Color.parseColor("#4a5b70")};
    private GradientDrawable deactiveGradient = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, deactiveColors);

    public RegistrationLoginFragment() {
        // Required empty public constructor
    }

    public static RegistrationLoginFragment newInstance(Bundle bundle) {
        RegistrationLoginFragment registrationLoginFragment = new RegistrationLoginFragment();
        registrationLoginFragment.setArguments(bundle);
        return registrationLoginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = view == null ? inflater.inflate(R.layout.accountmanagement, container, false) : view;

        viewPager = (ViewPager) view.findViewById(R.id.accountViewpager);
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), getArguments());
        viewPager.setAdapter(viewPagerAdapter);

        pageIndicatorView = (PageIndicatorView) view.findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(viewPager);
        pageIndicatorView.setAnimationType(AnimationType.SWAP);

        signinTextView = (TextView) view.findViewById(R.id.signinTextView);
        signupTextView = (TextView) view.findViewById(R.id.signupTextView);

        signupBtn = (Button) view.findViewById(R.id.signupbtn);
        signinBtn = (Button) view.findViewById(R.id.signinbtn);

        activeGradient.setCornerRadius(Redius);
        deactiveGradient.setCornerRadius(Redius);


        signupBtn.setBackground(deactiveGradient);
        signinBtn.setBackground(activeGradient);


        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signinBtn.setBackground(activeGradient);
                signupBtn.setBackground(deactiveGradient);

                signupTextView.setTextColor(Color.parseColor("#ffffff"));
                signinTextView.setTextColor(Color.parseColor("#526174"));

                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                }
            }
        });


        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupBtn.setBackground(activeGradient);
                signinBtn.setBackground(deactiveGradient);

                signinTextView.setTextColor(Color.parseColor("#ffffff"));
                signupTextView.setTextColor(Color.parseColor("#526174"));

                if (viewPager.getCurrentItem() != 1) {
                    viewPager.setCurrentItem(1);
                }
            }
        });


        return view;
    }

}