package example;

import net.krlite.plumeconfig.annotation.Category;
import net.krlite.plumeconfig.annotation.Comment;
import net.krlite.plumeconfig.annotation.Option;

import java.awt.*;

public class Example {
	public @Comment int comment = 1; // Integer comment

	public @Comment String comment2 = "comment2\ncommented line"; // Multi-line comment

	@Option(name = "String", comment = "A String\n Comment")
	@Category("abc")
	public String s = "string"; // A String option named "String" with a comment(line breaks will be ignored)

	@Option(key = "integer", name = "Int") // An Integer option named "Int" with a specified key "integer"
	public int i = 1;

	@Category("abc")
	private final @Comment String comment3 = "comment3"; // A comment in the category "abc"

	@Category("abc")
	private static final @Option double d = 1.0; // A double option in the category "abc"

	public static int silent = 1; // No annotations, will be ignored

	private static final int silent2 = 1;

	@Category("def")
	public static @Option Color color = Color.BLACK; // A Color option in the category "def"

	public static @Option boolean bool; // A boolean option
}
