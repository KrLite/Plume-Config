package net.krlite.plumeconfig.api;

import java.util.function.Function;

public interface ISavingFunction extends Function<Object, String> {
	@Override
	String apply(Object object);
}
