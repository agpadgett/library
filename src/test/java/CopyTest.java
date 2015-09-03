import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class CopyTest{

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyAtFirst() {
    assertEquals(0,Copy.all().size());
  }

  @Test
  public void returns_trueIfValuesAreSame() {
    Copy firstCopy = new Copy(3,3);
    Copy secondCopy = new Copy(3,3);
    assertEquals(true,firstCopy.equals(secondCopy));
  }

  @Test
  public void getId_returnsCorrectId() {
    Copy myCopy = new Copy(3,3);
    myCopy.save();
    assertEquals(myCopy.getId(), Copy.all().get(0).getId());
  }

  @Test
  public void getCopyNumber_returnsCorrectCopyNumber() {
    Copy myCopy = new Copy(4,4);
    myCopy.save();
    assertEquals(4,myCopy.getCopyNumber());
  }

  @Test
  public void getBookId_returnsCorrectBookId(){
    Copy myCopy = new Copy (1,3);
    myCopy.save();
    assertEquals(3, myCopy.getBookId());
  }

  @Test
  public void save_savesTheCopyInDataBase() {
    Copy myCopy = new Copy(4,4);
    myCopy.save();
    assertEquals(myCopy,Copy.all().get(0));
  }

  @Test
  public void find_findsCorrectCopy() {
    Copy myCopy = new Copy(4,4);
    myCopy.save();
    int myCopyId = myCopy.getId();
    assertEquals(myCopy,Copy.find(myCopyId));
  }

  @Test
  public void delete_removesFromTheDatabase() {
    Copy myCopy = new Copy(5,5);
    myCopy.save();
    myCopy.delete();
    assertEquals(Copy.all().size(),0);
  }

  @Test
  public void checkout_checkoutWorks(){
    Book myBook = new Book("a book");
    myBook.save();
    int bookId = myBook.getId();
    Copy myCopy = new Copy(1,bookId);
    myCopy.save();
    Patron myPatron = new Patron("Stan");
    myPatron.save();
    myCopy.checkout("Dec 1", myPatron);
    assertEquals("Dec 1", myCopy.getDueDate());
  }
}
