package net.krlite.plumeconfig;

import net.fabricmc.api.ModInitializer;
import net.krlite.plumeconfig.test.TestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class PlumeConfigMod implements ModInitializer {
	public static final String MOD_ID = "plumeconfig";
	public static final Logger LOGGER = LoggerFactory.getLogger("Plume Config");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing config...");
		TestConfig testConfig = new TestConfig();
		PlumeConfigMod.LOGGER.error(String.valueOf(testConfig.testColor.getValue().getRGB()));
		LOGGER.info("Try loading config...");
		testConfig.load();
	}

	// TODO: Request manipulates the config file (write, load, etc.)
	// TODO: Config is a collection of options (save, read, etc.)
	// TODO: Option classes, for example OptionBoolean, is a single config option class (extends Option)
	// TODO: PlumeConfig handles the above

	// TODO: PlumeConfig -> {Request -> Handling Config Files, Config -> Handling Options}
}
