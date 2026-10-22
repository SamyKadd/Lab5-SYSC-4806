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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
                        "Address Book #" + addressBook.getId())))
                .andExpect(content().string(org.hamcrest.Matchers.containsString(
                        "Create new address book")));
    }

    @Test
    void createsAddressBookFromPage() throws Exception{
        mockMvc.perform(post("/address-books"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        assertEquals(1, addressBookRepository.count());
    }

    @Test
    void addsBuddyFromPage() throws Exception{
        AddressBook addressBook = addressBookRepository.save(new AddressBook());

        mockMvc.perform(post("/address-books/{addressBookId}/buddies", addressBook.getId())
                        .param("name", "Bob")
                        .param("address", "Toronto")
                        .param("phone", "416555456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/address-books/" + addressBook.getId()));

        AddressBook savedBook = addressBookRepository.findById(addressBook.getId()).orElseThrow();
        assertEquals(1, savedBook.getBuddies().size());
        assertEquals("Bob", savedBook.getBuddies().getFirst().getName());
        assertEquals(1, buddyInfoRepository.count());
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
