package net.krlite.plumeconfig;

public class Formatter {
	public static final String END = "\n";
	public static final String END_LINE = "\n\n";
	public static final String END_CATEGORY = "\n\n\n";

	public static String newLine(String content) {
		return END + content;
	}

	public static String formatCategory(String category) {
		return "# | " + category + " |";
	}

	public static <T> String formatOption(String name, String key, T value, T defaultValue, String comment) {
		return formatName(name)
					   + newLine(formatContent(key, value))
					   + newLine(formatDefaultValue(defaultValue))
					   + formatComment(comment);
	}

	private static String formatName(String name) {
		return (name != null ? "# " + name : "");
	}

	private static <T> String formatContent(String key, T value) {
		return key + "=" + value.toString();
	}

	private static <T> String formatDefaultValue(T defaultValue) {
		return "# Default: " + defaultValue.toString();
	}

	private static String formatComment(String comment) {
		return (comment != null ? newLine("# " + comment) : "");
	}
}
