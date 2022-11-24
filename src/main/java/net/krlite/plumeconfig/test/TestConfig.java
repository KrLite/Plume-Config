package net.krlite.plumeconfig.test;

import net.krlite.plumeconfig.config.ConfigBuilder;
import net.krlite.plumeconfig.config.api.PlumeConfigApi;
import net.krlite.plumeconfig.option.*;

import java.awt.*;
import java.util.HashMap;

public class TestConfig extends PlumeConfigApi {
	public TestConfig() {
		super("plume", "test");
	}

	public OptionBoolean testBoolean = new OptionBoolean("option.test.boolean", false);
	public OptionDouble testDouble = new OptionDouble("option.test.double", 0.0);
	public OptionLong testInt = new OptionLong("option.test.int", 0);
	public OptionString testString = new OptionString("option.test.string", "");
	public OptionColor testColor = new OptionColor("option.test.color", Color.WHITE);

	@Override
	public void read(HashMap<String, String> config) {
		testBoolean.parse(config.get(testBoolean.getKey()));
		testDouble.parse(config.get(testDouble.getKey()));
		testInt.parse(config.get(testInt.getKey()));
		testString.parse(config.get(testString.getKey()));
		testColor.parse(config.get(testColor.getKey()));
	}

	@Override
	public void save(ConfigBuilder configBuilder) {
		configBuilder.accept(testBoolean);
		configBuilder.accept(testDouble);
		configBuilder.accept(testInt);
		configBuilder.accept(testString);
		configBuilder.accept(testColor);
	}
}
