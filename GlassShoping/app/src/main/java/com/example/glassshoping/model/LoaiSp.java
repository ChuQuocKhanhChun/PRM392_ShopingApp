package com.example.glassshoping.model;

public class LoaiSp {
    int id;
    String name;
    String description;
    String image;

    public LoaiSp() {
    }

    public LoaiSp(int id, String name, String description, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image= image;
    }
    public LoaiSp(String name, String image){
        this.name = name;
        this.image= image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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
}
