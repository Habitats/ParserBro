package parserBro;

public abstract class Log {

	public static void i(String string) {
		logMessage("INFO: " + string);
	}

	public static void w(String string) {
		logMessage("WARNING: " + string);
	}

	public static void e(String string) {
		logMessage("ERROR:" + string);
	}

	protected static void logMessage(String string) {
		System.out.println(string);
	}
}
