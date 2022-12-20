package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.IFunction;

@Convertor(from = Long.class, to = {Short.class, short.class})
public class LongToShort implements IFunction {
	@Override
	public Object apply(Object object) {
		return ((Long) object).shortValue();
	}
}
