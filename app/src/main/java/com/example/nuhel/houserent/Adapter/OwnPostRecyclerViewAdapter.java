package com.example.nuhel.houserent.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
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
import com.example.nuhel.houserent.Controller.ProjectKeys;
import com.example.nuhel.houserent.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class OwnPostRecyclerViewAdapter extends RecyclerView.Adapter<OwnPostRecyclerViewAdapter.ViewHolder> {

    private Context context;
    private RecyclerView recyclerView;
    private LinkedHashMap<String, HomeAddListDataModel> add_list;
    private DatabaseReference db;

    public OwnPostRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.db = GetFirebaseInstance.GetInstance().getReference(ProjectKeys.ALLADSDIR);
        this.add_list = new LinkedHashMap<>();
        this.recyclerView.setAdapter(this);
        init();
    }

    private void init() {

        String id = GetFirebaseAuthInstance.getFirebaseAuthInstance().getCurrentUser().getUid();

        Query query = db.orderByChild(ProjectKeys.OWNERKEY).equalTo(id);

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                HomeAddListDataModel model = new SnapShotToDataModelParser().getModel(dataSnapshot, context);
                add_list.put(dataSnapshot.getKey(), model);
                notifyDataSetChanged();
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

                if (position >= 0) {
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

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.own_post_list_item, null);
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
        private int pos;
        private ImageView imageView;
        private TextView area, room, type;
        private View itemView;
        private ArrayList<Uri> imageList;
        private String post_id;

        private CircleImageView deleteIcon;
        private CircleImageView editIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageView = itemView.findViewById(R.id.adsListImage);
            area = itemView.findViewById(R.id.adsListAreaText);
            room = itemView.findViewById(R.id.adsListRoomText);
            type = itemView.findViewById(R.id.adsListTypeText);

            deleteIcon = itemView.findViewById(R.id.deleteIcon);
            editIcon = itemView.findViewById(R.id.editIcon);

            deleteIcon.setOnClickListener(this);
            editIcon.setOnClickListener(this);

        }


        public void bindData(HomeAddListDataModel data, int position) {
            imageList = data.getImagelist();

            if (data != null) {
                pos = position;

                if (imageList.size() > 0) {
                    Glide.with(context)
                            .load(imageList.get(0))
                            .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                            .into(imageView);
                }


                String areaText = data.getArea() == null ? "" : data.getArea();
                String roomsText = data.getArea() == null ? "" : data.getRoom();
                String typeText = data.getArea() == null ? "" : data.getType();
                post_id = data.getPost_id() == null ? "" : data.getPost_id();

                area.setText("Area: " + areaText);
                room.setText("Rooms: " + roomsText);
                type.setText("Type: " + typeText);
            }
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.deleteIcon:

                    showConfirmDialog("Sure to delete post?");

                    break;

                case R.id.editIcon:
                    Toast.makeText(context, "Edit" + post_id, Toast.LENGTH_SHORT).show();
                    break;
            }
        }


        private void showConfirmDialog(String title) {

            int flag = 0;

            if (title.contains("delete")) {
                flag = 0;
            } else if (title.contains("change")) {
                flag = 1;
            }

            final int fl = flag;

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(title)
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (fl == 0) {
                                deletePost();
                            } else if (fl == 1) {


                            }
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

        }


        private void deletePost() {
            for (int looper = 0; looper <= imageList.size() - 1; looper++) {
                final int poss = looper;
                StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageList.get(looper).toString());
                storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Deleted image " + poss, Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!

                    }
                });
            }


            GetFirebaseInstance.GetInstance().getReference(ProjectKeys.ALLADSDIR).child(post_id).removeValue();

        }

    }


}