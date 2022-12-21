package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;

import java.util.function.Function;

@Convertor(from = Double.class, to = {Float.class, float.class})
public class DoubleToFloat implements Function<Double, Float> {
	@Override
	public Float apply(Double value) {
		return value.floatValue();
	}
}
