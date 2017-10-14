package com.example.nuhel.houserent.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nuhel.houserent.View.Fragments.UserLoginFragment;
import com.example.nuhel.houserent.View.Fragments.UserRegisterFragment;

/**
 * Created by Nuhel on 10/11/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private UserRegisterFragment userRegisterFragment;
    private UserLoginFragment userLoginFragment;

    public ViewPagerAdapter(FragmentManager fm, Bundle bundle) {
        super(fm);
        userLoginFragment = UserLoginFragment.newInstance(bundle);
        userRegisterFragment = UserRegisterFragment.newInstance(bundle);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return userRegisterFragment;
        } else {
            return userLoginFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
