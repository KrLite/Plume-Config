package example;

import net.krlite.plumeconfig.annotation.Category;
import net.krlite.plumeconfig.annotation.Comment;
import net.krlite.plumeconfig.annotation.Option;
import net.krlite.plumeconfig.io.LineBreak;

import java.awt.*;

@Comment("This is a comment")
@Comment(value = "at the top of the file", newLine = LineBreak.AFTER)
public class Example {
	@Comment("Multi-line\ncomment")
	@Option(name = "String", comment = "A String\n Comment")
	@Category("abc")
	public String s = "string"; // A String option named "String" with a comment(line breaks will be ignored)

	@Comment("An integer")
	@Option(key = "integer", name = "Int") // An Integer option named "Int" with a specified key "integer"
	public int i = 1;

	@Category("abc")
	private static final @Option double d = 1.0; // A double option in the category "abc"

	public static int silent = 1; // No annotations, will be ignored

	@Comment("This comment is useless and won't appear")
	private static final int silent2 = 1;

	@Comment("A color")
	@Comment("Which is under supported")
	@Category("def")
	public static @Option Color color = Color.BLACK; // A Color option in the category "def"

	@Comment("A boolean")
	public static @Option boolean bool; // A boolean option
}
