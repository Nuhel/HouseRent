package com.example.nuhel.houserent.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

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
    private LinkedHashMap<String, HomeAddListDataModel> add_list2;
    private DatabaseReference db;
    private Boolean is_first = true;

    private LinearLayoutManager linearLayoutManager;

    private String oldestPostId;

    public RecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.db = GetFirebaseInstance.GetInstace().getReference("HomeAddList");
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

        if (add_list.size()!=0){
            oldestPostId = (String) add_list.keySet().toArray()[add_list.size() - 1];
        }
        ValueEventListener vl = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long aa = dataSnapshot.getChildrenCount();
                if (aa > 1) {
                    Toast.makeText(context, String.valueOf(aa), Toast.LENGTH_SHORT).show();
                } else {
                    return;
                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds != null) {
                        try {
                            HomeAddListDataModel model = getModel(ds);
                            add_list.put(ds.getKey(), model);
                            notifyItemInserted(add_list.size() - 1);
                            notifyItemRangeChanged(add_list.size() - 1, add_list.size());
                        } catch (Exception e) {

                        }
                    }
                }
                loading = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        if (is_first){
            db.limitToFirst(5).addValueEventListener(vl);
            is_first=false;
        }else {
            db.orderByKey().startAt(oldestPostId).limitToFirst(5).addValueEventListener(vl);
        }

    }








    private HomeAddListDataModel getModel(DataSnapshot ds) {

        HomeAddListDataModel model = null;
        try {

            String areatext = ds.child("area").getValue() == null ? "" : ds.child("area").getValue().toString();
            String roomstext = ds.child("room").getValue() == null ? "" : ds.child("room").getValue().toString();
            String typetext = ds.child("type").getValue() == null ? "" : ds.child("type").getValue().toString();

            String img1 = ds.child("image1").getValue() == null ? "" : ds.child("image1").getValue().toString();
            String img2 = ds.child("image2").getValue() == null ? "" : ds.child("image2").getValue().toString();
            String img3 = ds.child("image3").getValue() == null ? "" : ds.child("image3").getValue().toString();
            model = new HomeAddListDataModel();
            model.setPost_id(ds.getKey());
            model.setArea(areatext);
            model.setImage1(img1);
            model.setImage2(img2);
            model.setImage3(img3);
            model.setRoom(roomstext);
            model.setType(typetext);


        } catch (Exception e) {

        }

        return model;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.main_ads_list_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bindData((HomeAddListDataModel) (add_list.values().toArray()[position]));


    }

    @Override
    public int getItemCount() {
        return add_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView area, room, type;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.adsListImage);
            area = (TextView) itemView.findViewById(R.id.adsListAreaText);
            room = (TextView) itemView.findViewById(R.id.adsListRoomText);
            type = (TextView) itemView.findViewById(R.id.adsListTypeText);
        }


        public void bindData(HomeAddListDataModel data) {

            if (data != null) {
                Glide.with(context)
                        .load(data.getImage1())
                        .into(imageView);
                String areatext = data.getArea() == null ? "" : data.getArea();
                String roomstext = data.getArea() == null ? "" : data.getRoom();
                String typetext = data.getArea() == null ? "" : data.getType();

                area.setText("Area: " + areatext);
                room.setText("Rooms: " + roomstext);
                type.setText("Type: " + typetext);
            }


        }
    }
}