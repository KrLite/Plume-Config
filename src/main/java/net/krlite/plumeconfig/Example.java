package net.krlite.plumeconfig;

import net.krlite.plumeconfig.annotation.Comment;
import net.krlite.plumeconfig.annotation.Option;

import java.awt.*;

public class Example {
	// This is an integer comment
	public @Comment int comment = 1;

	// This is a string comment
	public @Comment String comment2 = "comment2\ncommented line";

	// This is a string option
	@Option(name = "String", comment = "A String\n Comment")
	public String s = "string";

	// This is an integer option
	@Option(key = "integer", name = "Int")
	public int i = 1;

	// This is a string comment
	private final @Comment String comment3 = "comment3";

	// This is a double option, but final, so cannot be changed
	private static final @Option double d = 1.0;

	// This is a regular field to not being read and written
	public static int silent = 1;

	public static int silent2 = 1;

	// This is a color option
	public static @Option Color color = Color.BLACK;

	// This is a boolean option
	public static @Option boolean bool;
}
