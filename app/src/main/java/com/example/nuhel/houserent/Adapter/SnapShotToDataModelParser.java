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

            model = new HomeAddListDataModel();
            model.setPost_id(ds.getKey());
            model.setArea(areaText);


            for (DataSnapshot d : ds.getChildren()) {
                String key = d.getKey();
                if (key.contains("image")) {
                    model.setImagelist(Uri.parse(d.getValue().toString()));
                }
            }

            model.setRoom(roomsText);
            model.setType(typeText);

        } catch (Exception e) {

        }

        return model;
    }
}
