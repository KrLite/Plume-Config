package net.krlite.plumeconfig.io;

import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.exception.FileException;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.File;
import java.io.IOException;

public class Reader {
	private final File file;

	public Reader(File file) {
		this.file = file;
	}

	public TomlParseResult read() {
		try {
			return Toml.parse(file.toPath());
		} catch (IOException e) {
			PlumeConfigMod.LOGGER.error("Config file " + file.toPath() + " doesn't exist. Creating one.");
			try {
				new Writer(file).create();
				return read();
			} catch (IOException ioException) {
				FileException.traceFileReadingException(PlumeConfigMod.LOGGER, ioException, file);
				throw new RuntimeException(ioException);
			}
		}
	}
}
