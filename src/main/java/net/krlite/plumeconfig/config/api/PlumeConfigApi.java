package net.krlite.plumeconfig.config.api;

import net.krlite.plumeconfig.config.Config;
import net.krlite.plumeconfig.config.ConfigBuilder;

import java.util.HashMap;

abstract class PlumeConfigApi {
	abstract Config config();

	public abstract void read(HashMap<String, String> config);

	public abstract void save(ConfigBuilder configBuilder);

	public void load() {
		prepare();
		this.read(this.config().read());
		write();
	}

	public void write() {
		prepare();
		this.config().write();
	}

	private void prepare() {
		ConfigBuilder configBuilder = new ConfigBuilder();
		this.save(configBuilder);
		this.config().prepare(configBuilder);
	}
}
