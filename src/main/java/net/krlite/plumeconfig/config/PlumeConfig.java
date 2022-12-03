package net.krlite.plumeconfig.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.option.core.Option;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class PlumeConfig extends Config {
	public PlumeConfig(String pathName, String fileName) {
		super(pathName, fileName);
	}

	class ConfigExceptions {
		final String INITIALIZING = "Failed initializing config file: " + file.getName();
		final String SERIALIZING = "Failed serializing config file: " + file.getName();

		private <T extends Exception> RuntimeException throwException(String message, T e) {
			PlumeConfigMod.LOGGER.error(message);
			return new RuntimeException(e);
		}

		public RuntimeException throwFileInitializingException(IOException ioException) {
			return throwException(INITIALIZING, ioException);
		}

		public RuntimeException throwFileSerializingException(IOException ioException) {
			return throwException(SERIALIZING, ioException);
		}
	}

	private void initializeConfig() throws IOException {
		file.getParentFile().mkdirs();
		Files.createFile(file.toPath());
	}

	private void writeToFile(Option<?>... options) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(deSerialize(options));
		try {
			PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
			writer.write(json);
			writer.close();
		} catch (IOException e) {
			try {
				initializeConfig();
				writeToFile(options);
			} catch (IOException ioException) {
				throw new ConfigExceptions().throwFileInitializingException(ioException);
			}
		}
	}

	private FileReader readFromFile() {
		try {
			return new FileReader(file);
		} catch (FileNotFoundException e) {
			try {
				initializeConfig();
				return readFromFile();
			} catch (IOException ioException) {
				throw new ConfigExceptions().throwFileInitializingException(ioException);
			}
		}
	}

	@Override
	public JsonObject read() {
		try {
			return serialize(readFromFile());
		} catch (IOException ioException) {
			throw new ConfigExceptions().throwFileSerializingException(ioException);
		}
	}

	@Override
	public void write(Option<?>... options) {
		writeToFile(options);
	}
}
