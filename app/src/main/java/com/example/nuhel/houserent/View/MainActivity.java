package com.example.nuhel.houserent.View;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nuhel.houserent.Controller.FragmentControllerAfterUserLog_Reg;
import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.Controller.ProjectKeys;
import com.example.nuhel.houserent.R;
import com.example.nuhel.houserent.View.CustomViews.MyAnimations;
import com.example.nuhel.houserent.View.Fragments.AdList;
import com.example.nuhel.houserent.View.Fragments.RegistrationLoginFragment;
import com.example.nuhel.houserent.View.Fragments.UserProfileManageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentControllerAfterUserLog_Reg {

    private static Handler mHandler;
    private static AdList adListFragment = null;
    private static Toolbar toolbar;
    private static RegistrationLoginFragment registrationLoginFragment;
    private static FirebaseAuth mAuth = null;

    private static CircleImageView nav_userPhoto;
    private static TextView nav_username;
    private static DrawerLayout drawer;

    private static CircleImageView nav_user_pic_management;
    private static CircleImageView hide1;
    private static CircleImageView hide2;
    private static StorageReference mStorageRef;
    private static DatabaseReference mDatabase;
    private static int drawableResourceId;

    ImageLoader imageLoader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initImageLoader();

        drawableResourceId = (getBaseContext()).getResources().getIdentifier("usericon", "drawable", (getBaseContext()).getPackageName());
        mAuth = GetFirebaseAuthInstance.getFirebaseAuthInstance();

        addTollBar();
        mHandler = new Handler();

        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        nav_userPhoto = header.findViewById(R.id.nav_userphoto);
        nav_username = header.findViewById(R.id.nav_username);
        nav_user_pic_management = header.findViewById(R.id.nav_user_pic_management);
        hide1 = header.findViewById(R.id.nav_user_pic_change);
        hide2 = header.findViewById(R.id.nav_user_pic_delete);
        hide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropWindowSize(500, 500)
                        .start((Activity) view.getContext());
            }
        });

        hide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUserPhoto();
            }
        });

        nav_user_pic_management.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hide1.getVisibility() == View.VISIBLE) {
                    doOutAnim(hide1);
                    doOutAnim(hide2);
                } else {
                    doInAnim(hide1);
                    doInAnim(hide2);
                }
            }
        });

        setAddListFrag();
    }

    private void setRegisterFragment() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (mAuth.getCurrentUser() == null) {
                    registrationLoginFragment = new RegistrationLoginFragment();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_frags, registrationLoginFragment)
                            .commit();

                } else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container_frags, new UserProfileManageFragment())
                            .commit();
                }
            }
        };
        if (runnable != null) {
            toolbar.setTitle("Create Account");
            mHandler.post(runnable);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        setupUserDisplay();
    }


    public void setFrag() {
        nav_user_pic_management.setVisibility(View.VISIBLE);
        setAddListFrag();
        setupUserDisplay();

    }

    private void setupUserDisplay() {
        if (mAuth.getCurrentUser() != null) {
            GetFirebaseInstance.GetInstance().getReference(ProjectKeys.USERDIR).child(mAuth.getCurrentUser().getUid().toString()).child(ProjectKeys.USERDIRPROFILEIMAGE).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot != null) {

                        try {
                            String imagepath = dataSnapshot.getValue().toString();
                            if (imagepath.equals(ProjectKeys.NOIMG)) {
                                setImage(drawableResourceId, nav_userPhoto);
                            } else {
                                setImage(imagepath, nav_userPhoto);
                            }
                        } catch (Exception e) {
                            setImage(drawableResourceId, nav_userPhoto);
                        }

                    } else {
                        setImage(drawableResourceId, nav_userPhoto);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    setImage(drawableResourceId, nav_userPhoto);
                }

            });

            nav_username.setText(mAuth.getCurrentUser().getDisplayName());
            nav_user_pic_management.setVisibility(View.VISIBLE);
        } else {

            setImage(drawableResourceId, nav_userPhoto);
            nav_user_pic_management.setVisibility(View.GONE);
            hide1.setVisibility(View.GONE);
            hide2.setVisibility(View.GONE);
        }
    }

    private void setImage(Object model, ImageView v) {
        Glide.with(v.getContext()).load(model).into(v);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                File thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(this)
                            .setQuality(75)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .compressToFile(new File(resultUri.getPath()));

                } catch (IOException e) {
                    e.printStackTrace();
                }

                mStorageRef = FirebaseStorage.getInstance().getReference();
                //Storage Reference for main image
                StorageReference filepath = mStorageRef.child(ProjectKeys.PROFILEIMAGEDIR).child(mAuth.getCurrentUser().getUid() + ProjectKeys.JPGIMAGEFORMAT);

                //Now first upload the main image
                filepath.putFile(Uri.fromFile(thumb_bitmap)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final String download_url = task.getResult().getDownloadUrl().toString();
                            Map map = new HashMap();
                            map.put(ProjectKeys.USERDIRPROFILEIMAGE, download_url);
                            mDatabase = GetFirebaseInstance.GetInstance().getReference().child(ProjectKeys.USERDIR).child(mAuth.getCurrentUser().getUid());
                            mDatabase.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {

                                    }
                                }
                            });
                        } else {
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }

    private void addTollBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void doInAnim(View v) {
        v.animate().cancel();
        v.animate().setListener(null);
        v.setVisibility(View.VISIBLE);
        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(MyAnimations.RotateRight());
        animationSet.addAnimation(MyAnimations.inFromLeftAnimation());

        v.startAnimation(animationSet);
        v.animate()
                .alpha(1f)
                .setDuration(1000).setListener(null);
    }

    private void doOutAnim(final View v) {
        v.animate().cancel();
        v.animate().setListener(null);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(MyAnimations.RotateLeft());
        animationSet.addAnimation(MyAnimations.outToLeftAnimation());
        v.startAnimation(animationSet);
        v.animate()
                .alpha(0.0f)
                .setDuration(700).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                v.setVisibility(View.GONE);
            }
        });
    }

    private void deleteUserPhoto() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Storage Reference for main image
        StorageReference filepath = mStorageRef.child(ProjectKeys.PROFILEIMAGEDIR).child(mAuth.getCurrentUser().getUid() + ProjectKeys.JPGIMAGEFORMAT);

        filepath.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Map map = new HashMap();
                map.put(ProjectKeys.USERDIRPROFILEIMAGE, ProjectKeys.NOIMG);
                mDatabase = GetFirebaseInstance.GetInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());
                mDatabase.updateChildren(map);
            }
        });
    }

    private void initImageLoader() {
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc().imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                this).defaultDisplayImageOptions(defaultOptions).memoryCache(
                new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }

}
