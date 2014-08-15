package parserBro.parsers.tvParser;

import java.util.Date;

import parserBro.DateMgmt;

public class TvEpisode {

	private final Date airDate;
	private final int episodeNumber;
	private final int seasonNumber;
	private final String title;
	private final TvShow show;

	public TvEpisode(String title, int seasonNumber, int episodeNumber, Date airDate, TvShow show) {
		this.title = title;
		this.episodeNumber = episodeNumber;
		this.seasonNumber = seasonNumber;
		this.airDate = airDate;
		this.show = show;
	}

	public String getFormattedEpisodeNubmer() {
		String formattedEpisodeNumber = String.format("S%02dE%02d", seasonNumber, episodeNumber);
		return formattedEpisodeNumber;
	}

	public Date getAirTime() {
		return airDate;
	}

	public String getTitle() {
		return title;
	}

	public String toString() {
		return "Title: " + title + " - " + "Episode Number: " + getFormattedEpisodeNubmer() + " - Airdate: " + DateMgmt.getFormattedDate(airDate);
	}
}
