package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api")
public class AddressBookRestController{

    private final AddressBookRepository addressBookRepository;
    private final BuddyInfoRepository buddyInfoRepository;

    public AddressBookRestController(AddressBookRepository addressBookRepository,
                                     BuddyInfoRepository buddyInfoRepository){
        this.addressBookRepository = addressBookRepository;
        this.buddyInfoRepository = buddyInfoRepository;
    }

    @PostMapping("/address-books")
    @ResponseStatus(HttpStatus.CREATED)
    public AddressBook createAddressBook(@RequestBody AddressBook addressBook){
        //save new book
        return addressBookRepository.save(addressBook);
    }

    @PostMapping("/buddies")
    @ResponseStatus(HttpStatus.CREATED)
    public BuddyInfo createBuddy(@RequestBody BuddyInfo buddy){
        return buddyInfoRepository.save(buddy);
    }

    @GetMapping("/address-books/{addressBookId}")
    public AddressBook getAddressBook(@PathVariable Long addressBookId){
        return findAddressBook(addressBookId);
    }

    @PutMapping("/address-books/{addressBookId}/buddies/{buddyId}")
    @Transactional
    public AddressBook addBuddy(@PathVariable Long addressBookId, @PathVariable Long buddyId){
        AddressBook addressBook = findAddressBook(addressBookId);
        BuddyInfo buddy = buddyInfoRepository.findById(buddyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buddy not found"));

        //put buddy in the book
        addressBook.addBuddy(buddy);
        return addressBookRepository.save(addressBook);
    }

    private AddressBook findAddressBook(Long addressBookId){
        return addressBookRepository.findById(addressBookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address book not found"));
    }
}
