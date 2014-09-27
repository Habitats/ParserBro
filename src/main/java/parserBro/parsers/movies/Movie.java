package parserBro.parsers.movies;

public class Movie {

  private String title;
  private String rating;
  private String url;
  private String year;

  public Movie(String title, String ratingCount, String rating, String year, String url) {
    this.title = title;
    this.rating = rating;
    this.year = year;
    rating += " (" + ratingCount + " votes)";
    this.url = url;
  }

  public String[][] getInfo() {

    String[][] res = {{"IMDB", title},//
                      {"Year", year},//
                      {"Rating", rating},//
                      {"URL", url},//
    };
    return res;
  }
}
