package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;

import java.util.function.Function;

@Convertor(from = String.class, to = {Character.class, char.class})
public class StringToCharacter implements Function<String, Character> {
	@Override
	public Character apply(String s) {
		return s.charAt(0);
	}
}
