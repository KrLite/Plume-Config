package net.krlite.plumeconfig;

import net.fabricmc.api.ModInitializer;
import net.krlite.plumeconfig.example.ExampleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlumeConfigMod implements ModInitializer {
	public static final String MOD_ID = "plumeconfig";
	public static final Logger LOGGER = LoggerFactory.getLogger("Plume Config");

	@Override
	public void onInitialize() {
		/*
		 *
		 * This is an example of how to load and write the configs when you need to.
		 *
		 * LOGGER.info("Initializing config...");
		 * ExampleConfig exampleConfig = new ExampleConfig("example", "exampleConfig"); // Initialize the config class by new an instance of it
		 *
		 * LOGGER.info("Try loading config..."); // You can load the config as soon as you initialized the config, if the file or some of the values are missing, they will be written in default values
		 * exampleConfig.load(); // Remember to use pre-defined load() method instead of read() to load the config
		 *
		 *
		 *
		 * LOGGER.info("Try writing config..."); // You can write the config whenever you want, it will override the old values in the config file
		 * exampleConfig.write(); // Remember to use pre-defined write() method instead of save() to load the config
		 *
		 */
	}

	// TODO: Request manipulates the config file (write, load, etc.)
	// TODO: Config is a collection of options (save, read, etc.)
	// TODO: Option classes, for example OptionBoolean, is a single config option class (extends Option)
	// TODO: PlumeConfig handles the above

	// TODO: PlumeConfig -> {Request -> Handling Config Files, Config -> Handling Options}
}
