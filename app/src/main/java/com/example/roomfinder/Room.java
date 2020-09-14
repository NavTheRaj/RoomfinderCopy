package com.example.roomfinder;

import android.graphics.Bitmap;

public class Room {
    private int _id;
    private String ownerName;
    private String ownerNumber;
    private String location;
    private String type; //Room, Flat, Apartment, Hostel, House
    private String  parking; //Bike Cycle Car No Paarking
    private String  preference; //Family, Individual, Office, Any
    private String internet; //Yes no
    private Bitmap image;
    private String postDate;
    private String kitchen;  //Yes No
    private String room; //No of rooms


    public Room(){}

    public Room(int _id, String ownerName, String ownerNumber, String location, String type, String parking, String preference, String internet, Bitmap image, String postDate, String kitchen, String room) {
        this._id = _id;
        this.ownerName = ownerName;
        this.ownerNumber = ownerNumber;
        this.location = location;
        this.type = type;
        this.parking = parking;
        this.preference = preference;
        this.internet = internet;
        this.image = image;
        this.postDate = postDate;
        this.kitchen = kitchen;
        this.room = room;
    }


    public Room(String ownerName, String ownerNumber, String location, String type, String parking, String preference, String internet, Bitmap image, String postDate, String kitchen, String room) {
        this.ownerName = ownerName;
        this.ownerNumber = ownerNumber;
        this.location = location;
        this.type = type;
        this.parking = parking;
        this.preference = preference;
        this.internet = internet;
        this.image = image;
        this.postDate = postDate;
        this.kitchen = kitchen;
        this.room = room;
    }



    public static final Room[] rooms = {
    };


    //Getters and Setters
    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerNumber() {
        return ownerNumber;
    }

    public void setOwnerNumber(String ownerNumber) {
        this.ownerNumber = ownerNumber;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getKitchen() {
        return kitchen;
    }

    public void setKitchen(String kitchen) {
        this.kitchen = kitchen;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String toString(){
        return this.type+" | "+this.location;
    }
}
