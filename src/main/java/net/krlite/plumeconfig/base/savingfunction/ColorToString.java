package net.krlite.plumeconfig.base.savingfunction;

import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.SavingFunction;

import java.awt.*;

@Convertor(from = Color.class, to = String.class)
public class ColorToString implements SavingFunction<Color> {
	@Override
	public String apply(Color value) {
		return "0x" + Integer.toHexString(value.getRGB()).toUpperCase();
	}
}
