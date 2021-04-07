package com.example.saggu.myapplication;

public class ChannelObject {
    private int id;
    private String name;
    private float price;
    private String type;
    private String genre;
    private Boolean is_selectd;

    public ChannelObject() {
    }


    public ChannelObject(int id, String name, float price, String type){
        this.id =id;
        this.name =name;
        this.price =price;
        this.type =type;
    }


    public Boolean is_selected() {
        return is_selectd;
    }

    public void is_selected(Boolean is_selectd) {
        this.is_selectd = is_selectd;
    }

    public ChannelObject(int id, String name, float price, String type, Boolean is_selectd){
        this.id =id;
        this.name =name;
        this.price =price;
        this.type =type;
        this.is_selectd = is_selectd;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
