package example;

import net.fabricmc.api.ModInitializer;
import net.krlite.plumeconfig.base.ConfigFile;

import java.io.File;

public class ExampleModInitializer implements ModInitializer {
	@Override
	public void onInitialize() {
		ConfigFile config = new ConfigFile("example", new File("/config/example.toml"));
		Example example = config.load(Example.class);
		config.save(example);
	}
}
