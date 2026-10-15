package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class BuddyInfo{

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Integer phone;
    private String address;

    public BuddyInfo(){
    }

    public BuddyInfo(String name, Integer phone){
        this(name, "", phone);
    }

    public BuddyInfo(String name, String address, Integer phone){
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Long getId(){
        return id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setPhone(Integer phone){
        this.phone = phone;
    }

    public Integer getPhone(){
        return phone;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return address;
    }
}
