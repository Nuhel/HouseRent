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

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.example.nuhel.houserent.Controller.GetFirebaseAuthInstance;
import com.example.nuhel.houserent.Controller.GetFirebaseInstance;
import com.example.nuhel.houserent.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Nuhel on 8/18/2017.
 */

public class OwnPostRecyclerViewAdapter extends RecyclerView.Adapter<OwnPostRecyclerViewAdapter.ViewHolder> {

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
    private ChildEventListener vl;
    private ValueEventListener as;

    public OwnPostRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.db = GetFirebaseInstance.GetInstance().getReference("HomeAddList");
        this.add_list = new LinkedHashMap<>();
        this.recyclerView.setAdapter(this);
        init();
    }


    private void init() {

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                String key = dataSnapshot.getKey();
                int position = new ArrayList<String>(add_list.keySet()).indexOf(key);
                if(position>0){
                    add_list.remove(key);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, add_list.size());
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        as = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HomeAddListDataModel model = getModel(dataSnapshot);
                add_list.put(dataSnapshot.getKey(), model);
                notifyDataSetChanged();
                loading = true;
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


        String id = GetFirebaseAuthInstance.getFirebaseAuthInstance().getCurrentUser().getUid();
        GetFirebaseInstance.GetInstance().getReference("User").child(id).child("posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Toast.makeText(context, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                db.child(dataSnapshot.getKey()).addValueEventListener(as);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

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
        });



    }


    private HomeAddListDataModel getModel(DataSnapshot ds) {

        HomeAddListDataModel model = null;
        try {

            String areaText = ds.child("area").getValue() == null ? "" : ds.child("area").getValue().toString();
            String roomsText = ds.child("room").getValue() == null ? "" : ds.child("room").getValue().toString();
            String typeText = ds.child("type").getValue() == null ? "" : ds.child("type").getValue().toString();
            String img1 = ds.child("image1").getValue() == null ? "" : ds.child("image1").getValue().toString();
            String img2 = ds.child("image2").getValue() == null ? "" : ds.child("image2").getValue().toString();
            String img3 = ds.child("image3").getValue() == null ? "" : ds.child("image3").getValue().toString();
            model = new HomeAddListDataModel();
            model.setPost_id(ds.getKey());
            model.setArea(areaText);
            model.setImage1(img1);
            model.setImage2(img2);
            model.setImage3(img3);
            model.setRoom(roomsText);
            model.setType(typeText);


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
            imageView = itemView.findViewById(R.id.adsListImage);
            area = itemView.findViewById(R.id.adsListAreaText);
            room = itemView.findViewById(R.id.adsListRoomText);
            type = itemView.findViewById(R.id.adsListTypeText);
        }


        public void bindData(HomeAddListDataModel data) {
            if (data != null) {
                Glide.with(context)
                        .load(data.getImage1())
                        .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                        .into(imageView);
                String areaText = data.getArea() == null ? "" : data.getArea();
                String roomsText = data.getArea() == null ? "" : data.getRoom();
                String typeText = data.getArea() == null ? "" : data.getType();
                area.setText("Area: " + areaText);
                room.setText("Rooms: " + roomsText);
                type.setText("Type: " + typeText);
            }
        }
    }


}