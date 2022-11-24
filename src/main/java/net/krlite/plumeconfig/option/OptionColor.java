package net.krlite.plumeconfig.option;

import net.krlite.plumeconfig.Formatter;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.option.core.Option;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class OptionColor extends Option<Color> {
	public OptionColor(String key, Color defaultValue) {
		this(null, key, defaultValue, null);
	}

	public OptionColor(String name, String key, Color defaultValue) {
		this(name, key, defaultValue, null);
	}

	public OptionColor(String key, Color defaultValue, String comment) {
		this(null, key, defaultValue, comment);
	}

	public OptionColor(String name, String key, Color defaultColor, String comment) {
		super(name, key, defaultColor, comment);
	}

	@Override
	public Color parse(String sourceColor) {
		try {
			return value = new Color((int) Long.parseLong(sourceColor.toLowerCase(), 16));
		} catch (IllegalArgumentException e) {
			PlumeConfigMod.LOGGER.error(
					"Color parameter outside of expected range: " + key + " = " + sourceColor
							+ "! Using default value: " + Integer.toHexString(defaultValue.getRGB()) + "."
			);
			PlumeConfigMod.LOGGER.trace(e.getMessage());
			return value = defaultValue;
		}
	}

	@Override
	public String format() {
		return Formatter.formatOption(
				name, key,
				Integer.toHexString(value.getRGB()).toUpperCase(),
				Integer.toHexString(defaultValue.getRGB()).toUpperCase(),
				comment
		);
	}
}
