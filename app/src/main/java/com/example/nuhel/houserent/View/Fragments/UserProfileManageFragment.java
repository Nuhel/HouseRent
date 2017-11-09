package com.example.nuhel.houserent.View.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nuhel.houserent.Adapter.OwnPostRecyclerViewAdapter;
import com.example.nuhel.houserent.Adapter.RecyclerViewAdapter;
import com.example.nuhel.houserent.Controller.FragmentControllerAfterUserLog_Reg;
import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

public class UserProfileManageFragment extends Fragment {


    private RecyclerView recyclerView;
    private View view;
    private RecyclerViewAdapter adapter;
    private FragmentControllerAfterUserLog_Reg fragmentControllerAfterUserLogReg;
    private Button logOutButton;
    private Button postButton;
    private ArrayList<String> postIds;
    private DatabaseReference my_postlist_ref;
    private DatabaseReference all_postlist_ref;

    public UserProfileManageFragment() {
        // Required empty public constructor
    }

    public static UserProfileManageFragment newInstance(Bundle bundle) {
        UserProfileManageFragment userLoginFragment = new UserProfileManageFragment();
        userLoginFragment.setArguments(bundle);
        return userLoginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        all_postlist_ref = GetFirebaseInstance.GetInstance().getReference("HomeAddList");
        initializePostIds();
        view = view == null ? inflater.inflate(R.layout.user_profile_manage, container, false) : view;
        fragmentControllerAfterUserLogReg = (FragmentControllerAfterUserLog_Reg) getArguments().getSerializable("serializable");
        logOutButton = view.findViewById(R.id.logoutbutton);
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetFirebaseAuthInstance.getFirebaseAuthInstance().signOut();
                fragmentControllerAfterUserLogReg.setFrag();
            }
        });


        postButton = view.findViewById(R.id.postbutton);


        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> map1 = new HashMap<>();
                map1.put("area", "nuh");
                map1.put("image1", "http://www.canmorerealestate.com/assets/images/Featured-Listings/1041-Wilson-Way/Slider/1041-Wilson-Way-Canmore-houce.jpg");
                all_postlist_ref.child("hello").setValue(map1);
                my_postlist_ref.child("hello").setValue("f");
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        new OwnPostRecyclerViewAdapter(view.getContext(), recyclerView);

        return view;
    }

    private void initializePostIds() {
        postIds = new ArrayList<>();
        String id = GetFirebaseAuthInstance.getFirebaseAuthInstance().getCurrentUser().getUid();
        my_postlist_ref = GetFirebaseInstance.GetInstance().getReference("User").child(id).child("posts");
        my_postlist_ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                postIds.add(dataSnapshot.getKey());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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

}