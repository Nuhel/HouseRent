package com.example.nuhel.houserent.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nuhel.houserent.View.Fragments.UserRegisterFragment;

/**
 * Created by Nuhel on 10/11/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new UserRegisterFragment();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
