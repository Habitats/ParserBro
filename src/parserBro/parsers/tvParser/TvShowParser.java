package parserBro.parsers.tvParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import parserBro.DateMgmt;
import parserBro.Log;
import parserBro.NotFoundException;
import parserBro.parsers.NoCookieParser;

public class TvShowParser extends NoCookieParser {
	private enum ShowInfo {
		NEXT, LAST, SIMPLE, ALL;
	}

	private final SimpleDateFormat rageDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);

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
			throw new NotFoundException(showName);

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
		int latestEpisodeNumber = -1;
		int seasonNumber = -1;
		String latestEpisodeTitle = null;
		try {
			started = sdf2.parse(info.get("Started"));
			ampm = "PM";
			latestEpisodeDate = sdf.parse(info.get("Latest Episode").split("\\^")[2] + " " + airtime + " " + ampm);
			seasonNumber = Integer.parseInt(info.get("Latest Episode").split("\\^")[0].split("x")[0]);
			latestEpisodeNumber = Integer.parseInt(info.get("Latest Episode").split("\\^")[0].split("x")[1]);
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
		String imageUrl = info.get("image");

		TvShow tvShow = new TvShow(id, title, started, ended, status, runtime, country, classification, network, url, imageUrl);
		tvShow.setLatestEpisode(new TvEpisode(latestEpisodeTitle, seasonNumber, latestEpisodeNumber, latestEpisodeDate, tvShow));

		return tvShow;
	}

	private void getShowNextEpisodeInfo(TvShow show) throws Exception {
		Document episodesSource = genSource("http://services.tvrage.com/feeds/full_show_info.php?sid=" + show.getId());

		Date now = new Date();
		Date nextDate = null;
		String episodeTitle = "";
		Date episodeDate = null;
		int episodeNumber = -1;
		int seasonNumber = -1;
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
				seasonNumber = Integer.parseInt(episode.parent().attr("no"));
				episodeNumber = Integer.parseInt(episode.select("seasonnum").text());
				episodeDate = nextDate;

				show.setNextEpisode(new TvEpisode(episodeTitle, seasonNumber, episodeNumber, episodeDate, show));
				break;
			}
		}
	}

	private ArrayList<ArrayList<TvEpisode>> getEpisodes(TvShow show) throws NotFoundException {
		Document epListSource = genSource("http://services.tvrage.com/feeds/episode_list.php?sid=" + show.getId());
		ArrayList<ArrayList<TvEpisode>> episodes = new ArrayList<ArrayList<TvEpisode>>();
		for (Element season : epListSource.select("Season")) {
			ArrayList<TvEpisode> seasonArr = new ArrayList<TvEpisode>();
			for (Element episode : season.select("episode")) {
				String title = episode.select("title").text();
				int episodeNumber = Integer.parseInt(episode.select("seasonnum").text());
				int seasonNumber = Integer.parseInt(season.attr("no"));
				String airDateStr = episode.select("airdate").text();
				Date airDate = null;
				try {
					airDate = rageDateFormat.parse(airDateStr);
				} catch (ParseException e) {
				}

				seasonArr.add(new TvEpisode(title, seasonNumber, episodeNumber, airDate, show));
			}
			episodes.add(seasonArr);
		}
		return episodes;
	}

	public TvEpisode getEpisode(int season, int episode, TvShow show) throws NotFoundException {
		try {
			return show.getEpisodes().get(season - 1).get(episode - 1);
		} catch (Exception e) {
			throw new NotFoundException(e);
		}
	}

	public TvShow getTvShowInfo(String showName) throws NotFoundException {
		TvShow show;
		try {
			show = getShowQuckInfo(showName);
			getShowNextEpisodeInfo(show);
			show.setEpisodes(getEpisodes(show));
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
		long ms = System.currentTimeMillis();
		Log.i("Starting lookup: " + showName);
		try {
			Log.i(String.format("Lookup done: %s (%d)", showName, System.currentTimeMillis() - ms));
			return getShowQuckInfo(showName).toStringQuickInfo();
		} catch (Exception e) {
			throw new NotFoundException();
		} finally {
			Log.i(String.format("Lookup done: %s (%d)", showName, System.currentTimeMillis() - ms));
		}
	}

}
