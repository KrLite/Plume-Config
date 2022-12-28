package example;

import net.fabricmc.api.ModInitializer;
import net.krlite.plumeconfig.base.ConfigFile;

public class ExampleModInitializer implements ModInitializer {
	@Override
	public void onInitialize() {
		ConfigFile<Example> config = new ConfigFile<>(Example.class, "example");
		Example example = config.operate();
	}
}
