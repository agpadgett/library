import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Copy {
  private int id;
  private int copy_number;
  private int book_id;

  public int getId(){
    return id;
  }

  public int getCopyNumber(){
    return copy_number;
  }

  public int getBookId(){
    return book_id;
  }

  public Copy(int copy_number, int book_id){
    this.copy_number = copy_number;
    this.book_id = book_id;
  }

  public static List<Copy> all() {
    String sql = "SELECT * FROM copies";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql)
      .executeAndFetch(Copy.class);
    }
  }

  @Override
  public boolean equals(Object otherCopy){
    if(!(otherCopy instanceof Copy)){
      return false;
    } else {
      Copy newCopy = (Copy) otherCopy;
      return this.getId() == newCopy.getId()&&
             this.getCopyNumber() == newCopy.getCopyNumber()&&
             this.getBookId() == newCopy.getBookId();
    }
  }

  public void delete(){
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM copies WHERE id=:id";
      con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public void save(){
    try(Connection con = DB.sql2o.open()){
      String sql = "INSERT INTO copies (copy_number, book_id) VALUES (:copy_number, :book_id)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("copy_number", copy_number)
      .addParameter("book_id", book_id)
      .executeUpdate()
      .getKey();
    }
  }

  public static Copy find(int id) {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM copies WHERE id=:id";
      Copy copy = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Copy.class);
      return copy;
    }
  }


}
