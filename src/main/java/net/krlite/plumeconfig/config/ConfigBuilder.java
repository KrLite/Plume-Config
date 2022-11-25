package net.krlite.plumeconfig.config;

import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.option.core.Option;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class ConfigBuilder {
	public static final String ROOT_CATEGORY = "__root__";
	public final HashMap<String, String> categories = new HashMap<>();
	public final HashMap<String, Option<?>> options = new HashMap<>();
	private String lastOptionKey = ROOT_CATEGORY;

	public void accept(String category) {
		categories.put(lastOptionKey, category);
	}

	public <T extends Option<?>> void accept(@NotNull T option) {
		options.put(option.getKey(), option);
		lastOptionKey = option.getKey();
	}
}
