package example;

import net.krlite.plumeconfig.annotation.Category;
import net.krlite.plumeconfig.annotation.Comment;
import net.krlite.plumeconfig.annotation.Option;
import net.krlite.plumeconfig.io.LineBreak;

import java.awt.*;

@Comment("This is a comment")
@Comment(value = "at the top of the file", end = LineBreak.AFTER)
public class Example {
	@Category("abc")
	@Comment(value = "Multi-line\ncomment, before the field", beforeField = true)
	@Option(name = "String", comment = "A string\n comment")
	public String s = "string";

	@Comment("An integer")
	@Option(key = "integer", name = "Int")
	public int i = 1;

	@Category("abc")
	private static final @Option double d = 1.0;

	public static int silent = 1;

	@Category("def")
	@Comment("A color field")
	@Comment("which is under supported")
	public static @Option Color color = Color.BLACK;

	public static @Option boolean bool;
}
