package com.shopaway.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public  class Product {
    private String id;
    private String name;
    private String description;
    private String seller;
    private int price;
    private String rating;
    private String category;
    private Date createdOn;
    private String imageIds;
    private int qty;


    public Product() {

    }

    public Product(String id, String name, String description, String seller, int price, String rating, String category, Date createdOn, String imageIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.seller = seller;
        this.price = price;
        this.rating = rating;
        this.category = category;
        this.createdOn = createdOn;
        this.imageIds = imageIds;
    }

    public void setImageIds(String imageIds) {
        this.imageIds = imageIds;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getCategory() {
        return category;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }
    @JsonProperty
    public String getSeller() {
        return seller;
    }
    @JsonProperty
    public String getRating() {
        return rating;
    }

    @JsonProperty
    public Date getCreatedOn() {
        return createdOn;
    }
    @JsonProperty
    public int getPrice() {
        return price;
    }

    @JsonProperty
    public String getImageIds() {
        return imageIds;
    }

    @JsonProperty
    public int getQty(){
        return qty;
    }

    @JsonProperty
    public void setQty(int qty){
        this.qty = qty;
    }

}
