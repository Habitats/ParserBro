package parserBro.parsers.tvParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import parserBro.DateMgmt;
import parserBro.NotFoundException;
import parserBro.parsers.NoCookieParser;


public class TvShowParser extends NoCookieParser {
	private TvShow getShowQuckInfo(String showName) throws Exception {
		Document quickSource = genSource("http://services.tvrage.com/tools/quickinfo.php?show=" + showName.replaceAll(" ", "+"));
		HashMap<String, String> info = new HashMap<String, String>();

		if (quickSource == null)
			return null;
		for (String infoLine : quickSource.text().split("\n")) {
			String[] var = infoLine.split("@");
			String arg = var[0];
			String val = var.length == 2 ? var[1] : "";
			info.put(arg, val);
		}
		if (quickSource.text().contains("No Show Results Were Found For"))
			return new TvShow("That's no show!");

		String id = info.get("Show ID");
		String title = info.get("Show Name");
		String url = info.get("Show URL");
		Date started = null;
		Date ended = null;
		Date latestEpisodeDate = null;

		/* was a pain in the ass to get airtime from the other feed */
		String airtime = null;
		String ampm = null;
		for (String s : info.get("Airtime").split(" ")) {
			if (s.matches("[0-9]{2}:[0-9]{2}"))
				airtime = s;
			if (s.matches("am|pm"))
				ampm = s;
		}

		if (airtime == null) {
			airtime = genSource("http://services.tvrage.com/feeds/full_show_info.php?sid=" + id).select("airtime").text();
		}

		DateFormat sdf = new SimpleDateFormat("MMM/dd/yyyy hh:mm a", Locale.US);
		DateFormat sdf2 = new SimpleDateFormat("MMM/dd/yyyy", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone(DateMgmt.isDTS() ? "GMT-4" : "EST"));
		sdf2.setTimeZone(TimeZone.getTimeZone(DateMgmt.isDTS() ? "GMT-4" : "EST"));
		String latestEpisodeNumber = null;
		String latestEpisodeTitle = null;
		try {
			started = sdf2.parse(info.get("Started"));
			ampm = "PM";
			latestEpisodeDate = sdf.parse(info.get("Latest Episode").split("\\^")[2] + " " + airtime + " " + ampm);
			latestEpisodeNumber = "S" + info.get("Latest Episode").split("\\^")[0].split("x")[0] + "E" + info.get("Latest Episode").split("\\^")[0].split("x")[1];
			latestEpisodeTitle = info.get("Latest Episode").split("\\^")[1];
			ended = sdf2.parse(info.get("Ended"));
		} catch (Exception e) {
			System.err.println(e);
		}
		String country = info.get("Country");
		String status = info.get("Status");
		String classification = info.get("Classification");
		String network = info.get("Network");
		String runtime = info.get("Runtime");

		TvShow tvShow = new TvShow(id, title, latestEpisodeTitle, latestEpisodeNumber, latestEpisodeDate, started, ended, status, runtime, country, classification, network, url);

		return tvShow;
	}

	public TvShow getTvShowInfo(String showName) throws NotFoundException {
		TvShow show;
		try {
			show = getShowQuckInfo(showName);
			getShowNextEpisodeInfo(show);
			return show;
		} catch (Exception e) {
			throw new NotFoundException();
		}
	}

	public String[][] getShowNextEpisodeInfo(String showName) throws NotFoundException {
		TvShow show;
		try {
			show = getShowQuckInfo(showName);
			getShowNextEpisodeInfo(show);
			return show.toStringNext();
		} catch (Exception e) {
			throw new NotFoundException();
		}
	}

	public String[][] getShowLastEpisodeInfo(String showName) throws NotFoundException {
		TvShow show;
		try {
			show = getShowQuckInfo(showName);
			return show.toStringLast();
		} catch (Exception e) {
			throw new NotFoundException();
		}
	}

	public String[][] getShowInfoSimple(String showName) throws NotFoundException {
		try {
			return getShowQuckInfo(showName).toStringQuickInfoSimple();
		} catch (Exception e) {
			throw new NotFoundException();
		}
	}

	public String[][] getShowInfoAdvanced(String showName) throws NotFoundException {
		try {
			return getShowQuckInfo(showName).toStringQuickInfo();
		} catch (Exception e) {
			throw new NotFoundException();
		}
	}

	private void getShowNextEpisodeInfo(TvShow show) throws Exception {
		Document episodesSource = genSource("http://services.tvrage.com/feeds/full_show_info.php?sid=" + show.getId());

		Date now = new Date();
		Date nextDate = null;
		String episodeTitle = "";
		Date episodeDate = null;
		String episodeNumber = "";
		String airtime = episodesSource.select("airtime").text();
		String timezone = episodesSource.select("timezone").text().split(" ")[0];

		if (DateMgmt.isDTS()) {
			int oldVal = Integer.parseInt(timezone.replace("GMT", ""));
			int newVal = ++oldVal % 12;

			timezone = "GMT" + newVal;
		}
		for (Element episode : episodesSource.select("episodelist episode")) {
			String nextDateStr = episode.select("airdate").text() + " " + airtime + " " + timezone;
			nextDateStr = nextDateStr.replaceAll("(GMT[+-])(\\d)$", "$1\\0$2");
			nextDateStr = nextDateStr.replaceAll("(GMT[+-]\\d\\d)$", "$1:00");
			nextDate = new SimpleDateFormat("yyyy-MM-dd HH:mm z", Locale.US).parse(nextDateStr);
			if (nextDate.getTime() > now.getTime()) {
				episodeTitle = episode.select("title").text();
				episodeNumber = "S" + String.format("%02d", Integer.parseInt(episode.parent().attr("no"))) + "E" + episode.select("seasonnum").text();
				episodeDate = nextDate;

				show.setNextEpisodeTitle(episodeTitle);
				show.setNextEpisodeNumber(episodeNumber);
				show.setNextEpisodeDate(episodeDate);
				break;
			}
		}
	}

}
