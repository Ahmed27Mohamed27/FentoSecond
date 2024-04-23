package com.money.deliveryapp.Admin;

public class Fetch {
    private String name, email, password, mobile, id, image, uid;
    public Fetch()
    {

    }

    public Fetch(String name, String email, String mobile, String password, String uid, String id, String image, String imageId1,  String imageId2) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image;
        this.mobile = mobile;
        this.id = id;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
