package com.example.nuhel.houserent.View;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.nuhel.houserent.Adapter.HomeAddListDataModel;
import com.example.nuhel.houserent.R;
import com.ihsanbal.wiv.MediaView;

import java.util.ArrayList;

public class AdDescActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ad_desc_activity);


        HomeAddListDataModel m = null;

        Bundle b = this.getIntent().getExtras();
        if (b != null) {
            m = b.getParcelable("dataa");
        }

        MediaView mediaView = findViewById(R.id.media_view);

        mediaView.setOnMediaClickListener(new MediaView.OnMediaClickListener() {
            @Override
            public void onMediaClick(View view, int index) {
                Snackbar.make(view, "onClickIndex :" + index, Snackbar.LENGTH_LONG).show();
            }
        });

        ArrayList<Uri> imageList = m.getImagelist();


        ArrayList<String> medias = new ArrayList<>();
        for (Uri uri : imageList) {
            medias.add(uri.toString());
        }
        mediaView.setMedias(medias);
    }
}
