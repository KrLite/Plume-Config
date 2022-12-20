package net.krlite.plumeconfig.io;

import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.exception.FileException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Writer {
	private final File file;

	public Writer(File file) {
		this.file = file;
	}

	public void create() throws IOException {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
			PlumeConfigMod.LOGGER.info("Created config file " + file.toPath());
		}
	}

	public void initialize() throws IOException {
		create();
		FileWriter writer = new FileWriter(file);
		writer.write("");
		writer.flush();
		writer.close();
	}

	public void write(String content) {
		write(content, false);
	}

	public void write(String content, boolean lineBreak) {
		try {
			create();
		} catch (IOException ioException) {
			FileException.traceFileInitializingException(PlumeConfigMod.LOGGER, ioException, file);
		}
		try {
			FileWriter writer = new FileWriter(file, true);
			writer.write(content);
			if (lineBreak) writer.write("\n");
			writer.flush();
			writer.close();
		} catch (IOException ioException) {
			FileException.traceFileWritingException(PlumeConfigMod.LOGGER, ioException, file);
		}
	}

	public void nextLine() {
		write("", true);
	}
}
