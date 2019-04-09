package com.example.nuhel.houserent.View;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.nuhel.houserent.Adapter.HomeAddListDataModel;
import com.example.nuhel.houserent.ExcelBackUp;
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

    private TextView phone;
    private TextView email;

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

        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);

        mapButton = findViewById(R.id.mapbutton);


        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            m = b.getParcelable("dataa");
            onBindData(m);
        }

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeCall();
            }
        });


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email.getText().toString(), null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "I want rent your house");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(intent, "Choose an Email client :"));

                //startActivity(getSendEmailIntent(AdDescActivity.this, email.getText().toString(),"Apply For rent", "This is body"));
            }
        });

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
        title.setText(data.getTitle());
        desc.setText(data.getDesc());
        bedroom.setText(data.getBedroom());
        kitchen.setText(data.getKitchen());
        velcony.setText(data.getVelcony());
        bathroom.setText(data.getBathroom());
        rent.setText(data.getRent());
        advance.setText(data.getAdvance());
        area.setText(data.getArea());
        renttype.setText(data.getRentType());
        phone.setText(data.getPhone());
        email.setText(data.getEmail());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            makeCall();
        }
    }

    public void makeCall(){
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phone.getText().toString()));
        if (Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(AdDescActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AdDescActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }else{
                startActivity(callIntent);
            }
        }else{
            startActivity(callIntent);
        }
    }




    public Intent getSendEmailIntent(Context context, String email,
                                     String subject, String body) {
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        try {

            // Explicitly only use Gmail to send
            emailIntent.setClassName("com.google.android.gm",
                    "com.google.android.gm.ComposeActivityGmail");

            emailIntent.setType("text/html");

            // Add the recipients
            if (email != null)
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { email });

            if (subject != null)
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        subject);

            if (body != null)
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));

            // Add the attachment by specifying a reference to our custom
            // ContentProvider
            // and the specific file of interest
            // emailIntent.putExtra(
            // Intent.EXTRA_STREAM,
            // Uri.parse("content://" + CachedFileProvider.AUTHORITY + "/"
            // + fileName));

            return emailIntent;
            //          myContext.startActivity(emailIntent);
        } catch (Exception e) {
            emailIntent.setType("text/html");

            // Add the recipients
            if (email != null)
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                        new String[] { email });

            if (subject != null)
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                        subject);

            if (body != null)
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(body));

            //          myContext.startActivity(Intent.createChooser(emailIntent,
            //                  "Share Via"));
            return emailIntent;
        }
    }
}
