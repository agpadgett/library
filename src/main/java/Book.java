import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Book {
  private int id;
  private String title;

  public Book(String title) {
      this.title = title;
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public static List<Book> all() {
    String sql = "SELECT * FROM Books";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Book.class);
    }
  }

  @Override
   public boolean equals(Object otherBook){
     if (!(otherBook instanceof Book)) {
       return false;
     } else {
       Book newBook = (Book) otherBook;
       return this.getTitle().equals(newBook.getTitle()) &&
              this.getId() == newBook.getId();
     }
   }

   public void save() {
     try(Connection con =DB.sql2o.open()) {
       String sql = "INSERT INTO books (title) VALUES (:title)";
       this.id = (int) con.createQuery(sql, true)
       .addParameter("title", this.title)
       .executeUpdate()
       .getKey();
     }
   }

   public static Book find(int id) {
     try(Connection con = DB.sql2o.open()){
       String sql = "SELECT * FROM books WHERE id =:id";
       Book book = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Book.class);
        return book;
     }
   }

   public void addAuthor(Author author){
     try(Connection con = DB.sql2o.open()) {
       String sql = "INSERT INTO books_authors (book_id, author_id) VALUES (:book_id, :author_id)";
       con.createQuery(sql)
       .addParameter("book_id", this.getId())
       .addParameter("author_id", author.getId())
       .executeUpdate();
     }
   }

   public ArrayList<Author> getAuthors(){
     try(Connection con = DB.sql2o.open()){
       String sql = "SELECT author_id FROM books_authors WHERE book_id = :book_id";
       List<Integer> authorIds = con.createQuery(sql)
       .addParameter("book_id", this.getId())
       .executeAndFetch(Integer.class);

       ArrayList<Author> authors = new ArrayList<Author>();

       for(Integer authorId : authorIds){
         String authorQuery = "SELECT * FROM authors WHERE id = :authorId";
         Author author = con.createQuery(authorQuery)
         .addParameter("authorId", authorId)
         .executeAndFetchFirst(Author.class);
         authors.add(author);
       }

       return authors;
     }
   }

   public void update(String newTitle){
     this.title = newTitle;
     //Don't forget to do this, otherwise testing titles is a pain

     try(Connection con = DB.sql2o.open()){
       String sql = "UPDATE books SET title = :newTitle WHERE id = :id";
       con.createQuery(sql)
       .addParameter("newTitle", newTitle)
       .addParameter("id", id)
       .executeUpdate();
     }
   }

   public void delete() {
     try(Connection con = DB.sql2o.open()){
       String deleteQuery = "DELETE FROM books WHERE id = :id;";
       con.createQuery(deleteQuery)
       .addParameter("id", id)
       .executeUpdate();

       String joinDeleteQuery = "DELETE FROM books_authors WHERE book_id = :book_id";
       con.createQuery(joinDeleteQuery)
       .addParameter("book_id", this.getId())
       .executeUpdate();
     }
   }

  //  public List<Copy> getCopiesOfBook(){
  //    try(Connection con = DB.sql2o.open()){
  //      String sql = "SELECT copies.* FROM" +
  //           " books"+
  //           " JOIN copies ON (books.id = copies.book_id)"+
  //           " WHERE copies.book_id =:id";
  //      List<Copy> copies = con.createQuery(sql)
  //             .addParameter("id", this.getId())
  //             .executeAndFetch(Copy.class);
  //             return copies;
  //    }
  //  }

  //  public int getNumberOfCopieOfBook(){
  //    try(Connection con = DB.sql2o.open()){
  //      String sql = "SELECT * FROM copies WHERE book_id=:book_id";
  //      List<Copy> copies = con.createQuery(sql)
  //             .addParameter("book_id", this.getId())
  //             .executeAndFetch(Copy.class);
  //       int copyNum = copies.size();
  //       return copyNum;
  //    }
  //  }


}
