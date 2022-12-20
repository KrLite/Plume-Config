package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;

@Convertor(from = Double.class, to = {Float.class, float.class})
public class DoubleToFloat implements java.util.function.Function<Double, Float> {
	@Override
	public Float apply(Double aDouble) {
		return aDouble.floatValue();
	}
}
