package com.shopaway.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public  class Image {
    private String id;
    private String name;
    private Date createdOn;

    public Image() {

    }

    public Image(String id, String name, Date createdOn) {
        this.id = id;
        this.name = name;
        this.createdOn = createdOn;
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
    public Date getCreatedOn() {
        return createdOn;
    }

}
