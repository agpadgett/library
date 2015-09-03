import java.util.HashMap;
import java.util.List;
import java.util.Map;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;
import java.util.ArrayList;

public class App{
  public static void main (String[] args){
  //  staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String,Object>();
      model.put("template", "templates/index.vtl");

      model.put("books", Author.all());
      model.put("authors", Author.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/books", (request, response) -> {
      Map<String, Object> model = new HashMap<String,Object>();
      model.put("template", "templates/books.vtl");
      model.put("books", Book.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/books", (request, response) -> {
      Map<String, Object> model = new HashMap<String,Object>();
      String title = request.queryParams("title");
      Book newBook = new Book(title);
      newBook.save();
      model.put("books", Book.all());
      model.put("template", "templates/books.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/authors", (request, response) -> {
      Map<String, Object> model = new HashMap<String,Object>();
      model.put("template", "templates/authors.vtl");
      model.put("authors", Author.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/authors", (request, response) -> {
      Map<String, Object> model = new HashMap<String,Object>();
      String name = request.queryParams("name");
      Author newAuthor = new Author(name);
      newAuthor.save();
      model.put("authors", Author.all());
      model.put("template", "templates/authors.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/books/:id", (request, response) -> {
      Map<String,Object> model = new HashMap<String,Object>();
      int bookId = Integer.parseInt(request.params("id"));
      Book book = Book.find(bookId);
      ArrayList<Author> authorList = book.getAuthors();
      model.put("book", book);
      model.put("template","templates/book.vtl");
      return new ModelAndView(model,layout);
    }, new VelocityTemplateEngine());

    post("/books/:id/addAuthor", (request, response) -> {
      int bookId = Integer.parseInt(request.queryParams("book_id"));
      Book book = Book.find(bookId);
      String newAuthorName = request.queryParams("authorName");
      Author newAuthor = new Author(newAuthorName);
      newAuthor.save();
      newAuthor.addBook(book);
      String bookIdPath = String.format("/books/%d", bookId);
      response.redirect(bookIdPath);
      return null;
    });

    post("/books/:book_id/deleteAuthor/:author_id", (request, response) -> {
      int bookId = Integer.parseInt(request.params("book_id"));
      int deadAuthorId = Integer.parseInt(request.queryParams("deleteAuthor"));
      Author deadAuthor = Author.find(deadAuthorId);
      deadAuthor.delete();
      String deleteAuthPath = String.format("/books/%d", bookId);
      response.redirect(deleteAuthPath);
      return null;
    });


  }
}
