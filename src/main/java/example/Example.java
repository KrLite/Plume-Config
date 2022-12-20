package example;

import net.krlite.plumeconfig.annotation.Comment;
import net.krlite.plumeconfig.annotation.Option;

import java.awt.*;

public class Example {
	public @Comment int comment = 1;

	public @Comment String comment2 = "comment2";

	@Option(name = "String", comment = "A String Comment")
	public String s = "string";

	@Option(key = "integer", name = "Int")
	public int i = 1;

	private final @Comment String comment3 = "comment3";

	private static final @Option double d = 1.0;

	public static int silent = 1;

	public static int silent2 = 1;

	public static @Option Color color = Color.BLACK;

	public static @Option boolean bool;
}
