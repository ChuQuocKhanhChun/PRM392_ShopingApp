package com.manager.glassshoping.model;

import java.util.Date;

public class SanPhamMoi {
    int id;
    String name;
    String description;
    int category_id;
    Double price;
    int stock;
    String image;
    Date CreateOn;

    public SanPhamMoi() {
    }

    public SanPhamMoi(int id, String name, String description, int category_id, Double price, int stock, String image, Date createOn) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category_id = category_id;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.CreateOn = createOn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreateOn() {
        return CreateOn;
    }

    public void setCreateOn(Date createOn) {
        CreateOn = createOn;
    }
}
