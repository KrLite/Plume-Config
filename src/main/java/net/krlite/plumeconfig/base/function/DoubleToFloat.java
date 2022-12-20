package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.IFunction;

@Convertor(from = Double.class, to = {Float.class, float.class})
public class DoubleToFloat implements IFunction {
	@Override
	public Object apply(Object object) {
		return ((Double) object).floatValue();
	}
}
