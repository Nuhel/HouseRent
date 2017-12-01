package com.example.nuhel.houserent.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nuhel.houserent.R;

import java.util.ArrayList;

/**
 * Created by Nuhel on 11/19/2017.
 */

public class AddPostPopUpRViewAdapter extends RecyclerView.Adapter<AddPostPopUpRViewAdapter.MyViewHolder> {

    private ArrayList<Uri> converted_imagePaths;
    private Context context;
    private Button bt;

    private ArrayList<Uri> original_imagePaths;


    public AddPostPopUpRViewAdapter(Context context) {
        this.converted_imagePaths = new ArrayList<>();
        this.context = context;
    }


    public void updateView(ArrayList<Uri> data, ArrayList<Uri> original_imagePaths) {


        int old_pos = converted_imagePaths.size();
        for (Uri uri : data) {
            this.converted_imagePaths.add(uri);
        }

        this.original_imagePaths = original_imagePaths;

        int last_pos = converted_imagePaths.size();

        notifyItemRangeChanged(old_pos, last_pos);
    }

    public ArrayList<Uri> getconverted_imagePaths() {
        return converted_imagePaths;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_rec_view_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        Glide.with(context).load(converted_imagePaths.get(position)).into(holder.imageView);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                converted_imagePaths.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                original_imagePaths.remove(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return converted_imagePaths.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageButton btn;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageview);
            btn = view.findViewById(R.id.dltBtn);
        }

    }

}