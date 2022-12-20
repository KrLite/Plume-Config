package net.krlite.plumeconfig.base.function;

import net.krlite.plumeconfig.annotation.Convertor;

import java.awt.*;
import java.util.function.Function;

@Convertor(from = Long.class, to = Color.class)
public class LongToColor implements Function<Long, Color> {
	@Override
	public Color apply(Long aLong) {
		return new Color(aLong.intValue(), true);
	}
}
