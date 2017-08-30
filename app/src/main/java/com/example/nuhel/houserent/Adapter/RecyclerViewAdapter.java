package com.example.nuhel.houserent.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.nuhel.houserent.R;

import java.util.ArrayList;

/**
 * Created by Nuhel on 8/18/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    private Context context;
    private ArrayList<HomeAddListDataModel> dataList;

    public RecyclerViewAdapter(Context context, ArrayList<HomeAddListDataModel> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.main_ads_list_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
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
            Glide.with(context)
                    .load(data.getImage1())
                    .into(imageView);
            area.setText("Area: " + data.getArea());
            room.setText("Rooms: " + data.getRoom());
            type.setText("Type: " + data.getType());

        }
    }
}
