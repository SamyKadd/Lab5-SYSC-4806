package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuddyInfoTest{

    @Test
    void testGetName(){
        BuddyInfo buddy = new BuddyInfo("Alice", 123456789);

        assertEquals("Alice", buddy.getName());
    }

    @Test
    void testGetPhone(){
        BuddyInfo buddy = new BuddyInfo("Alice", 123456789);

        assertEquals(123456789, buddy.getPhone());
    }

    @Test
    void testGetAddress(){
        BuddyInfo buddy = new BuddyInfo("Alice", "Ottawa", 123456789);

        assertEquals("Ottawa", buddy.getAddress());
    }
}
