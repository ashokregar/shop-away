package com.shopaway.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.swing.*;
import java.util.Date;

public class Address {
    private String id;
    private String street;
    private String city;
    private String state;
    private int pin;
    private Date createdOn;
    private String userId;

    public Address(){

    }

    public Address(String street, String city, String state, int pin, Date createdOn, String userId ){
        this.street = street;
        this.state = state;
        this.city = city;
        this.pin = pin;
        this.createdOn = createdOn;
        this.userId = userId;
    }

    @JsonProperty
    public String getId(){
        return id;
    }

    @JsonProperty
    public String getStreet(){
        return street;
    }

    @JsonProperty
    public String getState(){
        return state;
    }

    @JsonProperty
    public String getCity(){
        return city;
    }

    @JsonProperty
    public int getPin(){
        return pin;
    }

    @JsonProperty
    public Date getCreatedOn(){
        return createdOn;
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
}
