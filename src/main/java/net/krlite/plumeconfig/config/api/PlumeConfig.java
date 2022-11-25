package net.krlite.plumeconfig.config.api;

import net.fabricmc.loader.api.FabricLoader;
import net.krlite.plumeconfig.config.Config;
import net.krlite.plumeconfig.config.Request;

import java.nio.file.Path;

public abstract class PlumeConfig extends PlumeConfigApi{
	public PlumeConfig(String fileName) {
		request = new Request(FabricLoader.getInstance().getConfigDir(), fileName);
		config = new Config(request);
	}

	public PlumeConfig(String path, String fileName) {
		request = new Request(Path.of(FabricLoader.getInstance().getConfigDir().toString(), "/", path), fileName);
		config = new Config(request);
	}

	private final Config config;
	private final Request request;

	Config config() {
		return this.config;
	}
}
