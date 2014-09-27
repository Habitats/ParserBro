package parserBro.parsers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

import parserBro.Config;
import parserBro.Log;
import parserBro.NotFoundException;

public abstract class NoCookieParser {

  protected Document genSource(String url) throws NotFoundException {

    // get actual info
    int tries = 1;
    Document source = null;
    while (tries <= Config.HTTP_RETRIES) {
      try {
        source =
            Jsoup.connect(url).timeout(Config.HTTP_TIMEOUT_SECONDS * 1000)
                .userAgent("Mozilla/5.0 (Windows NT 6.2; WOW64; rv:20.0) Gecko/20100101 Firefox/20.0")
                .ignoreContentType(true).get();
        return source;
      } catch (IOException e) {
        Log.w("GEN SOURCE FAILED, RETRYING: " + tries + " URL: " + url);
        // e.printStackTrace();
        try {
          Thread.sleep(Config.HTTP_TIMEOUT_WAIT);
        } catch (InterruptedException e1) {
        }
      }
      tries++;
    }
    throw new NotFoundException();
  }
}
