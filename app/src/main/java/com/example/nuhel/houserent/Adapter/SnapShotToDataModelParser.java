package com.example.nuhel.houserent.Adapter;

import com.example.nuhel.houserent.Controller.ProjectKeys;
import com.google.firebase.database.DataSnapshot;

/**
 * Created by Nuhel on 11/13/2017.
 */

public class SnapShotToDataModelParser {

    public HomeAddListDataModel getModel(DataSnapshot ds) {

        HomeAddListDataModel model = null;
        try {
            String areaText = ds.child(ProjectKeys.AREARKEY).getValue() == null ? "" : ds.child(ProjectKeys.AREARKEY).getValue().toString();
            String roomsText = ds.child(ProjectKeys.ROOMKEY).getValue() == null ? "" : ds.child(ProjectKeys.ROOMKEY).getValue().toString();
            String typeText = ds.child(ProjectKeys.TYPEKEY).getValue() == null ? "" : ds.child(ProjectKeys.TYPEKEY).getValue().toString();
            String img1 = ds.child("image1").getValue() == null ? "" : ds.child("image1").getValue().toString();
            String img2 = ds.child("image2").getValue() == null ? "" : ds.child("image2").getValue().toString();
            String img3 = ds.child("image3").getValue() == null ? "" : ds.child("image3").getValue().toString();
            model = new HomeAddListDataModel();
            model.setPost_id(ds.getKey());
            model.setArea(areaText);
            model.setImage1(img1);
            model.setImage2(img2);
            model.setImage3(img3);
            model.setRoom(roomsText);
            model.setType(typeText);

        } catch (Exception e) {

        }

        return model;
    }
}
