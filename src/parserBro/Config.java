package parserBro;

import java.util.TimeZone;

public class Config {
	public static final int HTTP_RETRIES = 3;
	public static final int HTTP_TIMEOUT_SECONDS = 5;

	/* how long should the thread sleep before retrying after timeout */
	public static final long HTTP_TIMEOUT_WAIT = 1000;

	/* timezone of system */
	public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("UTC");

}
