package net.krlite.plumeconfig.config;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.krlite.plumeconfig.json.ConfigSerializer;
import net.krlite.plumeconfig.option.core.Option;

import java.io.File;
import java.util.HashMap;

public abstract class Config extends ConfigSerializer {
	/** The config file. */
	File file;

	/**
	 * Creates a new config by name.
	 * @param fileName	The name of the config file.
	 */
	public Config(String fileName) {
		file = FabricLoader.getInstance().getConfigDir().resolve(fileName + ".json5").toFile();
	}

	/**
	 * Creates a new config by path and name.
	 * @param pathName	The parent path of the config file.
	 * @param fileName	The name of the config file.
	 */
	public Config(String pathName, String fileName) {
		file = FabricLoader.getInstance().getConfigDir().resolve(pathName).resolve(fileName + ".json5").toFile();
	}

	/**
	 * Reads the config from file.
	 */
	public abstract JsonObject read();

	/**
	 * Writes the config into file.
	 * @param options	A list of options needed to be written.
	 */
	public abstract void write(Option<?>... options);
}
