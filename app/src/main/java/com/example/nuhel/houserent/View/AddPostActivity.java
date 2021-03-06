package com.example.nuhel.houserent.View;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nuhel.houserent.Adapter.AddPostPopUpRViewAdapter;
import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.CustomImagePicker.Action;
import com.example.nuhel.houserent.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.roger.catloadinglibrary.CatLoadingView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class AddPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static Toolbar toolbar;
    String[] houseType = {"Duplex", "Flat", "Unit", "Sublate"};
    String[] rentType = {"Bachelor", "Family", "Anys"};
    private ImageButton gellaryPicker;
    private Spinner spinner_rentType;
    private Spinner spinner_house_type;
    private ArrayList<String> postIds;
    private Button post_ad_button;
    private String userUid;
    private ArrayList<String> downloadLinks;
    private DatabaseReference my_postlist_ref;
    private DatabaseReference all_postlist_ref;
    private Button search_place;
    private int PLACE_PICKER_REQUEST = 999;
    private ArrayList<Uri> converted_imagePaths;
    private ArrayList<Uri> original_imagePaths;

    private AddPostPopUpRViewAdapter addPostPopUpRViewAdapter;
    private StorageReference mStorageRef;

    private int cu = 0;
    private String postkey;
    private TextView place_nameShow;
    private Button postButton;


    private int[] activeColors = {Color.parseColor("#6adcc8"), Color.parseColor("#5dcfc0"), Color.parseColor("#50c3b8")};
    private GradientDrawable activeGradient = new GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, activeColors);
    private int Redius = 20;


    private EditText ted;
    private EditText ded;
    private EditText bed;
    private EditText ked;
    private EditText ved;
    private EditText baed;
    private EditText red;
    private EditText aed;

    private EditText phone;
    private EditText email;

    private Place place;

    private CatLoadingView mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpostpopup);
        userUid = GetFirebaseAuthInstance.getFirebaseAuthInstance().getCurrentUser().getUid();
        all_postlist_ref = GetFirebaseInstance.GetInstance().getReference("HomeAddList");

        initViews();
        setSupportActionBar(toolbar);
        initActions();
        initializePostIds();

        RecyclerView recyclerView = findViewById(R.id.horizontal_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(addPostPopUpRViewAdapter);


    }

    private void initViews() {


        ted = findViewById(R.id.titlet);
        ded = findViewById(R.id.desc_of_post);
        bed = findViewById(R.id.no_bedRoom);
        ked = findViewById(R.id.no_kitchen);
        ved = findViewById(R.id.no_velcony);
        baed = findViewById(R.id.no_bathRoom);
        red = findViewById(R.id.edit_rent);
        aed = findViewById(R.id.edit_advance);
        phone = findViewById(R.id.edit_phone);
        email = findViewById(R.id.edit_email);



        original_imagePaths = new ArrayList<>();
        converted_imagePaths = new ArrayList<>();
        downloadLinks = new ArrayList<>();
        toolbar = findViewById(R.id.app_bar);
        gellaryPicker = findViewById(R.id.imageaddBtn);
        search_place = findViewById(R.id.search_place);
        spinner_rentType = findViewById(R.id.spinner_rentType);
        spinner_house_type = findViewById(R.id.spinner_house_type);
        post_ad_button = findViewById(R.id.post_ad_button);
        place_nameShow = findViewById(R.id.place_nameShow);
        postButton = findViewById(R.id.post_ad_button);
        activeGradient.setCornerRadius(Redius);
        postButton.setBackground(activeGradient);
        addPostPopUpRViewAdapter = new AddPostPopUpRViewAdapter(getBaseContext());
    }


    private void initActions() {
        gellaryPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isStoragePermissionGranted()) {
                    Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                    startActivityForResult(i, 200);
                }
            }
        });
        search_place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPlace();
            }
        });

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(getBaseContext(),
                        R.layout.spinner_base, houseType);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner_house_type.setOnItemSelectedListener(this);
        spinner_house_type.setAdapter(adapter);


        ArrayAdapter<String> adapter2 =
                new ArrayAdapter<String>(getBaseContext(),
                        R.layout.spinner_base, rentType);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_rentType.setOnItemSelectedListener(this);
        spinner_rentType.setAdapter(adapter2);
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mView = new CatLoadingView();
                mView.show(getSupportFragmentManager(), "Uploading..");
                uploadImages(addPostPopUpRViewAdapter.getconverted_imagePaths());
            }
        });
    }

    private void getPlace() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (parent.getId() == R.id.spinner_rentType) {
            Toast.makeText(getBaseContext(), "Rent", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getBaseContext(), "House", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 200) {
            converted_imagePaths.clear();
            if (resultCode == RESULT_OK) {
                String[] links = data.getStringArrayExtra("all_path");
                for (int a = 0; a <= links.length - 1; a++) {
                    Uri orguri = Uri.parse(links[a]);
                    if (original_imagePaths.indexOf(orguri) < 0) {
                        original_imagePaths.add(orguri);
                        converted_imagePaths.add(getCompressedFileUri(orguri));
                    }
                }

                addPostPopUpRViewAdapter.updateView(converted_imagePaths, original_imagePaths);
            }


        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getBaseContext());
                String toastMsg = String.format("Place: %s", place.getLatLng());
                Toast.makeText(getBaseContext(), toastMsg, Toast.LENGTH_LONG).show();
                place_nameShow.setText("Add Place : " + place.getName());
                this.place = place;

            }
        }

    }

    private Uri getCompressedFileUri(Uri path) {
        File thumb_bitmap = null;
        try {
            thumb_bitmap = new Compressor(getBaseContext())
                    .setQuality(75)
                    .setMaxWidth(200)
                    .setMaxHeight(200)
                    .compressToFile(new File(path.getPath()));
            Uri uri = Uri.fromFile(thumb_bitmap);
            return uri;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }



    private void uploadImages(ArrayList<Uri> converted_imagePaths) {
        final ArrayList<Uri> uplopad_images = converted_imagePaths;

        final String title = ted.getText().toString();
        final String desc = ded.getText().toString();
        final String bedroom = bed.getText().toString();
        final String kitchen = ked.getText().toString();
        final String bathroom = baed.getText().toString();

        final String balcony =ved.getText().toString();

        final String rent = red.getText().toString();
        String houseType2;
        String rentTypes2;
        String area2;

        String lat2;
        String lan2;
        final String advance = aed.getText().toString();

        if (place == null) {
            mView.dismiss();
            return;
        }

        else {
            houseType2 = spinner_house_type.getSelectedItem().toString();
            rentTypes2 = spinner_rentType.getSelectedItem().toString();
            area2 = place.getName().toString();

            lat2 = String.valueOf(place.getLatLng().latitude);
            lan2 = String.valueOf(place.getLatLng().longitude);
        }

        final String houseType = houseType2;
        final String rentTypes = rentTypes2;
        final String area = area2;

        final String lat = lat2;
        final String lan = lan2;


        if (converted_imagePaths.size() == 0) {
            Toast.makeText(this, "Please select minimum one image", Toast.LENGTH_SHORT).show();
            mView.dismiss();
            return;
        }

        if (title.length() == 0) {
            Toast.makeText(this, "You must provide a title", Toast.LENGTH_SHORT).show();
            mView.dismiss();
            return;
        }
        if (desc.length() == 0) {
            Toast.makeText(this, "You must provide description", Toast.LENGTH_SHORT).show();
            mView.dismiss();
            return;
        }
        if (bedroom.length() == 0) {
            Toast.makeText(this, "You must provide number of bedroom", Toast.LENGTH_SHORT).show();
            mView.dismiss();
            return;
        }
        if (kitchen.length() == 0) {
            Toast.makeText(this, "You must provide number of kitchen", Toast.LENGTH_SHORT).show();
            mView.dismiss();
            return;
        }
        if (bathroom.length() == 0) {
            Toast.makeText(this, "You must provide number of bathroom", Toast.LENGTH_SHORT).show();
            mView.dismiss();
            return;
        }
        if (rent.length() == 0) {
            Toast.makeText(this, "You must provide your rent", Toast.LENGTH_SHORT).show();
            mView.dismiss();
            return;
        }
        if (advance.length() == 0) {
            Toast.makeText(this, "You must provide amount of advance", Toast.LENGTH_SHORT).show();
            mView.dismiss();
            return;
        }

        postkey = postKeyGenerator();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        StorageReference filepath = mStorageRef.child("post_images").child(postkey + "imgno-" + cu + ".jpeg");

        filepath.putFile(uplopad_images.get(cu)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    downloadLinks.add(task.getResult().getDownloadUrl().toString());
                } else {
                    Toast.makeText(getBaseContext(), "Image Upload Failed", Toast.LENGTH_SHORT).show();

                }
                cu++;
                if (cu <= uplopad_images.size() - 1) {
                    Toast.makeText(getBaseContext(), "Task Done " + String.valueOf(cu), Toast.LENGTH_SHORT).show();
                    uploadImages(uplopad_images);
                } else {
                    Toast.makeText(getBaseContext(), "Task Done Uploaded", Toast.LENGTH_SHORT).show();
                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("title", title);
                    map1.put("desc", desc);
                    map1.put("bedroom", bedroom);
                    map1.put("kitchen", kitchen);
                    map1.put("bathroom", bathroom);
                    map1.put("advance", advance);
                    map1.put("type", houseType);
                    map1.put("rentType", rentTypes);
                    map1.put("area", area);
                    map1.put("lat", lat);
                    map1.put("lan", lan);
                    map1.put("rent", rent);
                    map1.put("balcony", balcony);

                    String id = GetFirebaseAuthInstance.getFirebaseAuthInstance().getCurrentUser().getUid();
                    map1.put("owner", id);
                    for (int looper = 0; looper <= downloadLinks.size() - 1; looper++) {
                        map1.put("image" + (looper + 1), downloadLinks.get(looper));
                    }
                    all_postlist_ref.child(postkey).setValue(map1);
                    my_postlist_ref.child(postkey).setValue("f");
                    mView.dismiss();
                    finish();
                }
            }
        });
    }


    private void initializePostIds() {
        postIds = new ArrayList<>();
        my_postlist_ref = GetFirebaseInstance.GetInstance().getReference("User").child(userUid).child("posts");
        my_postlist_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                postIds.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                int index = postIds.indexOf(dataSnapshot.getKey());
                postIds.set(index, dataSnapshot.getKey());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int index = postIds.indexOf(dataSnapshot.getKey());
                postIds.remove(index);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String postKeyGenerator() {
        if (postIds.size() > 0) {
            int number = Integer.parseInt(postIds.get(postIds.size() - 1).split("PostNo-")[1]) + 1;
            return userUid + "PostNo-" + number;
        } else {
            return userUid + "PostNo-0";
        }
    }

}
