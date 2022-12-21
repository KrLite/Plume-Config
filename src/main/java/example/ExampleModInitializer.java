package example;

import net.fabricmc.api.ModInitializer;
import net.krlite.plumeconfig.base.ConfigFile;

import java.io.File;

public class ExampleModInitializer implements ModInitializer {
	@Override
	public void onInitialize() {
		ConfigFile config = new ConfigFile("example");
		Example example = config.loadAndSave(Example.class);
	}
}
