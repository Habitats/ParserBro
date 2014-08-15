package parserBro.parsers.youTube;

public class Youtube {

	private String url;
	private String title;
	private double rating;
	private int views;
	private int raters;
	private int likes;
	private int dislikes;

	// optional visual representation
	private String ratingsBar;

	private String id;

	public Youtube(String url) {
		setIdFromUrl(url);
		this.url = url;
	}

	private double convertRatingToPercent(double rating) {
		return rating *= (2.0 / 10.0);
	}

	private void setIdFromUrl(String url) {
		if (url.toLowerCase().contains("gdata.youtube.com/feeds/api/videos") || url.toLowerCase().contains("youtu.be/"))
			id = url.split("/")[url.split("/").length - 1];
		else
			id = url.split("v=")[1].split("&")[0];
	}

	public void generateInfo(String title, int raters, double rating, int views) {
		this.title = title;
		this.views = views;

		double percentage = convertRatingToPercent(rating);
		likes = (int) (raters * percentage);
		dislikes = (raters - likes);
	}

	private String getViewsFormatted() {
		return String.format("%,d", views);
	}

	public String[][] getInfo() {
		String[][] res = { { "YouTube", title },//
				{ "Views", getViewsFormatted() },//
				{ null, ratingsBar },//
				{ "URL", getShortUrl() } };
		return res;
	}

	public String[][] getInfoNoUrl() {
		String[][] res = { { "YouTube", title },//
				{ "Views", getViewsFormatted() },//
				{ null, ratingsBar } };//
		return res;
	}

	public String getShortUrl() {
		return "https://youtu.be/" + id;
	}

	public String getApiUrl() {
		return "http://gdata.youtube.com/feeds/api/videos/" + id;
	}

	public String getApiQueryUrl() {
		return "";
	}

	public String getOriginalUrl() {
		return url;
	}

	public double getRating() {
		return rating;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public String getRatingsBar() {
		return ratingsBar;
	}

	public int getDislikes() {
		return dislikes;
	}

	public int getLikes() {
		return likes;
	}

	public void setRatingsBar(String ratingsBar) {
		this.ratingsBar = ratingsBar;
	}
}
