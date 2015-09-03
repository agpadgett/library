import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class PatronTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(Patron.all().size(),0);
  }

  @Test
  public void equals_returnTrueWhenSameName() {
    Patron firstName = new Patron("Ethen");
    Patron secondName = new Patron("Ethen");
    assertEquals(true,firstName.equals(secondName));
  }

  @Test
  public void getName_returnsTheName() {
    Patron myPatron = new Patron("Ethen");
    myPatron.save();
    assertEquals("Ethen",myPatron.getName());
  }

  @Test
  public void getId_returnsCorrectId() {
    Patron myPatron = new Patron("Ethen");
    myPatron.save();
    assertEquals(myPatron.getId(), Patron.all().get(0).getId());
  }

  @Test
  public void save_insertsPatronIntoDatabase() {
    Patron myPatron = new Patron("Ethen");
    myPatron.save();
    assertEquals(myPatron,Patron.all().get(0));
  }

  @Test
  public void find_returnsCorrectPatron() {
    Patron myPatron = new Patron("Ethen");
    myPatron.save();
    int myId = myPatron.getId();
    assertEquals(myPatron, Patron.find(myId));
  }

  @Test
  public void delete_deletesPatronFromDatabase(){
    Patron myPatron = new Patron("Bob");
    myPatron.save();
    myPatron.delete();
    assertEquals(0, Patron.all().size());
  }

  @Test
  public void addCopyToPatron_addsCopyToPatron(){
    Patron myPatron = new Patron("Sally");
    myPatron.save();
    Copy myCopy = new Copy (1, 2);
    myCopy.save();
    myPatron.addCopyToPatron(myCopy);
    assertEquals(myCopy, myPatron.getCopiesForPatron().get(0));
  }


}
