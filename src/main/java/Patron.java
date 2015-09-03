import java.util.List;
import org.sql2o.*;
import java.util.ArrayList;

public class Patron{
  private int id;
  private String name;

  public int getId(){
    return id;
  }

  public String getName(){
    return name;
  }

  public Patron(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object otherPatron){
    if(!(otherPatron instanceof Patron)){
      return false;
    } else {
      Patron newPatron = (Patron) otherPatron;
      return this.getName().equals(newPatron.getName()) &&
      this.getId() == newPatron.getId();
    }
  }

  public void delete(int id){
    try(Connection con = DB.sql2o.open()){
      String sql = "DELETE FROM patrons WHERE id =:id";
      con.createQuery(sql)
      .addParameter("id", id)
      .executeUpdate();
    }
  }

  public static List<Patron> all(){
    String sql = "SELECT * FROM patrons ORDER BY name ASC";
    try(Connection con = DB.sql2o.open()){
      return con.createQuery(sql)
      .executeAndFetch(Patron.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()){
      String sql = "INSERT INTO patrons (name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
      .addParameter("name", name)
      .executeUpdate()
      .getKey();
    }
  }

  public static Patron find (int id) {
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT * FROM patrons WHERE id =:id";
      Patron patron = con.createQuery(sql)
      .addParameter("id", id)
      .executeAndFetchFirst(Patron.class);
      return patron;
    }
  }

  public void addCopyToPatron(Copy copy){
    try(Connection con = DB.sql2o.open()){
      String sql = "INSERT INTO checkout (patron_id, copy_id) VALUES (:patron_id, :copy_id)";
      con.createQuery(sql)
      .addParameter("patron_id", this.getId())
      .addParameter("copy_id", copy.getId())
      .executeUpdate();
    }
  }

  public List<Copy> getCopiesForPatron(){
    try(Connection con = DB.sql2o.open()){
      String sql = "SELECT copies.* FROM" +
      " patrons"+
      " JOIN checkout ON (patrons.id = checkout.patron_id)" +
      " JOIN copies ON (checkout.copy_id = copies.id)" +
      " WHERE patrons.id =:id";
      List<Copy> copies = con.createQuery(sql)
      .addParameter("id", this.id)
      .executeAndFetch(Copy.class);
      return copies;
    }
  }


  public void delete(){
    try(Connection con = DB.sql2o.open()){
      String deleteQuery = "DELETE FROM patrons WHERE id =:id";
      con.createQuery(deleteQuery)
      .addParameter("id", id)
      .executeUpdate();

      String joinDeleteQuery = "DELETE FROM checkout WHERE patron_id = :patron_id";
      con.createQuery(joinDeleteQuery)
      .addParameter("patron_id", this.getId())
      .executeUpdate();
    }
  }

}
