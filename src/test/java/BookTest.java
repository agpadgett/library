import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;

public class BookTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void all_emptyFirst() {
    assertEquals(Book.all().size(), 0);
  }

  @Test
  public void equals_returnsTrueIfTitlesAreSame(){
    Book firstBook = new Book ("Pride and Prejudice");
    Book secondBook = new Book ("Pride and Prejudice");
    assertEquals(true, firstBook.equals(secondBook));
  }

  @Test
  public void save_savesIntoDatabase(){
    Book myBook = new Book ("Pride and Prejudice");
    myBook.save();
    assertEquals(Book.all().get(0), myBook);
  }

  @Test
  public void getId_returnsCorrectId(){
    Book myBook = new Book ("Pride and Prejudice");
    myBook.save();
    //  myBook.getId();
    assertEquals(Book.all().get(0).getId(), myBook.getId());
  }

  @Test
  public void getTitle_returnsCorrectTitle(){
    Book myBook = new Book ("Pride and Prejudice");
    myBook.save();
    assertEquals("Pride and Prejudice", myBook.getTitle());
  }

  @Test
  public void find_findsBookInDatabase(){
    Book myBook = new Book ("Pride and Prejudice");
    myBook.save();
    Book savedBook = Book.find(myBook.getId());
    assertEquals(myBook, savedBook);
  }

  @Test
  public void addAuthor_addsAuthorToBook(){
    Book myBook = new Book ("Pride and Prejudice");
    myBook.save();
    Author myAuthor = new Author("Jane Austen");
    myAuthor.save();
    myBook.addAuthor(myAuthor);
    assertEquals(myAuthor, myBook.getAuthors().get(0));
  }

  @Test
  public void update_changesBookTitle(){
    Book myBook = new Book ("Pride and Prejudice");
    myBook.save();
    myBook.update("Pride and Prejudice AND ZOMBIES");
    assertEquals("Pride and Prejudice AND ZOMBIES", myBook.getTitle());
  }


  @Test
  public void delete_deletesBook(){
    Book myBook = new Book ("Pride and Prejudice");
    myBook.save();
    myBook.delete();
    assertEquals(0, Book.all().size());
  }

  @Test
  public void getCopiesOfBook(){
    Book myBook = new Book("book");
    myBook.save();
    int bookId = myBook.getId();
    Copy myCopy = new Copy(1, bookId);
    myCopy.save();
    assertEquals(myBook.getCopiesOfBook().get(0), myCopy);
  }

  @Test
  public void addCopyWorks(){
    Book myBook = new Book("book");
    myBook.save();
    myBook.addCopy();
    assertEquals(1, myBook.getCopiesOfBook().size());
  }
}
