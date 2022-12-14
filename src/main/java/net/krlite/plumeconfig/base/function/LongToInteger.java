package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;

import java.util.function.Function;

@Convertor(from = Long.class, to = {Integer.class, int.class})
public class LongToInteger implements Function<Long, Integer> {
	@Override
	public Integer apply(Long value) {
		return value.intValue();
	}
}
