package com.manager.helo.model;

public class Cart {
    int prdId;
    String prdName;
    long prdPrice;
    String prdImage;
    int prdQtt;

    public Cart(){

    }

    public int getPrdId() {
        return prdId;
    }

    public void setPrdId(int prdId) {
        this.prdId = prdId;
    }

    public String getPrdName() {
        return prdName;
    }

    public void setPrdName(String prdName) {
        this.prdName = prdName;
    }

    public long getPrdPrice() {
        return prdPrice;
    }

    public void setPrdPrice(long prdPrice) {
        this.prdPrice = prdPrice;
    }

    public String getPrdImage() {
        return prdImage;
    }

    public void setPrdImage(String prdImage) {
        this.prdImage = prdImage;
    }

    public int getPrdQtt() {
        return prdQtt;
    }

    public void setPrdQtt(int prdQtt) {
        this.prdQtt = prdQtt;
    }
}
