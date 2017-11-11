package com.example.nuhel.houserent.View.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.nuhel.houserent.Adapter.OwnPostRecyclerViewAdapter;
import com.example.nuhel.houserent.Adapter.RecyclerViewAdapter;
import com.example.nuhel.houserent.Controller.FragmentControllerAfterUserLog_Reg;
import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.CustomImagePicker.Action;
import com.example.nuhel.houserent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import id.zelory.compressor.Compressor;

public class UserProfileManageFragment extends Fragment {


    private static StorageReference mStorageRef;
    private RecyclerView recyclerView;
    private View view;
    private RecyclerViewAdapter adapter;
    private Button logOutButton;
    private Button postButton;
    private ArrayList<String> postIds;
    private DatabaseReference my_postlist_ref;
    private DatabaseReference all_postlist_ref;
    private String userUid;
    private ArrayList<Uri> imagePaths;
    private int cu = 0;
    private ImageView imageView;
    private String postkey;


    public UserProfileManageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        userUid = GetFirebaseAuthInstance.getFirebaseAuthInstance().getCurrentUser().getUid();
        all_postlist_ref = GetFirebaseInstance.GetInstance().getReference("HomeAddList");
        imagePaths = new ArrayList<>();
        initializePostIds();
        view = view == null ? inflater.inflate(R.layout.user_profile_manage, container, false) : view;
        logOutButton = view.findViewById(R.id.logoutbutton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFirebaseAuthInstance.getFirebaseAuthInstance().signOut();
                ((FragmentControllerAfterUserLog_Reg) getActivity()).setFrag();
            }
        });

        postButton = view.findViewById(R.id.postbutton);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("area", "nuh");
                map1.put("image1", "http://www.canmorerealestate.com/assets/images/Featured-Listings/1041-Wilson-Way/Slider/1041-Wilson-Way-Canmore-houce.jpg");
                all_postlist_ref.child(postkey).setValue(map1);
                my_postlist_ref.child(postkey).setValue("f");
            }
        });


        view.findViewById(R.id.multiImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);

            }
        });

        imageView = view.findViewById(R.id.imageview);

        view.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImages();

            }
        });


        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        new OwnPostRecyclerViewAdapter(view.getContext(), recyclerView);
        return view;
    }

    private void initializePostIds() {
        postIds = new ArrayList<>();
        my_postlist_ref = GetFirebaseInstance.GetInstance().getReference("User").child(userUid).child("posts");
        my_postlist_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                postIds.add(dataSnapshot.getKey());
                postkey = postkeyGenerator();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int index = postIds.indexOf(dataSnapshot.getKey());
                postIds.remove(index);
                postkey = postkeyGenerator();
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
            Toast.makeText(getContext(), userUid + "PostNo-" + number, Toast.LENGTH_SHORT).show();
            return userUid + "PostNo-" + number;
        } else {
            Toast.makeText(getContext(), userUid + "PostNo-0", Toast.LENGTH_SHORT).show();
            return userUid + "PostNo-0";
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        imagePaths.clear();
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String[] links = data.getStringArrayExtra("all_path");
            for (int a = 0; a <= links.length - 1; a++) {
                File thumb_bitmap = null;
                try {
                    thumb_bitmap = new Compressor(getContext())
                            .setQuality(75)
                            .setMaxWidth(200)
                            .setMaxHeight(200)
                            .compressToFile(new File(links[a]));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imagePaths.add(Uri.fromFile(thumb_bitmap));
            }
        }
    }


    private void uploadImages() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        //Storage Reference for main image
        StorageReference filepath;
        filepath = mStorageRef.child("post_images").child(postkeyGenerator() + "imgno-" + cu + ".jpeg");
        filepath.putFile(imagePaths.get(cu)).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {

                } else {

                }
                cu++;
                if (cu <= imagePaths.size() - 1) {
                    Toast.makeText(getContext(), "Task Done " + String.valueOf(cu), Toast.LENGTH_SHORT).show();
                    uploadImages();
                } else {
                    Toast.makeText(getContext(), "Task Done Uploaded", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}