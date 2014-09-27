package parserBro.parsers;

import org.jsoup.nodes.Document;

import parserBro.NotFoundException;

public class MiscParser extends NoCookieParser {

  public String[][] imgurParse(String url) throws NotFoundException {
    Document source = genSource(url);
    String title = source.select("title").text().split(" - Imgur")[0];
    String[][] res = {{"imgur", title}};
    if (!title.startsWith("imgur") && title.length() > 0) {
      return res;
    }
    throw new NotFoundException();
  }

  public String[][] getGoogleResult(String searchTerm) throws NotFoundException {
    String url = "https://www.google.com/search?q=" + searchTerm;
    String title;

    Document source = genSource(url);

    try {
      title = source.select("div.std > h3.r").first().text();
      String[][] res = {{"Google", title}};
      return res;

    } catch (NullPointerException e2) {
      // name = source.select("div.vsc > h3.r").first().text();
      title = source.select("div.rc h3.r").first().text();
      url = source.select("div.rc h3.r").first().select("a[href]").attr("href");
      String[][] res = {{"Google", title}, {"URL", url}};
      return res;
    }
  }

}
