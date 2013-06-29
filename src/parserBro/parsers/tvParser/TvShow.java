package parserBro.parsers.tvParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import lombok.Data;

import parserBro.Config;
import parserBro.DateMgmt;

public class TvShow {
	/* corresponds with tvrage quickinfo */
	private String id;
	private String title;
	private Date started;
	private String url;
	private String premiered;
	private Date ended;
	private String status;
	private String country;
	private String classification;
	private String[] genres;
	private String network;
	private String runtime;
	private String timezone;
	private String imageUrl;

	private TvEpisode latestEpisode;
	private TvEpisode nextEpisode;
	
	private ArrayList<ArrayList<TvEpisode>> episodes;

	/* end quickinfo */

	public TvShow(String id, String title, Date started, Date ended, String status, String runtime, String country, String classification, String network, String url, String imageUrl) {
		this.id = id;
		this.title = title;
		this.started = started;
		this.ended = ended;
		this.status = status;
		this.runtime = runtime;
		this.country = country;
		this.classification = classification;
		this.network = network;
		this.url = url;
	}

	public TvShow(String title) {
		this.title = title;
	}

	

	private String getLatestEpisode() {
		TimeZone zone = TimeZone.getDefault();
		System.out.println(zone.getDisplayName());
		System.out.println(zone.getID());
		try {
			return DateMgmt.getFormattedDate(latestEpisode.getAirTime()) + " " + DateMgmt.getFormattedAirTime(latestEpisode.getAirTime()) + " (" + DateMgmt.getFormattedSinceDate(latestEpisode.getAirTime()) + " ago)";
		} catch (Exception e) {
			return "Nothing aired yet";
		}
	}

	private String getNextEpisode() {
		try {
			return DateMgmt.getFormattedDate(nextEpisode.getAirTime()) + " " + DateMgmt.getFormattedAirTime(nextEpisode.getAirTime()) + " (in " + DateMgmt.getFormattedTilDate(nextEpisode.getAirTime()) + ")";
		} catch (Exception e) {
			return "TBA";
		}
	}

	public String[][] toStringQuickInfo() {
		String[][] availableForAll = { //
		{ "TV", title },//
				{ "Latest Episode", getLatestEpisode() },//
				{ "Start", DateMgmt.getFormattedDate(started) },//
				{ "End", DateMgmt.getFormattedDate(ended) },//
				{ "Status", status },//
				{ "Runtime", runtime },//
				{ "Country", country },//
				{ "Type", classification },//
				{ "Network", network },//
				{ "URL", url },//
		};

		return availableForAll;
	}

	public String[][] toStringQuickInfoSimple() {
		String[][] availableForAll = { //
		{ "TV", title },//
				{ "Latest Episode", getLatestEpisode() },//
				{ "Number", latestEpisode.getFormattedEpisodeNubmer() },//
				{ "Start", DateMgmt.getFormattedDate(started) },//
				{ "End", DateMgmt.getFormattedDate(ended) },//
				{ "Status", status },//
				{ "URL", url },//
		};

		return availableForAll;
	}

	public String[][] toStringLast() {
		String[][] availableForAll = { //
		{ "TV", title },//
				{ "Latest Episode", getLatestEpisode() },//
				{ "Number", latestEpisode.getFormattedEpisodeNubmer() },//
				{ "Title", latestEpisode.getTitle() },//
		};

		return availableForAll;
	}

	public String[][] toStringNext() {
		String[][] availableForAll = { //
		{ "TV", title },//
				{ "Next Episode", getNextEpisode() },//
				{ "Number", nextEpisode.getFormattedEpisodeNubmer() },//
				{ "Title", nextEpisode.getTitle() },//
		};

		return availableForAll;
	}
	public void setEpisodes(ArrayList<ArrayList<TvEpisode>> arrayList) {
		this.episodes = arrayList;
	}

	public String getId() {
		return id;
	}

	public Date getStarted() {
		return started;
	}

	public String getUrl() {
		return url;
	}

	public String getPremiered() {
		return premiered;
	}

	public Date getEnded() {
		return ended;
	}

	public String getStatus() {
		return status;
	}

	public String getCountry() {
		return country;
	}

	public String getClassification() {
		return classification;
	}

	public String[] getGenres() {
		return genres;
	}

	public String getNetwork() {
		return network;
	}

	public String getRuntime() {
		return runtime;
	}

	public void setNextEpisode(TvEpisode nextEpisode) {
		this.nextEpisode = nextEpisode;
	}

	public void setLatestEpisode(TvEpisode latestEpisode) {
		this.latestEpisode = latestEpisode;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public String getTimezone() {
		return timezone;
	}

	public String getTitle() {
		return title;
	}
	public ArrayList<ArrayList<TvEpisode>> getEpisodes() {
		return episodes;
	}
}
