package net.krlite.plumeconfig.option;

import com.google.gson.JsonObject;
import net.krlite.plumeconfig.option.core.Option;

public class OptionString extends Option<String> {
	public OptionString(String key, String defaultValue) {
		this(null, key, defaultValue, null);
	}

	public OptionString(String name, String key, String defaultValue, String comment) {
		super(name, key, defaultValue, comment);
	}

	@Override
	public String parse(String source) {
		return value = (source == null ? defaultValue : source);
	}

	@Override
	public String parse(JsonObject source) {
		return value = (source.get(key) == null ? defaultValue : source.get(key).getAsString());
	}
}
