package com.example.nuhel.houserent.View.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.nuhel.houserent.Adapter.AddPostPopUpRViewAdapter;
import com.example.nuhel.houserent.Adapter.OwnPostRecyclerViewAdapter;
import com.example.nuhel.houserent.Controller.FragmentControllerAfterUserLog_Reg;
import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.CustomImagePicker.Action;
import com.example.nuhel.houserent.R;
import com.example.nuhel.houserent.View.PopUps.AddPostPopUpView;
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
import com.nightonke.boommenu.BoomButtons.ButtonPlaceEnum;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextOutsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Piece.PiecePlaceEnum;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

import static com.nightonke.boommenu.ButtonEnum.TextOutsideCircle;

public class UserProfileManageFragment extends Fragment {

    private static StorageReference mStorageRef;
    private RecyclerView recyclerView;
    private View view;
    private ArrayList<String> postIds;
    private DatabaseReference my_postlist_ref;
    private DatabaseReference all_postlist_ref;
    private String userUid;

    private ArrayList<Uri> converted_imagePaths;
    private ArrayList<Uri> original_imagePaths;

    private int cu = 0;
    private String postkey;
    private ArrayList<String> downloadLinks;
    private int PLACE_PICKER_REQUEST = 999;
    private AddPostPopUpRViewAdapter addPostPopUpRViewAdapter;


    public UserProfileManageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        userUid = GetFirebaseAuthInstance.getFirebaseAuthInstance().getCurrentUser().getUid();
        all_postlist_ref = GetFirebaseInstance.GetInstance().getReference("HomeAddList");

        original_imagePaths = new ArrayList<>();
        converted_imagePaths = new ArrayList<>();
        downloadLinks = new ArrayList<>();

        view = view == null ? inflater.inflate(R.layout.user_profile_manage, container, false) : view;


        initializePostIds();


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        new OwnPostRecyclerViewAdapter(view.getContext(), recyclerView);

        BoomMenuButton bmb = view.findViewById(R.id.bmb);

        bmb.setButtonPlaceEnum(ButtonPlaceEnum.SC_3_1);
        bmb.setPiecePlaceEnum(PiecePlaceEnum.DOT_3_1);
        bmb.setButtonEnum(TextOutsideCircle);
        for (int i = 0; i < bmb.getButtonPlaceEnum().buttonNumber(); i++) {


            TextOutsideCircleButton.Builder builder = new TextOutsideCircleButton.Builder().listener(new OnBMClickListener() {
                @Override
                public void onBoomButtonClick(int index) {

                    switch (index) {
                        case 0:
                            GetFirebaseAuthInstance.getFirebaseAuthInstance().signOut();
                            ((FragmentControllerAfterUserLog_Reg) getActivity()).setFrag();
                            break;

                        case 1:
                            showDialog();
                        case 2:
                    }

                }
            });

            if (i == 0) {
                builder.normalImageRes(R.drawable.addposticon).normalText("Log Out");
            } else if (i == 1) {
                builder.normalImageRes(R.drawable.addposticon).normalText("Add New Post");
            } else if (i == 2) {
                builder.normalImageRes(R.drawable.addposticon).normalText("Change Profile \n Photo");
            }

            bmb.addBuilder(builder);
        }

        return view;
    }

    private void showDialog() {

        AddPostPopUpView addPostPopUpView = new AddPostPopUpView(getContext());

        View view = addPostPopUpView.getView();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        final PopupWindow popupWindow = new PopupWindow(view, width, height);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(this.view, Gravity.CENTER, 0, 0);

        ImageButton button = addPostPopUpView.getGellaryPickerbtn();

        ImageButton clsbtn = addPostPopUpView.getClosebtn();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);
            }
        });


        clsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });


        addPostPopUpRViewAdapter = new AddPostPopUpRViewAdapter(getContext());

        RecyclerView recyclerView = view.findViewById(R.id.horizontal_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.setAdapter(addPostPopUpRViewAdapter);

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

    private String postkeyGenerator() {
        if (postIds.size() > 0) {
            int number = Integer.parseInt(postIds.get(postIds.size() - 1).split("PostNo-")[1]) + 1;
            return userUid + "PostNo-" + number;
        } else {
            return userUid + "PostNo-0";
        }
    }


    private void uploadImages() {
        postkey = postkeyGenerator();
        mStorageRef = FirebaseStorage.getInstance().getReference();


        StorageReference filepath = mStorageRef.child("post_images").child(postkey + "imgno-" + cu + ".jpeg");

        filepath.putFile(converted_imagePaths.get(cu)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    downloadLinks.add(task.getResult().getDownloadUrl().toString());
                } else {
                    Toast.makeText(getContext(), "Image Upload Failed", Toast.LENGTH_SHORT).show();

                }
                cu++;
                if (cu <= converted_imagePaths.size() - 1) {
                    Toast.makeText(getContext(), "Task Done " + String.valueOf(cu), Toast.LENGTH_SHORT).show();
                    uploadImages();
                } else {
                    Toast.makeText(getContext(), "Task Done Uploaded", Toast.LENGTH_SHORT).show();

                    HashMap<String, String> map1 = new HashMap<>();
                    map1.put("area", "nuh");

                    for (int looper = 0; looper <= downloadLinks.size() - 1; looper++) {
                        map1.put("image" + (looper + 1), downloadLinks.get(looper));
                    }
                    String id = GetFirebaseAuthInstance.getFirebaseAuthInstance().getCurrentUser().getUid();
                    map1.put("owner", id);
                    all_postlist_ref.child(postkey).setValue(map1);
                    my_postlist_ref.child(postkey).setValue("f");

                }
            }
        });

    }

    private void getPlace() {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(getActivity());
            startActivityForResult(intent, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        converted_imagePaths.clear();

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] links = data.getStringArrayExtra("all_path");

            for (int a = 0; a <= links.length - 1; a++) {
                Uri orguri = Uri.parse(links[a]);


                if (original_imagePaths.indexOf(orguri) < 0) {

                    File thumb_bitmap = null;
                    try {
                        thumb_bitmap = new Compressor(getContext())
                                .setQuality(75)
                                .setMaxWidth(200)
                                .setMaxHeight(200)
                                .compressToFile(new File(links[a]));


                        Uri uri = Uri.fromFile(thumb_bitmap);
                        original_imagePaths.add(orguri);
                        converted_imagePaths.add(uri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }


            }

            addPostPopUpRViewAdapter.updateView(converted_imagePaths, original_imagePaths);


        } else if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getContext());
                String toastMsg = String.format("Place: %s", place.getLatLng());
                Toast.makeText(getContext(), toastMsg, Toast.LENGTH_LONG).show();
            }
        }

    }

}