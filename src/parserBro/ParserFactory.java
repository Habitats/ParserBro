package parserBro;

import parserBro.parsers.MiscParser;
import parserBro.parsers.movies.ImdbParser;
import parserBro.parsers.tvParser.TvShowParser;
import parserBro.parsers.youTube.YoutubeParser;

public class ParserFactory {

  public static ImdbParser createImdbParser() {
    return new ImdbParser();
  }

  public static MiscParser createMiscParser() {
    return new MiscParser();
  }

  public static YoutubeParser createYoutubeParser() {
    return new YoutubeParser();
  }

  public static TvShowParser createTvShowParser() {
    return new TvShowParser();
  }
}
