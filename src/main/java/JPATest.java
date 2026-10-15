import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.AddressBook;
import org.example.BuddyInfo;

public class JPATest{

    public static void main(String[] args){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("AdressBookPersistenceUnit");

        EntityManager em = emf.createEntityManager();

        BuddyInfo buddy1 = new BuddyInfo("Samy", "Ottawa", 613606195);
        BuddyInfo buddy2 = new BuddyInfo("Tala", "Ottawa", 819744825);

        AddressBook book = new AddressBook();
        book.addBuddy(buddy1);
        book.addBuddy(buddy2);

        em.getTransaction().begin();
        em.persist(buddy1);
        em.persist(buddy2);

        em.persist(book);

        em.getTransaction().commit();

        em.close();
        emf.close();
    }
}
