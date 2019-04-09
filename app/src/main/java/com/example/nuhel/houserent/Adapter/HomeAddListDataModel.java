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
    private String area;
    private String type;
    private String Post_id;
    private String title;
    private String desc;
    private String bedroom;
    private String kitchen;
    private String bathroom;
    private String lat;
    private String lan;
    private String housetype;
    private String rentType;
    private String rent;
    private String advance;
    private String velcony;
    private String phone;
    private String email;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public HomeAddListDataModel() {

        imagelist = new ArrayList<>();
    }

    protected HomeAddListDataModel(Parcel in) {
        imagelist = in.createTypedArrayList(Uri.CREATOR);
        area = in.readString();
        type = in.readString();
        Post_id = in.readString();
        title = in.readString();
        desc = in.readString();
        bedroom = in.readString();
        kitchen = in.readString();
        bathroom = in.readString();
        lat = in.readString();
        lan = in.readString();
        housetype = in.readString();
        rentType = in.readString();
        advance = in.readString();
        rent = in.readString();
        velcony = in.readString();

        phone = in.readString();
        email = in.readString();
    }

    public String getVelcony() {
        return velcony;
    }

    public void setVelcony(String velcony) {
        this.velcony = velcony;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getAdvance() {
        return advance;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getBedroom() {
        return bedroom;
    }

    public void setBedroom(String bedroom) {
        this.bedroom = bedroom;
    }

    public String getKitchen() {
        return kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    public String getBathroom() {
        return bathroom;
    }

    public void setBathroom(String bathroom) {
        this.bathroom = bathroom;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLan() {
        return lan;
    }

    public void setLan(String lan) {
        this.lan = lan;
    }

    public String getHousetype() {
        return housetype;
    }

    public void setHousetype(String housetype) {
        this.housetype = housetype;
    }

    public String getRentType() {
        return rentType;
    }

    public void setRentType(String rentType) {
        this.rentType = rentType;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(imagelist);
        dest.writeString(area);
        dest.writeString(type);
        dest.writeString(Post_id);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(bedroom);
        dest.writeString(kitchen);
        dest.writeString(bathroom);
        dest.writeString(lat);
        dest.writeString(lan);
        dest.writeString(housetype);
        dest.writeString(rentType);
        dest.writeString(advance);
        dest.writeString(rent);
        dest.writeString(velcony);
        dest.writeString(phone);
        dest.writeString(email);
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
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
