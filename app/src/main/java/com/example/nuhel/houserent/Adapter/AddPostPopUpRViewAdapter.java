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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.nuhel.houserent.R;

import java.util.ArrayList;

/**
 * Created by Nuhel on 11/19/2017.
 */

public class AddPostPopUpRViewAdapter extends RecyclerView.Adapter<AddPostPopUpRViewAdapter.MyViewHolder> {

    private ArrayList<Uri> data;
    private Context context;
    private Button bt;


    public AddPostPopUpRViewAdapter(Context context) {
        this.data = new ArrayList<>();
        this.context = context;
    }


    public void UpdateView(ArrayList<Uri> data) {
        int last_pos = this.data.size();
        for (Uri uri : data) {
            if (this.data.indexOf(uri) < 0) {
                this.data.add(uri);
                notifyItemInserted(this.data.size());

            }

        }
        notifyItemRangeChanged(last_pos, this.data.size());
        Toast.makeText(context, "Hello " + this.data.size(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.multi_rec_view_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Glide.with(context).load(data.get(position)).into(holder.imageView);

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                data.remove(position);
                notifyItemRemoved(position);
                //this line below gives you the animation and also updates the
                //list items after the deleted item
                notifyItemRangeChanged(position, getItemCount());

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageButton btn;

        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.imageview);
            btn = view.findViewById(R.id.dltBtn);
        }

    }

}