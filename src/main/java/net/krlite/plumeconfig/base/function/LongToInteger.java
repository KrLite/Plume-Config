package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.IFunction;

@Convertor(from = Long.class, to = {Integer.class, int.class})
public class LongToInteger implements IFunction {
	@Override
	public Object apply(Object object) {
		return ((Long) object).intValue();
	}
}
