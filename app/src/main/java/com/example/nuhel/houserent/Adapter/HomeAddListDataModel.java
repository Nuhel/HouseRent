package com.example.nuhel.houserent.Adapter;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Nuhel on 8/30/2017.
 */

public class HomeAddListDataModel implements Parcelable {

    public static final Creator<HomeAddListDataModel> CREATOR = new Creator<HomeAddListDataModel>() {
        @Override
        public HomeAddListDataModel createFromParcel(Parcel in) {
            return new HomeAddListDataModel(in);
        }

        @Override
        public HomeAddListDataModel[] newArray(int size) {
            return new HomeAddListDataModel[size];
        }
    };
    private ArrayList<Uri> imagelist;
    private String Area;
    private String Room;
    private String Type;
    private String Post_id;

    public HomeAddListDataModel() {

        imagelist = new ArrayList<>();
    }

    protected HomeAddListDataModel(Parcel in) {
        imagelist = in.createTypedArrayList(Uri.CREATOR);
        Area = in.readString();
        Room = in.readString();
        Type = in.readString();
        Post_id = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(imagelist);
        dest.writeString(Area);
        dest.writeString(Room);
        dest.writeString(Type);
        dest.writeString(Post_id);
    }

    @Override
    public int describeContents() {
        return 0;
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
