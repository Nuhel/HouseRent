package com.example.nuhel.houserent.Adapter;

import android.content.Context;
import android.net.Uri;

import com.example.nuhel.houserent.Controller.ProjectKeys;
import com.google.firebase.database.DataSnapshot;

/**
 * Created by Nuhel on 11/13/2017.
 */

public class SnapShotToDataModelParser {

    public HomeAddListDataModel getModel(DataSnapshot ds, Context con) {

        HomeAddListDataModel model = null;
        try {
            String areaText = ds.child(ProjectKeys.AREARKEY).getValue() == null ? "" : ds.child(ProjectKeys.AREARKEY).getValue().toString();
            String roomsText = ds.child(ProjectKeys.ROOMKEY).getValue() == null ? "" : ds.child(ProjectKeys.ROOMKEY).getValue().toString();
            String typeText = ds.child(ProjectKeys.TYPEKEY).getValue() == null ? "" : ds.child(ProjectKeys.TYPEKEY).getValue().toString();
            String title = ds.child("title").getValue() == null ? "" : ds.child("title").getValue().toString();
            String kitchen = ds.child("kitchen").getValue() == null ? "" : ds.child("kitchen").getValue().toString();
            String bathrooms = ds.child("bathroom").getValue() == null ? "" : ds.child("bathroom").getValue().toString();
            String advance = ds.child("advance").getValue() == null ? "" : ds.child("advance").getValue().toString();
            String rentType = ds.child("rentType").getValue() == null ? "" : ds.child("rentType").getValue().toString();
            String rent = ds.child("rent").getValue() == null ? "" : ds.child("rent").getValue().toString();
            String lat = ds.child("lat").getValue() == null ? "" : ds.child("lat").getValue().toString();
            String lan = ds.child("lan").getValue() == null ? "" : ds.child("lan").getValue().toString();

            model = new HomeAddListDataModel();
            model.setPost_id(ds.getKey());
            model.setArea(areaText);

            for (DataSnapshot d : ds.getChildren()) {
                String key = d.getKey();
                if (key.contains("image")) {
                    model.setImagelist(Uri.parse(d.getValue().toString()));
                }
            }

            model.setBedroom(roomsText);
            model.setType(typeText);
            model.setPost_id(ds.getKey());

        } catch (Exception e) {

        }

        return model;
    }
}
