package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressBookTest{

    @Test
    void testAddBuddy(){
        AddressBook addressBook = new AddressBook();
        BuddyInfo buddy = new BuddyInfo("Alice", 123456789);

        addressBook.addBuddy(buddy);

        assertEquals(1, addressBook.getBuddies().size());
        assertTrue(addressBook.getBuddies().contains(buddy));
    }

    @Test
    void testRemoveBuddy(){
        AddressBook addressBook = new AddressBook();
        BuddyInfo buddy = new BuddyInfo("Alice", 123456789);

        addressBook.addBuddy(buddy);
        addressBook.removeBuddy(buddy);

        assertEquals(0, addressBook.getBuddies().size());
        assertFalse(addressBook.getBuddies().contains(buddy));
    }
}
