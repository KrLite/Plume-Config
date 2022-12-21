package net.krlite.plumeconfig.api;

import java.util.function.Function;

public interface SavingFunction<U> extends Function<U, String> {
	@Override
	String apply(U u);
}
