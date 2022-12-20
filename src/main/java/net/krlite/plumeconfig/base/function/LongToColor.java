package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.IFunction;

import java.awt.*;

@Convertor(from = Long.class, to = Color.class)
public class LongToColor implements IFunction {
	@Override
	public Object apply(Object object) {
		return new Color(((Long) object).intValue(), true);
	}
}
