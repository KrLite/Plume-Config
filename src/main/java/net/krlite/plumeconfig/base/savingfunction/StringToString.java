package net.krlite.plumeconfig.base.savingfunction;

import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.SavingFunction;

@Convertor(from = String.class, to = String.class)
public class StringToString implements SavingFunction<String> {
	@Override
	public String apply(String s) {
		return "\"" + s + "\"";
	}
}
