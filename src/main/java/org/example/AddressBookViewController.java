package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

import java.util.stream.StreamSupport;

@Controller
public class AddressBookViewController{

    private final AddressBookRepository addressBookRepository;

    public AddressBookViewController(AddressBookRepository addressBookRepository){
        this.addressBookRepository = addressBookRepository;
    }

    @GetMapping("/")
    public String showHome(Model model){
        //show all the saved books
        model.addAttribute("addressBooks", StreamSupport.stream(
                addressBookRepository.findAll().spliterator(), false).toList());
        return "index";
    }

    @GetMapping("/address-books/{addressBookId}")
    public String showAddressBook(@PathVariable Long addressBookId, Model model){
        AddressBook addressBook = addressBookRepository.findById(addressBookId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address book not found"));

        //give the book to the page
        model.addAttribute("addressBook", addressBook);
        return "address-book";
    }
}
