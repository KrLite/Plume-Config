package net.krlite.plumeconfig;

public class Formatter {
	public static final String END = "\n";
	public static final String END_LINE = "\n\n";
	public static final String END_CATEGORY = "\n\n\n";

	public static String formatCategory(String category) {
		return "# | " + category + " |";
	}

	public static <T> String formatOption(String name, String key, T value, T defaultValue, String comment) {
		return formatName(name)
					   + formatContent(key, value)
					   + formatDefaultValue(defaultValue)
					   + formatComment(comment);
	}

	private static String formatName(String name) {
		return (name != null ? "# " + name + END : "");
	}

	private static <T> String formatContent(String key, T value) {
		return key + "=" + value.toString() + END;
	}

	private static <T> String formatDefaultValue(T defaultValue) {
		return "# Default: " + defaultValue.toString() + END;
	}

	private static String formatComment(String comment) {
		return (comment != null ? "# " + comment + END_LINE : END);
	}
}
