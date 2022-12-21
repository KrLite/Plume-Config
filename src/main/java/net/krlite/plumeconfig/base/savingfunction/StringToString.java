package net.krlite.plumeconfig.base.savingfunction;

import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.SavingFunction;

@Convertor(from = String.class, to = String.class)
public class StringToString implements SavingFunction<String> {
	@Override
	public String apply(String value) {
		if (!value.contains("\n")) {
			return "\"" + value + "\"";
		} else {
			if (value.contains("\"\"\"")) {
				value = value.replaceAll("\"\"\"+", "\"\"");
				PlumeConfigMod.LOGGER.error("Multi-line string containing triple quotes or more (\"\"\"+), replacing to double quotes(\"\").");
			}
			return "\"\"\"\n" + value + "\"\"\"";
		}
	}
}
