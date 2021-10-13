package com.shopaway.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public  class User {
    private long id;
    private String name;
    private String username;
    private String password;
    private String mobile;
    private String email;
    private Date createdOn;
    private Date lastLogin;

    public User() {

    }

    public User(long id, String name, String username, String password, String mobile, String email, Date createdOn, Date lastLogin) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
        this.createdOn = createdOn;
        this.lastLogin = lastLogin;
    }

    @JsonProperty
    public long getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }

    @JsonProperty
    public String getPassword() {
        return password;
    }
    @JsonProperty
    public String getMobile() {
        return mobile;
    }
    @JsonProperty
    public String getEmail() {
        return email;
    }

    @JsonProperty
    public Date getCreatedOn() {
        return createdOn;
    }
    @JsonProperty
    public Date getLastLogin() {
        return lastLogin;
    }

}
