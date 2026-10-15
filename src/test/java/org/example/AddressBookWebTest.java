package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AddressBookWebTest{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private BuddyInfoRepository buddyInfoRepository;

    @BeforeEach
    void clearDatabase(){
        addressBookRepository.deleteAll();
        buddyInfoRepository.deleteAll();
    }

    @Test
    void createsBuddyFromJson() throws Exception{
        mockMvc.perform(post("/api/buddies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Alice\",\"address\":\"Ottawa\",\"phone\":613555123}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.address").value("Ottawa"));
    }

    @Test
    void showsHomePage() throws Exception{
        AddressBook addressBook = addressBookRepository.save(new AddressBook());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(org.hamcrest.Matchers.containsString(
                        "Address Book #" + addressBook.getId())));
    }

    @Test
    void addsBuddyAndShowsThymeleafPage() throws Exception{
        AddressBook addressBook = addressBookRepository.save(new AddressBook());
        BuddyInfo buddy = buddyInfoRepository.save(new BuddyInfo("Alice", "Ottawa", 613555123));

        mockMvc.perform(put("/api/address-books/{addressBookId}/buddies/{buddyId}",
                        addressBook.getId(), buddy.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buddies[0].name").value("Alice"));

        mockMvc.perform(get("/address-books/{addressBookId}", addressBook.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Alice")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Ottawa")));
    }

    @Test
    void returnsNotFoundForMissingAddressBook() throws Exception{
        mockMvc.perform(get("/api/address-books/{addressBookId}", 999999))
                .andExpect(status().isNotFound());
    }
}
