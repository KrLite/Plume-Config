package net.krlite.plumeconfig.config;

import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.option.core.Option;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

public record PlumeConfig(Request request) {
	public void prepare(@NotNull ConfigBuilder configBuilder) {
		if (!request.formattedConfig.isEmpty()) {
			request.clear();
		}

		if (configBuilder.categories.containsKey(ConfigBuilder.ROOT_CATEGORY)) {
			request.appendCategory(configBuilder.categories.get(ConfigBuilder.ROOT_CATEGORY));
		}

		for (Option<?> option : configBuilder.options.values()) {
			request.appendOption(option);

			if (configBuilder.categories.containsKey(option.getKey())) {
				request.appendCategory(configBuilder.categories.get(option.getKey()));
			}
		}
	}

	private void create() {
		try {
			request.create();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public HashMap<String, String> read() {
		try {
			request.load();

			return request.config;
		} catch (IOException e) {
			create();

			return read();
		}
	}

	public void write() {
		try {
			request.write();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * @return The ConfigSerializer for this config.
	 */
	//public abstract ConfigSerializer<? extends PlumeConfig> getSerializer();
}
