package com.manager.helo.model;

public class Item {
    int id_prd;
    int quantity;
    String product_image;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    String product_name;

    public int getId_prd() {
        return id_prd;
    }

    public void setId_prd(int id_prd) {
        this.id_prd = id_prd;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }
}
