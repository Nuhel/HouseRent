package com.example.nuhel.houserent.View.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.nuhel.houserent.Adapter.RecyclerViewAdapter;
import com.example.nuhel.houserent.R;

public class AdList extends Fragment {


    private RecyclerView recyclerView;
    private View view;
    private RecyclerViewAdapter adapter;
    public AdList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = view == null ? inflater.inflate(R.layout.activity_main_ads_list, container, false) : view;
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(view.getContext(), recyclerView);
        return view;
    }

    public void setAdapter(RecyclerViewAdapter adapter) {
        this.adapter = adapter;
        if (this.adapter == null) {
            Toast.makeText(view.getContext(), " Null", Toast.LENGTH_SHORT).show();
        } else {
            if (recyclerView == null) {
                Toast.makeText(view.getContext(), "R Null", Toast.LENGTH_SHORT).show();
            }
            // recyclerView.setAdapter(adapter);
            //this.adapter.setRecyclerView(recyclerView,this.adapter);
        }
    }
}
