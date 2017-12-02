package com.example.nuhel.houserent.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.Controller.ProjectKeys;
import com.example.nuhel.houserent.R;
import com.example.nuhel.houserent.View.AdDescActivity;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Nuhel on 8/18/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading = true;
    private Context context;
    private RecyclerView recyclerView;
    private LinkedHashMap<String, HomeAddListDataModel> add_list;
    private DatabaseReference db;
    private Boolean is_first = true;
    private LinearLayoutManager linearLayoutManager;
    private String oldestPostId;

    public RecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.db = GetFirebaseInstance.GetInstance().getReference(ProjectKeys.ALLADSDIR);
        this.add_list = new LinkedHashMap<>();
        loadMoreData();
        this.recyclerView.setAdapter(this);
        init();
    }

    private void init() {
        linearLayoutManager = (LinearLayoutManager) recyclerView
                .getLayoutManager();
        recyclerView
                .addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView,
                                           int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        totalItemCount = linearLayoutManager.getItemCount();
                        lastVisibleItem = linearLayoutManager
                                .findLastVisibleItemPosition() < lastVisibleItem ? lastVisibleItem : linearLayoutManager
                                .findLastVisibleItemPosition();
                        if (loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                            loading = false;
                            loadMoreData();
                        }
                    }
                });
    }

    private void loadMoreData() {

        if (add_list.size() != 0) {
            oldestPostId = (String) add_list.keySet().toArray()[add_list.size() - 1];
        }

        ChildEventListener vl = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String key = dataSnapshot.getKey();
                if (add_list.get(key) == null) {
                    HomeAddListDataModel model = new SnapShotToDataModelParser().getModel(dataSnapshot, context);
                    add_list.put(dataSnapshot.getKey(), model);
                    notifyDataSetChanged();
                    loading = true;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String key = dataSnapshot.getKey();
                HomeAddListDataModel model = new SnapShotToDataModelParser().getModel(dataSnapshot, context);
                if (model != null) {
                    add_list.put(key, model);
                    notifyItemChanged(new ArrayList<String>(add_list.keySet()).indexOf(key));
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int position = new ArrayList<String>(add_list.keySet()).indexOf(key);
                add_list.remove(key);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, add_list.size());
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        if (is_first) {
            db.limitToFirst(7).addChildEventListener(vl);
            is_first = false;

        } else {
            db.orderByKey().startAt(oldestPostId).limitToFirst(5).addChildEventListener(vl);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.main_ads_list_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData((HomeAddListDataModel) (add_list.values().toArray()[position]), position);

    }

    @Override
    public int getItemCount() {
        return add_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private TextView area, room, type;
        private int position;
        private ArrayList<Uri> imageList;
        private HomeAddListDataModel model;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.adsListImage);
            area = itemView.findViewById(R.id.adsListAreaText);
            room = itemView.findViewById(R.id.adsListRoomText);
            type = itemView.findViewById(R.id.adsListTypeText);
            imageView.setOnClickListener(this);
        }


        public void bindData(HomeAddListDataModel data, int position) {
            this.position = position;

            model = data;

            if (data != null) {
                imageList = data.getImagelist();

                if (imageList.size() > 0) {
                    Glide.with(context)
                            .load(imageList.get(0))
                            .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                            .into(imageView);
                }

                String areaText = data.getArea() == null ? "" : data.getArea();
                String roomsText = data.getArea() == null ? "" : data.getBedroom();
                String typeText = data.getArea() == null ? "" : data.getType();

                area.setText("Area: " + areaText);
                room.setText("Rooms: " + roomsText);
                type.setText("Type: " + typeText);
            }
        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context, AdDescActivity.class);
            Bundle b = new Bundle();
            b.putParcelable("dataa", model);
            intent.putExtras(b);
            context.startActivity(intent);


        }
    }
}