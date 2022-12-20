package net.krlite.plumeconfig;

import example.Example;
import net.fabricmc.api.ModInitializer;
import net.krlite.plumeconfig.base.ConfigFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlumeConfigMod implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("plumeconfig");
	public static final String MOD_ID = "plumeconfig";
	public static final String ENTRYPOINT_FUNCTION = MOD_ID + "-function";
	public static final String ENTRYPOINT_SAVING_FUNCTION = MOD_ID + "-savingfunction";

	@Override
	public void onInitialize() {
	}
}
