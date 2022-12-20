package net.krlite.plumeconfig.base.savingfunction;

import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.ISavingFunction;

@Convertor(from = String.class, to = String.class)
public class StringToString implements ISavingFunction {
	@Override
	public String apply(Object object) {
		String string = (String) object;
		if (!string.contains("\n")) {
			return "\"" + string + "\"";
		} else {
			if (string.contains("\"\"\"")) {
				string = string.replaceAll("\"\"\"+", "\"\"");
				PlumeConfigMod.LOGGER.error("Multi-line string containing triple quotes or more (\"\"\"+), replacing to double quotes(\"\").");
			}
			return "\"\"\"\n" + string + "\"\"\"";
		}
	}
}
