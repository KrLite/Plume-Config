package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;

@Convertor(from = Long.class, to = {Short.class, short.class})
public class LongToShort implements java.util.function.Function<Long, Short> {
	@Override
	public Short apply(Long aLong) {
		return aLong.shortValue();
	}
}
