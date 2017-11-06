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
import com.example.nuhel.houserent.R;

public class UserProfileManageFragment extends Fragment {


    private RecyclerView recyclerView;
    private View view;
    private RecyclerViewAdapter adapter;
    private FragmentControllerAfterUserLog_Reg fragmentControllerAfterUserLogReg;
    private Button logOutButton;

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

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        new OwnPostRecyclerViewAdapter(view.getContext(), recyclerView);

        return view;
    }

}