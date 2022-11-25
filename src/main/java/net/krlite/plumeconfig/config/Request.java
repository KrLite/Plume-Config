package net.krlite.plumeconfig.config;

import net.krlite.plumeconfig.Formatter;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.option.core.Option;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Scanner;

public class Request {
	public final File file;
	// For loading
	public final HashMap<String, String> config = new HashMap<>();
	// For writing
	public String formattedConfig = "";

	public Request(@NotNull Path path, String fileName) {
		this.file = path.resolve(fileName + ".properties").toFile();
	}

	public void clear() {
		formattedConfig = "";
	}

	public void write() throws IOException {
		PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
		writer.write(formattedConfig);
		writer.close();
	}

	public void load() throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		for (int line = 1; scanner.hasNextLine(); line++)
			parseConfigEntry(scanner.nextLine());
	}

	public void create() throws IOException {
		file.getParentFile().mkdirs();
		Files.createFile(file.toPath());
	}

	public void appendCategory(String category, boolean isRoot) {
		formattedConfig += (isRoot ? "" : Formatter.END) + Formatter.formatCategory(category) + Formatter.END_LINE;
	}

	public <T extends Option<?>> void appendOption(@NotNull T option) {
		formattedConfig += option.format();
	}

	private void parseConfigEntry(@NotNull String entry) {
		if (!entry.isEmpty() && !entry.startsWith("#")) {
			String[] entryLine = entry.split("=", 2);

			if (entryLine.length == 2)
				config.put(entryLine[0], entryLine[1]);
		}
	}
}
