package net.krlite.plumeconfig.option;

import net.krlite.plumeconfig.option.core.Option;

public class OptionBoolean extends Option<Boolean> {
	public OptionBoolean(String key, boolean defaultValue) {
		this(null, key, defaultValue, null);
	}

	public OptionBoolean(String name, String key, boolean defaultValue) {
		this(name, key, defaultValue, null);
	}

	public OptionBoolean(String key, boolean defaultValue, String comment) {
		this(null, key, defaultValue, comment);
	}

	public OptionBoolean(String name, String key, boolean defaultValue, String comment) {
		super(name, key, defaultValue, comment);
	}

	@Override
	public Boolean parse(String source) {
		return source == null ? defaultValue : (value = source.toLowerCase().matches("true|yes|on|1") || (!source.toLowerCase().matches("false|no|off|0") && defaultValue));
	}
}
