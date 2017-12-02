package com.example.nuhel.houserent.SlideShow;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.nuhel.houserent.R;

import java.util.ArrayList;

/**
 * Created by Nuhel on 11/16/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<Uri> images;
    LayoutInflater inflater;

    public ViewPagerAdapter(Context context, ArrayList<Uri> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);

    }


    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.gellary_base, view, false);
        ImageView imageView = myImageLayout.findViewById(R.id.imageView);
        Glide.with(context).load(images.get(position)).into(imageView);
        view.addView(myImageLayout);
        return myImageLayout;
    }
}
