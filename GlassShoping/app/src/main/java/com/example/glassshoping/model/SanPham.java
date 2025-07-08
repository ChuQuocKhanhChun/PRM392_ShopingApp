package com.example.glassshoping.model;

import java.io.Serializable;
import java.util.Date;

public class SanPham implements Serializable {
    int id;
    String name;
    String description;
    int category_id;
    Double price;
    int stock;
    String image;
    String CreateOn;

    public SanPham() {
    }

    public SanPham(int id, String name, String description, int category_id, Double price, int stock, String image, String createOn) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category_id = category_id;
        this.price = price;
        this.stock = stock;
        this.image = image;
        this.CreateOn = createOn;
    }

    public SanPham(int id, String name, String description, int categoryId, Double price, int stock, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category_id = categoryId;
        this.price = price;
        this.stock = stock;
        this.image = image;
    }

    public String getCreateOn() {
        return CreateOn;
    }

    public void setCreateOn(String createOn) {
        CreateOn = createOn;
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
}
