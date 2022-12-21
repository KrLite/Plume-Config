package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;

import java.util.function.Function;

@Convertor(from = Long.class, to = {Short.class, short.class})
public class LongToShort implements Function<Long, Short> {
	@Override
	public Short apply(Long value) {
		return value.shortValue();
	}
}
