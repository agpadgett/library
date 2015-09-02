import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class AuthorTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyFirst() {
    assertEquals(Author.all().size(), 0);
  }

  @Test
  public void returns_TrueIfNamesAreSame() {
    Author firstAuthor = new Author ("Homer");
    Author secondAuthor = new Author ("Homer");
    assertEquals(true,firstAuthor.equals(secondAuthor));
  }

  @Test
  public void save_InsertsInTheDatabase(){
    Author myAuthor =  new Author ("Homer");
    myAuthor.save();
    assertEquals(myAuthor,Author.all().get(0));
  }

  @Test
  public void getId_Returns_CorrestID() {
    Author myAuthor = new Author ("Homes");
    myAuthor.save();
    assertEquals(Author.all().get(0).getId(),myAuthor.getId());
  }

  @Test
  public void addBooks_AuthorToJointTable() {
    Author myAuthor = new Author ("Homes");
    myAuthor.save();
    Book myBook = new Book ("Book of stuffs");
    myBook.save();
    myAuthor.addBook(myBook);
    Book savedBook = myAuthor.getBooks().get(0);
    assertEquals(savedBook, myBook);
  }

  @Test
  public void getName_Returns_CorrestName() {
    Author myAuthor = new Author ("Homes");
    myAuthor.save();
    assertEquals("Homes",myAuthor.getName());
  }

  @Test
  public void update_ChangesNameInDatabase() {
    Author myAuthor = new Author ("JK Rowling");
    myAuthor.save();
    myAuthor.update("Donald Trump");
    assertEquals("Donald Trump",myAuthor.getName());
  }

  @Test
  public void delete_AuthorsFromDAtabase() {
    Author myAuthor = new Author ("JK Rowling");
    myAuthor.save();
    myAuthor.delete();
    assertEquals(Author.all().size(),0);
  }

  @Test
  public void find_returnsCorrectAuthor(){
    Author myAuthor = new Author ("JK Rowling");
    myAuthor.save();
    int myId = myAuthor.getId();
    assertEquals(myAuthor, Author.find(myId));
  }
}
