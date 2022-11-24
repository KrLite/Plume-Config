package net.krlite.plumeconfig.config.api;

import net.fabricmc.loader.api.FabricLoader;
import net.krlite.plumeconfig.config.ConfigBuilder;
import net.krlite.plumeconfig.config.PlumeConfig;
import net.krlite.plumeconfig.config.Request;

import java.nio.file.Path;
import java.util.HashMap;

public abstract class PlumeConfigApi {
	private final PlumeConfig plumeConfig;
	private final Request request;

	public PlumeConfigApi(String fileName) {
		request = new Request(FabricLoader.getInstance().getConfigDir(), fileName);
		plumeConfig = new PlumeConfig(request);
	}

	public PlumeConfigApi(String path, String fileName) {
		request = new Request(Path.of(FabricLoader.getInstance().getConfigDir().toString(), "/", path), fileName);
		plumeConfig = new PlumeConfig(request);
	}

	public abstract void read(HashMap<String, String> config);

	public abstract void save(ConfigBuilder configBuilder);

	public void load() {
		this.read(this.plumeConfig.read());
	}

	public void write() {
		ConfigBuilder configBuilder = new ConfigBuilder();
		this.save(configBuilder);
		this.plumeConfig.prepare(configBuilder);
		this.plumeConfig.write();
	}
}
