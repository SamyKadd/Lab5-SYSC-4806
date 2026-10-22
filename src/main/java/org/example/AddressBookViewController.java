package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.StreamSupport;

@Controller
public class AddressBookViewController{

    private final AddressBookRepository addressBookRepository;
    private final BuddyInfoRepository buddyInfoRepository;

    public AddressBookViewController(AddressBookRepository addressBookRepository,
                                     BuddyInfoRepository buddyInfoRepository){
        this.addressBookRepository = addressBookRepository;
        this.buddyInfoRepository = buddyInfoRepository;
    }

    @GetMapping("/")
    public String showHome(Model model){
        //show all the saved books
        model.addAttribute("addressBooks", StreamSupport.stream(
                addressBookRepository.findAll().spliterator(), false).toList());
        return "index";
    }

    @PostMapping("/address-books")
    public String createAddressBook(){
        //make a new empty book
        addressBookRepository.save(new AddressBook());
        return "redirect:/";
    }

    @GetMapping("/address-books/{addressBookId}")
    public String showAddressBook(@PathVariable Long addressBookId, Model model){
        AddressBook addressBook = findAddressBook(addressBookId);

        //give the book to the page
        model.addAttribute("addressBook", addressBook);
        model.addAttribute("newBuddy", new BuddyInfo());
        return "address-book";
    }

    @PostMapping("/address-books/{addressBookId}/buddies")
    public String addBuddy(@PathVariable Long addressBookId,
                           @ModelAttribute("newBuddy") BuddyInfo buddy){
        AddressBook addressBook = findAddressBook(addressBookId);

        //save both parts
        BuddyInfo savedBuddy = buddyInfoRepository.save(buddy);
        addressBook.addBuddy(savedBuddy);
        addressBookRepository.save(addressBook);
        return "redirect:/address-books/" + addressBookId;
    }

    private AddressBook findAddressBook(Long addressBookId){
        return addressBookRepository.findById(addressBookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address book not found"));
    }
}
