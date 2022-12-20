package net.krlite.plumeconfig.base.savingfunction;

import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.ISavingFunction;

import java.awt.*;

@Convertor(from = Color.class, to = String.class)
public class ColorToString implements ISavingFunction {
	@Override
	public String apply(Object object) {
		return "0x" + Integer.toHexString(((Color) object).getRGB());
	}
}
