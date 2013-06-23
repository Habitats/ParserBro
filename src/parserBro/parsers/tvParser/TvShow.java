package parserBro.parsers.tvParser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

	private String latestEpisodeTitle;
	private String latestEpisodeNumber; // SSxEE
	private Date latestEpisodeDate; // MMMM/dd/yyyy

	private String nextEpisodeTitle;
	private String nextEpisodeNumber;
	private Date nextEpisodeDate;

	/* end quickinfo */

	public TvShow(String id, String title, String latestEpisodeTitle, String latestEpisodeNumber, Date latestEpisodeDate, Date started, Date ended, String status, String runtime, String country, String classification, String network, String url) {
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

		this.latestEpisodeTitle = latestEpisodeTitle;
		this.latestEpisodeNumber = latestEpisodeNumber;
		this.latestEpisodeDate = latestEpisodeDate;
	}

	public TvShow(String title) {
		this.title = title;
	}

	private String getFormattedDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
		sdf.setTimeZone(DateMgmt.TIME_ZONE);
		return date != null ? sdf.format(date) : null;
	}

	private String getFormattedAirTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm z", Locale.UK);
		sdf.setTimeZone(DateMgmt.TIME_ZONE);
		return date != null ? sdf.format(date) : null;
	}

	private String getLatestEpisode() {
		TimeZone zone = TimeZone.getDefault();
		System.out.println(zone.getDisplayName());
		System.out.println(zone.getID());
		try {
			return getFormattedDate(latestEpisodeDate) + " " + getFormattedAirTime(latestEpisodeDate) + " (" + DateMgmt.getFormattedSinceDate(latestEpisodeDate) + " ago)";
		} catch (Exception e) {
			return "Nothing aired yet";
		}
	}

	private String getNextEpisode() {
		try {
			return getFormattedDate(nextEpisodeDate) + " " + getFormattedAirTime(nextEpisodeDate) + " (in " + DateMgmt.getFormattedTilDate(nextEpisodeDate) + ")";
		} catch (Exception e) {
			return "TBA";
		}
	}

	public String[][] toStringQuickInfo() {
		String[][] availableForAll = { //
		{ "TV", title },//
				{ "Latest Episode", getLatestEpisode() },//
				{ "Start", getFormattedDate(started) },//
				{ "End", getFormattedDate(ended) },//
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
				{ "Number", latestEpisodeNumber },//
				{ "Start", getFormattedDate(started) },//
				{ "End", getFormattedDate(ended) },//
				{ "Status", status },//
				{ "URL", url },//
		};

		return availableForAll;
	}

	public String[][] toStringLast() {
		String[][] availableForAll = { //
		{ "TV", title },//
				{ "Latest Episode", getLatestEpisode() },//
				{ "Number", latestEpisodeNumber },//
				{ "Title", latestEpisodeTitle },//
		};

		return availableForAll;
	}

	public String[][] toStringNext() {
		String[][] availableForAll = { //
		{ "TV", title },//
				{ "Next Episode", getNextEpisode() },//
				{ "Number", nextEpisodeNumber },//
				{ "Title", nextEpisodeTitle },//
		};

		return availableForAll;
	}

	public String getId() {
		return id;
	}

	public void setNextEpisodeTitle(String nextEpisodeTitle) {
		this.nextEpisodeTitle = nextEpisodeTitle;
	}

	public void setNextEpisodeDate(Date nextEpisodeDate) {
		this.nextEpisodeDate = nextEpisodeDate;
	}

	public void setNextEpisodeNumber(String nextEpisodeNumber) {
		this.nextEpisodeNumber = nextEpisodeNumber;
	}

	public String getNextEpisodeTitle() {
		return nextEpisodeTitle;
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
}
