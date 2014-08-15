package test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import parserBro.NotFoundException;
import parserBro.ParserFactory;
import parserBro.parsers.tvParser.TvEpisode;
import parserBro.parsers.tvParser.TvShow;
import parserBro.parsers.tvParser.TvShowParser;

public class ParserBro {
	public static void main(String[] args) {
		System.setProperty("http.proxyHost", "localhost");
		System.setProperty("http.proxyPort", "8888");
		new ParserBro().run();
	}

	private void run() {
		testTvParse();
//		testJsoupProxy();
	}

	private void testJsoupProxy() {
		Document doc;
		try {
			doc = Jsoup.connect("http://www.charlietheunicorn.org").ignoreContentType(true).get();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void testTvParse() {
		TvShow show;
		try {
			TvShowParser parser = ParserFactory.createTvShowParser();
			show = parser.getTvShowInfo("Futurama");

			TvEpisode episode = parser.getEpisode(1, 1, show);
			System.out.println(episode);
			episode = parser.getEpisode(4, 8, show);
			System.out.println(episode);
			String asd = null;
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
	}
}
