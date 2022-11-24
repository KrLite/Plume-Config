package net.krlite.plumeconfig.option;

import net.krlite.plumeconfig.Formatter;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.option.core.Option;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

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
			return value = Enum.valueOf(defaultValue.getDeclaringClass(), source);
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
	public String format() {
		return Formatter.formatOption(name, key, value.name(), defaultValue.name(), comment);
	}
}
