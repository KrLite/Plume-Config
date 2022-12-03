package net.krlite.plumeconfig.option;

import com.google.gson.JsonObject;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.option.core.Option;
import net.minecraft.util.math.MathHelper;

public class OptionDouble extends Option<Double> {
	public OptionDouble(String key, double defaultValue) {
		this(null, key, defaultValue, null);
	}

	public OptionDouble(String name, String key, double defaultValue) {
		this(name, key, defaultValue, null);
	}

	public OptionDouble(String key, double defaultValue, String comment) {
		this(null, key, defaultValue, comment);
	}

	public OptionDouble(String name, String key, double defaultValue, String comment) {
		super(name, key, defaultValue, comment);
	}

	public double getRelativeValue(double relative) {
		if ( relative == 0 ) {
			return value;
		}
		return value / relative;
	}

	public int getRelativeValueSlider(double relative) {
		return (int) Math.round(getRelativeValue(relative) * 100);
	}

	public int getPercentageSlider() {
		if ( value < 0 || value > 1 ) {
			PlumeConfigMod.LOGGER.warn("Required percentage slider value outside of range 0 to 1: " + value + ", clamping to range.");
		}
		return MathHelper.clamp((int) Math.round(value * 100), 0, 100);
	}

	@Override
	public Double parse(String source) {
		try {
			return value = (source == null ? defaultValue : Double.parseDouble(source));
		} catch (NumberFormatException e) {
			PlumeConfigMod.LOGGER.trace(e.getMessage());
			return value = defaultValue;
		}
	}

	@Override
	public Double parse(JsonObject source) {
		try {
			return value = (source.get(key) == null ? defaultValue : source.get(key).getAsDouble());
		} catch (NumberFormatException e) {
			PlumeConfigMod.LOGGER.trace(e.getMessage());
			return value = defaultValue;
		}
	}
}
