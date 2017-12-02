package com.example.nuhel.houserent.View;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nuhel.houserent.Adapter.HomeAddListDataModel;
import com.example.nuhel.houserent.R;
import com.example.nuhel.houserent.SlideShow.ViewPagerAdapter;
import com.rd.PageIndicatorView;
import com.rd.animation.type.AnimationType;

import java.util.ArrayList;

public class AdDescActivity extends AppCompatActivity {

    private static ViewPager mPager;
    private TextView title;
    private TextView desc;
    private TextView bedroom;
    private TextView kitchen;
    private TextView velcony;
    private TextView bathroom;
    private TextView rent;
    private TextView advance;
    private PageIndicatorView pageIndicatorView;
    private TextView area;
    private TextView renttype;
    private Button mapButton;

    private HomeAddListDataModel m = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_desc_activity);

        mPager = findViewById(R.id.pager);
        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        bedroom = findViewById(R.id.bedroom);
        kitchen = findViewById(R.id.kitchen);
        velcony = findViewById(R.id.velcony);
        bathroom = findViewById(R.id.bathroom);
        rent = findViewById(R.id.rent);
        advance = findViewById(R.id.advance);
        area = findViewById(R.id.area);
        renttype = findViewById(R.id.rentType);
        mapButton = findViewById(R.id.mapbutton);



        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            m = b.getParcelable("dataa");
            onBindData(m);
        }


        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("lat", m.getLat());
                intent.putExtra("lan", m.getLan());
                intent.putExtra("name", m.getArea());
                startActivity(intent);
            }
        });

        ArrayList<Uri> imageList = m.getImagelist();
        mPager.setAdapter(new ViewPagerAdapter(this, imageList));
        pageIndicatorView = findViewById(R.id.pageIndicatorView);
        pageIndicatorView.setViewPager(mPager);
        pageIndicatorView.setAnimationType(AnimationType.SWAP);
    }

    private void onBindData(HomeAddListDataModel data) {
        title.setText(data.getTitle().toString());
        desc.setText(data.getDesc());
        bedroom.setText(data.getBedroom());
        kitchen.setText(data.getKitchen());
        velcony.setText(data.getVelcony());
        bathroom.setText(data.getBathroom());
        rent.setText(data.getRent());
        advance.setText(data.getAdvance());
        area.setText(data.getArea());
        renttype.setText(data.getRentType());
    }
}
