package org.example;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class AddressBook{

    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(fetch = FetchType.EAGER)
    private List<BuddyInfo> buddies;

    public AddressBook(){
        this.buddies = new ArrayList<>();
    }

    public Long getId(){
        return id;
    }

    public List<BuddyInfo> getBuddies(){
        return buddies;
    }

    public void setBuddies(List<BuddyInfo> buddies){
        this.buddies = buddies;
    }

    public void addBuddy(BuddyInfo buddy){
        if(buddy != null){
            buddies.add(buddy);
        }
    }

    public void removeBuddy(BuddyInfo buddy){
        if(buddies.remove(buddy)){
            System.out.println(buddy.getName() + " removed from the address book.");
        }else{
            System.out.println("Buddy not found in the address book.");
        }
    }

    public void printBuddies(){
        if(buddies.isEmpty()){
            System.out.println("Address book is empty.");
        }else{
            for(BuddyInfo buddy : buddies){
                System.out.println(buddy.getName() + " - " + buddy.getPhone());
            }
        }
    }
}
