package net.krlite.plumeconfig.api;

import java.util.function.Function;

public interface SavingFunction<T> extends Function<T, String> {
	@Override
	String apply(T t);
}
