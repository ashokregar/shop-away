package com.shopaway.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public  class User {
    private String id;
    private String name;
    private String username;
    private String password;
    private String mobile;
    private String email;
    private Date createdOn;
    private Date lastLogin;

    public User() {

    }


    /**
     * Atomically sets the value to the given updated value if the current value == the expected value.
     *  @param name name
     *  @param username username
     *  @param password password
     *  @param mobile mobile number
     *  @param email email id
     *  @param createdOn created date
     *  @param lastLogin last login date
    **/
    public User( String name, String username, String password, String mobile, String email, Date createdOn, Date lastLogin) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.mobile = mobile;
        this.email = email;
        this.createdOn = createdOn;
        this.lastLogin = lastLogin;
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

    @JsonProperty
    public void setId(String id){
        this.id = id;
    }

    @JsonProperty
    public void setCreatedOn(Date createdOn){
        this.createdOn = createdOn;
    }
    @JsonProperty
    public void setLastLogin(Date lastLogin){
        this.lastLogin = lastLogin;
    }

    @JsonProperty
    public void setEmail(String email){
        this.email = email;
    }

}
