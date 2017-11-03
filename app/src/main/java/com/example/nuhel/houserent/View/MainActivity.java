package com.example.nuhel.houserent.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nuhel.houserent.Controller.FragmentControllerAfterUserLog_Reg;
import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.CustomViews.CustmoCIV;
import com.example.nuhel.houserent.R;
import com.example.nuhel.houserent.View.Fragments.AdList;
import com.example.nuhel.houserent.View.Fragments.RegistrationLoginFragment;
import com.example.nuhel.houserent.View.Fragments.UserProfileManageFragment;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentControllerAfterUserLog_Reg, Serializable {

    private static Handler mHandler;
    private static AdList adListFragment = null;
    private static Toolbar toolbar;
    private static RegistrationLoginFragment registrationLoginFragment;
    private static Serializable serializable;
    private static FirebaseAuth mAuth = null;
    private static CustmoCIV nav_userPhoto;
    private static TextView nav_username;
    private static DrawerLayout drawer;
    private static CustmoCIV nav_user_pic_management;
    private static CustmoCIV hide1;
    private static CustmoCIV hide2;
    private static int vfl = 0;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = GetFirebaseAuthInstance.getFirebaseAuthInstance();
        addTollBar();

        serializable = this;
        mHandler = new Handler();

        setAddListFrag();
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nav_userPhoto = navigationView.getHeaderView(0).findViewById(R.id.nav_userphoto);
        nav_username = navigationView.getHeaderView(0).findViewById(R.id.nav_username);
        nav_user_pic_management = navigationView.getHeaderView(0).findViewById(R.id.nav_user_pic_management);

        hide1 = navigationView.getHeaderView(0).findViewById(R.id.hide1);
        hide2 = navigationView.getHeaderView(0).findViewById(R.id.hide2);


        nav_user_pic_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hide1.getVisibility() == View.VISIBLE) {

                    hide1.animate().cancel();
                    hide1.animate().setListener(null);
                    hide2.animate().cancel();
                    hide2.animate().setListener(null);


                    RotateAnimation rotate = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(1000);
                    rotate.setInterpolator(new LinearInterpolator());


                    AnimationSet animationSet = new AnimationSet(true);
                    animationSet.addAnimation(rotate);
                    animationSet.addAnimation(outToLeftAnimation());

                    hide1.startAnimation(animationSet);
                    hide1.animate()
                            .alpha(0.0f)
                            .setDuration(700).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            hide1.setVisibility(View.GONE);
                        }
                    });


                    hide2.startAnimation(animationSet);
                    hide2.animate()
                            .alpha(0.0f)
                            .setDuration(700).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            hide2.setVisibility(View.GONE);
                        }
                    });


                } else {

                    hide1.animate().cancel();
                    hide1.animate().setListener(null);
                    hide1.setVisibility(View.VISIBLE);


                    RotateAnimation rotate = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    rotate.setDuration(1000);
                    rotate.setInterpolator(new LinearInterpolator());


                    AnimationSet animationSet = new AnimationSet(true);
                    animationSet.addAnimation(rotate);
                    animationSet.addAnimation(inFromLeftAnimation());


                    hide1.startAnimation(animationSet);
                    hide1.animate()
                            .alpha(1f)
                            .setDuration(1000).setListener(null);

                    hide2.animate().cancel();
                    hide2.animate().setListener(null);
                    hide2.setVisibility(View.VISIBLE);
                    hide2.startAnimation(animationSet);
                    hide2.animate()
                            .alpha(1f)
                            .setDuration(1000).setListener(null);

                }
            }
        });

        setuserdisplay();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            setRegisterFragment();
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            setAddListFrag();
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setAddListFrag() {
        adListFragment = adListFragment == null ? new AdList() : adListFragment;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().replace(R.id.container_frags, new AdList())
                        .commit();
            }
        };

        if (runnable != null) {
            mHandler.post(runnable);
            toolbar.setTitle("Ad Lists");
        }
    }


    private void setRegisterFragment() {
        final Context context = getBaseContext();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                bundle.putSerializable("serializable", serializable);
                if (mAuth.getCurrentUser() == null) {
                    registrationLoginFragment = RegistrationLoginFragment.newInstance(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_frags, registrationLoginFragment)
                            .commit();
                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_frags, UserProfileManageFragment.newInstance(bundle))
                            .commit();
                }
            }
        };

        if (runnable != null) {
            toolbar.setTitle("Create Account");
            mHandler.post(runnable);
        }
    }

    private void addTollBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void setFrag() {
        setAddListFrag();
        setuserdisplay();
    }

    private void setuserdisplay() {
        if (mAuth.getCurrentUser() != null) {
            Glide.with(getBaseContext()).load(mAuth.getCurrentUser().getPhotoUrl().toString()).into(nav_userPhoto);
            nav_username.setText(mAuth.getCurrentUser().getDisplayName());
            Toast.makeText(getBaseContext(), mAuth.getCurrentUser().getPhotoUrl().toString(), Toast.LENGTH_SHORT).show();
        } else {
            int drawableResourceId = this.getResources().getIdentifier("usericon", "drawable", this.getPackageName());
            Glide.with(getBaseContext()).load(drawableResourceId).into(nav_userPhoto);

        }
    }


    // 1)inFromRightAnimation

    private Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(1000);
        inFromRight.setInterpolator(new AccelerateInterpolator());
        return inFromRight;
    }

    //2)outToLeftAnimation
    private Animation outToLeftAnimation() {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(1000);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

//3)inFromLeftAnimation

    private Animation inFromLeftAnimation() {
        Animation inFromLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromLeft.setDuration(1000);
        inFromLeft.setInterpolator(new AccelerateInterpolator());
        return inFromLeft;
    }

//4)outToRightAnimation

    private Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(1000);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }

}
