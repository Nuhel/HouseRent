package com.example.nuhel.houserent.Adapter;

import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Nuhel on 8/30/2017.
 */

public class HomeAddListDataModel {

    private ArrayList<Uri> imagelist;
    private String Area;
    private String Room;
    private String Type;
    private String Post_id;


    public HomeAddListDataModel() {

        imagelist = new ArrayList<>();
    }

    public String getPost_id() {
        return Post_id;
    }

    public void setPost_id(String post_id) {
        Post_id = post_id;
    }

    public ArrayList<Uri> getImagelist() {
        return imagelist;
    }

    public void setImagelist(Uri uri) {
        this.imagelist.add(uri);
    }

    public String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }
}
