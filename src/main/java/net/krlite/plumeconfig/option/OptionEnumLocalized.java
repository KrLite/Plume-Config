package net.krlite.plumeconfig.option;

import com.google.gson.JsonObject;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.option.core.EnumParser;
import net.krlite.plumeconfig.option.core.ILocalizable;
import net.krlite.plumeconfig.option.core.Option;

import java.util.Arrays;

public class OptionEnumLocalized<E extends Enum<E> & ILocalizable> extends Option<E> {
	public OptionEnumLocalized(String key, E defaultValue) {
		this(null, key, defaultValue, null);
	}

	public OptionEnumLocalized(String name, String key, E defaultValue) {
		this(name, key, defaultValue, null);
	}

	public OptionEnumLocalized(String key, E defaultValue, String comment) {
		this(null, key, defaultValue, comment);
	}

	public OptionEnumLocalized(String name, String key, E defaultValue, String comment) {
		super(name, key, defaultValue, comment);
	}

	@Override
	public E parse(String source) {
		try {
			return value = (source == null ? defaultValue : EnumParser.parseValue(source, defaultValue.getDeclaringClass()));
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
	public E parse(JsonObject source) {
		return source.get(key) == null ? value = defaultValue : parse(source.get(key).getAsString());
	}

	@Override
	public String getValueRaw() {
		return value.getLocalizedName();
	}

	@Override
	public String getDefaultValueRaw() {
		return defaultValue.getLocalizedName();
	}
}
