package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddressBookIntegrationTest{

    @LocalServerPort
    private int port;

    @Autowired
    private AddressBookRepository addressBookRepository;

    @Autowired
    private BuddyInfoRepository buddyInfoRepository;

    private RestClient restClient;

    @BeforeEach
    void setUp(){
        addressBookRepository.deleteAll();
        buddyInfoRepository.deleteAll();

        //use the real running server
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void createsBuddyOverHttp(){
        ResponseEntity<BuddyInfo> response = restClient.post()
                .uri("/api/buddies")
                .body(new BuddyInfo("Alice", "Ottawa", 613555123))
                .retrieve()
                .toEntity(BuddyInfo.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Ottawa", response.getBody().getAddress());
    }

    @Test
    void addsBuddyToAddressBookOverHttp(){
        AddressBook addressBook = createAddressBook();
        BuddyInfo buddy = createBuddy();

        AddressBook updatedBook = restClient.put()
                .uri("/api/address-books/{addressBookId}/buddies/{buddyId}",
                        addressBook.getId(), buddy.getId())
                .retrieve()
                .body(AddressBook.class);

        assertNotNull(updatedBook);
        assertEquals(1, updatedBook.getBuddies().size());
        assertEquals("Alice", updatedBook.getBuddies().getFirst().getName());

        AddressBook savedBook = restClient.get()
                .uri("/api/address-books/{addressBookId}", addressBook.getId())
                .retrieve()
                .body(AddressBook.class);

        assertNotNull(savedBook);
        assertEquals("Ottawa", savedBook.getBuddies().getFirst().getAddress());
    }

    @Test
    void rendersAddressBookPageOverHttp(){
        AddressBook addressBook = createAddressBook();
        BuddyInfo buddy = createBuddy();

        restClient.put()
                .uri("/api/address-books/{addressBookId}/buddies/{buddyId}",
                        addressBook.getId(), buddy.getId())
                .retrieve()
                .toBodilessEntity();

        String page = restClient.get()
                .uri("/address-books/{addressBookId}", addressBook.getId())
                .retrieve()
                .body(String.class);

        assertNotNull(page);
        assertTrue(page.contains("Alice"));
        assertTrue(page.contains("Ottawa"));
    }

    private AddressBook createAddressBook(){
        return restClient.post()
                .uri("/api/address-books")
                .body(new AddressBook())
                .retrieve()
                .body(AddressBook.class);
    }

    private BuddyInfo createBuddy(){
        return restClient.post()
                .uri("/api/buddies")
                .body(new BuddyInfo("Alice", "Ottawa", 613555123))
                .retrieve()
                .body(BuddyInfo.class);
    }
}
