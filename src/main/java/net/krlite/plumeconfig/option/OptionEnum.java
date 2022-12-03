package net.krlite.plumeconfig.option;

import com.google.gson.JsonObject;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.option.core.Option;

import java.util.Arrays;

public class OptionEnum extends Option<Enum<?>> {
	public OptionEnum(String key, Enum<?> defaultValue) {
		this(null, key, defaultValue, null);
	}

	public OptionEnum(String name, String key, Enum<?> defaultValue) {
		this(name, key, defaultValue, null);
	}

	public OptionEnum(String key, Enum<?> defaultValue, String comment) {
		this(null, key, defaultValue, comment);
	}

	public OptionEnum(String name, String key, Enum<?> defaultValue, String comment) {
		super(name, key, defaultValue, comment);
	}

	@Override
	public Enum<?> parse(String source) {
		try {
			return value = (source == null ? defaultValue : Enum.valueOf(defaultValue.getDeclaringClass(), source));
		} catch (IllegalArgumentException e) {
			PlumeConfigMod.LOGGER.error(
					"Invalid value for enum option: " + key + " = " + source
							+ " in class: " + defaultValue.getDeclaringClass().getName()
							+ "! Valid values are: " + Arrays.toString(defaultValue.getDeclaringClass().getEnumConstants())
							+ ". Using default value: " + defaultValue + "."
			);
			PlumeConfigMod.LOGGER.trace(e.getMessage());
			return value = defaultValue;
		}
	}

	@Override
	public Enum<?> parse(JsonObject source) {
		return source.get(key) == null ? value = defaultValue : parse(source.get(key).getAsString());
	}

	@Override
	public String getValueRaw() {
		return value.name();
	}

	@Override
	public String getDefaultValueRaw() {
		return defaultValue.name();
	}
}
