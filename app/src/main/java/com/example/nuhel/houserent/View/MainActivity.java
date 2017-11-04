package com.example.nuhel.houserent.View;

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
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        nav_userPhoto = navigationView.getHeaderView(0).findViewById(R.id.nav_userphoto);
        nav_username = navigationView.getHeaderView(0).findViewById(R.id.nav_username);

        hide1 = navigationView.getHeaderView(0).findViewById(R.id.hide1);
        hide2 = navigationView.getHeaderView(0).findViewById(R.id.hide2);



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
        setuserdisplay();
    }

    @Override
    public void setFrag() {
        setAddListFrag();

    }


    private void setuserdisplay() {

        if (mAuth.getCurrentUser() != null) {
            Glide.with(getBaseContext()).load(mAuth.getCurrentUser().getPhotoUrl()).into(nav_userPhoto);
            nav_username.setText(mAuth.getCurrentUser().getDisplayName());

            Toast.makeText(getBaseContext(), mAuth.getCurrentUser().getPhotoUrl().toString(), Toast.LENGTH_SHORT).show();
        } else {
            int drawableResourceId = this.getResources().getIdentifier("usericon", "drawable", this.getPackageName());
            Glide.with(getBaseContext()).load(drawableResourceId).into(nav_userPhoto);

        }
    }


}
