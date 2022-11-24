package net.krlite.plumeconfig.option;

import net.krlite.plumeconfig.Formatter;
import net.krlite.plumeconfig.option.core.Option;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

public class OptionString extends Option<String> {
	public OptionString(String key, String defaultValue) {
		this(null, key, defaultValue, null);
	}

	public OptionString(String name, String key, String defaultValue, String comment) {
		super(name, key, defaultValue, comment);
	}

	@Override
	public String parse(String source) {
		return value = source;
	}
}
