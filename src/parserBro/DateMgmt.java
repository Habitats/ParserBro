package parserBro;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateMgmt {

	public static void main(String[] args) {
		getCurrentYear();
		getCurrentWeek();
	}

	// eee = GMT+2
	// forum = GMTi

	public static long convertSiteDateToEpoc(String date, String dateFormat) {
		long epoc = 0;

		try {
			epoc = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return epoc;
	}

	public static String convertEpocToReadable(long epoc) {
		String date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date(epoc * 1000));
		return date;
	}

	public static boolean isDTS() {
		return TimeZone.getDefault().inDaylightTime(new Date());
	}

	public static long convertFromWeekNrToEpoc(int week, int year) {
		long epoc = 0;
		if (year == 0)
			year = 2012;
		// had to add this cuz on nix sunday is default for some fuckign reason
		// and this is the only workaround
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		SimpleDateFormat format = new SimpleDateFormat("ww yyyy z");
		format.setCalendar(cal);

		try {

			epoc = format.parse(String.format("%s %s GMT+01:00", week, year)).getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return epoc;

	}

	public static long convertFromYearToEpoc(int year) {
		long epoc = 0;
		if (year == 0)
			year = 2012;

		try {
			epoc = new SimpleDateFormat("yyyy").parse(String.format("%s GMT+01:00", year)).getTime() / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return epoc;

	}

	public static int getCurrentWeek() {
		// had to add this cuz on nix sunday is default for some fuckign reason
		// and this is the only workaround
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);

		SimpleDateFormat format = new SimpleDateFormat("ww");
		format.setCalendar(cal);
		format.format(System.currentTimeMillis());

		int week = Integer.parseInt(format.format(System.currentTimeMillis()));
		return week;
	}

	public static int getCurrentYear() {
		int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(System.currentTimeMillis()));
		return year;
	}

	/*
	 * takes in MS and formats it like: years, days, hours and minutes, either
	 * in the future or past, depending on the sign
	 */

	private static String formatMs(long ms) {

		long days = TimeUnit.MILLISECONDS.toDays(ms);
		long hours = TimeUnit.MILLISECONDS.toHours(ms - TimeUnit.DAYS.toMillis(days));
		long minutes = TimeUnit.MILLISECONDS.toMinutes(ms - TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hours));

		int year = (int) Math.floor(days / 365.);

		String inclYear = year != 0 ? String.format("%d year%s, ", year, year == 1 ? "" : "s") : "";
		if (inclYear.length() != 0)
			days = (int) (days % 365);
		String inclDays = days != 0 || inclYear.length() != 0 ? String.format("%d day%s, ", days, days == 1 ? "" : "s") : "";
		String inclHours = hours != 0 || inclDays.length() != 0 ? String.format("%d hour%s and ", hours, hours == 1 ? "" : "s") : "";
		String inclMinutes = String.format("%d minute%s", minutes, minutes == 1 ? "" : "s");

		return inclYear + inclDays + inclHours + inclMinutes;
	}

	public static String getFormattedTilDate(Date nextDate) {
		long msTilNext = nextDate.getTime() - new Date().getTime();
		return formatMs(msTilNext);
	}

	public static String getFormattedSinceDate(Date sinceDate) {
		long msSinceLast = new Date().getTime() - sinceDate.getTime();
		return formatMs(msSinceLast);
	}

	public static String getFormattedDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
		sdf.setTimeZone(Config.TIME_ZONE);
		return date != null ? sdf.format(date) : null;
	}

	public static String getFormattedAirTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm z", Locale.UK);
		sdf.setTimeZone(Config.TIME_ZONE);
		return date != null ? sdf.format(date) : null;
	}
}
