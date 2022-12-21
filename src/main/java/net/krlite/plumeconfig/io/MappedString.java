package net.krlite.plumeconfig.io;

public class MappedString {
	private final String content;
	private final String replacement;

	public MappedString(String content) {
		this(content, "");
	}

	public MappedString(String content, String replacement) {
		this.content = content;
		this.replacement = replacement;
	}

	public String get() {
		return content;
	}

	public boolean isEmpty() {
		return content.isEmpty();
	}

	public MappedString mapLineBreaks() {
		return new MappedString(content.replaceAll("\n", replacement), replacement);
	}

	public MappedString mapSpaces() {
		return new MappedString(content.replaceAll(" ", "_"), replacement); // Map spaces to underscores
	}
}
