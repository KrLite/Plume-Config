package net.krlite.plumeconfig.option;

import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.option.core.Option;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

public class OptionLong extends Option<Long> {
	public OptionLong(String key, long defaultValue) {
		this(null, key, defaultValue, null);
	}

	public OptionLong(String name, String key, long defaultValue) {
		this(name, key, defaultValue, null);
	}

	public OptionLong(String key, long defaultValue, String comment) {
		this(null, key, defaultValue, comment);
	}

	public OptionLong(String name, String key, long defaultValue, String comment) {
		super(name, key, defaultValue, comment);
	}

	@Override
	public Long parse(String source) {
		try {
			return value = Long.parseLong(source);
		} catch (NumberFormatException e) {
			PlumeConfigMod.LOGGER.trace(e.getMessage());
			return value = defaultValue;
		}
	}
}
