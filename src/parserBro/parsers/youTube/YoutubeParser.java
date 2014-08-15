package parserBro.parsers.youTube;


import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import parserBro.NotFoundException;
import parserBro.parsers.NoCookieParser;


public class YoutubeParser extends NoCookieParser {

	public Youtube getYouTubeInfoFromSearch(String searchTerm) throws NotFoundException {
		Document source = genSource("http://gdata.youtube.com/feeds/api/videos?q=" + searchTerm);
		String url = source.select("entry id").first().text();
		return getYoutubeInfo(url);

	}

	public Youtube getYoutubeInfo(String url) throws NotFoundException {
		Youtube vid = new Youtube(url);
		Document source = genSource(vid.getApiUrl());

		Element video = source.select("entry").first();
		String title = video.select("title").text();
		int numRaters = Integer.parseInt(video.getElementsByAttribute("numRaters").attr("numRaters"));
		double average = Double.parseDouble(video.getElementsByAttribute("average").attr("average"));
		int views = Integer.parseInt(video.getElementsByAttribute("viewCount").first().attr("viewCount"));

		vid.generateInfo(title, numRaters, average, views);

		return vid;
	}
}
