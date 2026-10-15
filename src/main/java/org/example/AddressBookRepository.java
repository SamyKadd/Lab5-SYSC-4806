package org.example;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "addressBooks", path = "addressBooks")
public interface AddressBookRepository extends CrudRepository<AddressBook,Long>{
}
