package com.data.entity;

public class Foodbank {
    private String id;
    private String storeName;
    private String foodDesc;
    private int foodQuantity;
    private String location;
    private String contactNum;
    private String representativeName;
    private boolean halal;
    private String imgPath;

    public Foodbank(String id, String storeName, String foodDesc, int foodQuantity, String location, String contactNum, String representativeName, boolean halal, String imgPath) {
        this.id = id;
        this.storeName = storeName;
        this.foodDesc = foodDesc;
        this.foodQuantity = foodQuantity;
        this.location = location;
        this.contactNum = contactNum;
        this.representativeName = representativeName;
        this.halal = halal;
        this.imgPath = imgPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getFoodDesc() {
        return foodDesc;
    }

    public void setFoodDesc(String foodDesc) {
        this.foodDesc = foodDesc;
    }

    public int getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(int foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getRepresentativeName() {
        return representativeName;
    }

    public void setRepresentativeName(String representativeName) {
        this.representativeName = representativeName;
    }

    public boolean isHalal() {
        return halal;
    }

    public void setHalal(boolean halal) {
        this.halal = halal;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
