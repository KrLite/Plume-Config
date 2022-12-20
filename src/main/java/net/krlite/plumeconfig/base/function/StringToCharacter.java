package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.IFunction;

@Convertor(from = String.class, to = {Character.class, char.class})
public class StringToCharacter implements IFunction {
	@Override
	public Object apply(Object object) {
		return ((String) object).charAt(0);
	}
}
