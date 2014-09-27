package parserBro.parsers.movies;


import org.jsoup.nodes.Document;

import parserBro.parsers.NoCookieParser;


public class ImdbParser extends NoCookieParser {

  public Movie getImdbInfo(String movieTitle) throws Exception {
    String
        apiUrl =
        "http://mymovieapi.com/?type=xml&plot=simple&episode=1&limit=1&yg=0&mt=none&lang=en-US&offset=&aka=simple&release=simple&business=0&tech=0&title="
        + movieTitle;
    Document source = genSource(apiUrl);
    String title = source.select("title").text();
    String rating = null;
    rating = source.select("rating").text();
    String ratingCount = source.select("rating_count").text();
    String year = source.select("year").text();
    String url = source.select("imdb_url").text();
    if (title.length() == 0) {
      throw new Exception("That's no movie!");
    }
    Movie movie = new Movie(title, ratingCount, rating, year, url);
    return movie;
  }

}
