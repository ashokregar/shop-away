package com.shopaway.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

public  class Order {
    private String id;
    private Product[] products;
    private Address address;
    private Date createdOn;
    private STATUS status;
    private String userId;

    public Order() {

    }

    public Order( Product[] products, Address address, Date createdOn, STATUS status, String userId) {
        this.products = products;
        this.address = address;
        this.createdOn = createdOn;
        this.status = status;
        this.userId = userId;
    }


    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public Date getCreatedOn() {
        return createdOn;
    }


    @JsonProperty
    public Product[] getProducts(){
        return products;
    }

    @JsonProperty
    public Address getAddress() {
        return address;
    }

    @JsonProperty
    public STATUS getStatus(){
        return status;
    }

    @JsonProperty
    public String getUserId(){
        return userId;
    }

    @JsonProperty
    public void setId(String id){
        this.id = id;
    }

    @JsonProperty
    public void setCreatedOn(Date createdOn){
        this.createdOn = createdOn;
    }
    @JsonProperty
    public void setProducts(Product[] products){
        this.products = products;
    }
}


