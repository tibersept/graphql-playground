package isd.graphql.playground;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.google.common.collect.ImmutableMap;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Component
public class GraphQLDataFetchers {

  private enum AuthorType {
    GREAT, MEH
  }

  private static List<Map<String, String>> books;
  private static List<Map<String, String>> authors;

  public static void initData() {
    initBooks();
    initAuthors();
  }

  private static void initBooks() {
    books = new ArrayList<>();

    Map<String, String> book1 = new HashMap<>();
    book1.put("id", "book-1");
    book1.put("name", "Harry Potter and the Philosopher's Stone");
    book1.put("pageCount", "223");
    book1.put("authorId", "author-1");
    book1.put("publicationDate", "19970626000000");

    Map<String, String> book2 = new HashMap<>();
    book2.put("id", "book-2");
    book2.put("name", "Moby Dick");
    book2.put("pageCount", "635");
    book2.put("authorId", "author-2");
    book2.put("publicationDate", "18510101000000");

    Map<String, String> book3 = new HashMap<>();
    book3.put("id", "book-3");
    book3.put("name", "Interview with the vampire");
    book3.put("pageCount", "371");
    book3.put("authorId", "author-3");
    book3.put("publicationDate", "19760505000000");

    books.add(book1);
    books.add(book2);
    books.add(book3);
  }

  private static void initAuthors() {
    authors = new ArrayList<>();

    Map<String, String> author1 = new HashMap<>();
    author1.put("id", "author-1");
    author1.put("firstName", "Joanne");
    author1.put("lastName", "Rowling");
    author1.put("aType", AuthorType.MEH.toString());

    Map<String, String> author2 = new HashMap<>();
    author2.put("id", "author-2");
    author2.put("firstName", "Herman");
    author2.put("lastName", "Melville");

    Map<String, String> author3 = new HashMap<>();
    author3.put("id", "author-3");
    author3.put("firstName", "Anne");
    author3.put("lastName", "Rice");

    authors.add(author1);
    authors.add(author2);
    authors.add(author3);
  }

  public DataFetcher getBookByIdDataFetcher() {
    return dataFetchingEnvironment -> {
      String bookId = dataFetchingEnvironment.getArgument("id");
      return books.stream().filter(book -> book.get("id").equals(bookId)).findFirst().orElse(null);
    };
  }

  public DataFetcher getAuthorDataFetcher() {
    return dataFetchingEnvironment -> {
      Map<String, String> book = dataFetchingEnvironment.getSource();
      String authorId = book.get("authorId");
      return authors.stream().filter(author -> author.get("id").equals(authorId)).findFirst().orElse(null);
    };
  }

  public DataFetcher createBook() {
    return dataFetchingEnvironment -> {
        // id: ID!, name: String, pageCount: Int, publicationDate: DateTime, authorId: ID!
        String id = dataFetchingEnvironment.getArgument("id");
        String authorId = dataFetchingEnvironment.getArgument("authorId");
        String name = dataFetchingEnvironment.getArgument("name");
        String pageCount = dataFetchingEnvironment.getArgument("pageCount");
        String publicationDate = dataFetchingEnvironment.getArgument("publicationDate");
        Map<String, String> existingBook = books.stream().filter(b -> b.get("id").equals(id)).findFirst().orElse(null);
        if (existingBook != null) {
          System.out.println("Book with given ID already exists.");
          return null;
        }
        Map<String, String> author = authors.stream().filter(a -> a.get("id").equals(authorId)).findFirst().orElse(null);
        if (author == null) {
          return null;
        }
        Map<String, String> book = new HashMap<>();
        book.put("id", id);
        book.put("name", name);
        book.put("pageCount", pageCount);
        book.put("authorId", authorId);
        book.put("publicationDate", publicationDate);
        books.add(book);
        return book;
    };
  }
}
