package net.krlite.plumeconfig.example;

import com.google.gson.JsonObject;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.config.api.PlumeConfigApi;
import net.krlite.plumeconfig.config.PlumeConfig;
import net.krlite.plumeconfig.option.*;
import net.krlite.plumeconfig.option.core.ILocalizable;

import java.awt.*;

/**
 * Excluded from the mod jar file, just for an example.
 */
public class Example implements PlumeConfigApi {
	public enum ExampleEnum implements ILocalizable {
		EXAMPLE_ENUM_1("1"),
		EXAMPLE_ENUM_2("2"),
		EXAMPLE_ENUM_3("3");

		private final String name;

		ExampleEnum(String name) {
			this.name = name;
		}

		@Override
		public String getLocalizedName() {
			return name;
		}
	}

	public static final PlumeConfig config = new PlumeConfig(PlumeConfigMod.MOD_ID, "example");

	public static final OptionBoolean ob = new OptionBoolean("ob", "ob", true);
	public static final OptionLong ol = new OptionLong("ol", "ol", 0L);
	public static final OptionString os = new OptionString("os", "os", "none", null);
	public static final OptionEnumLocalized<ExampleEnum> oe = new OptionEnumLocalized<>("oe", "oe", ExampleEnum.EXAMPLE_ENUM_1);
	public static final OptionColor oc = new OptionColor("oc", "oc", new Color(0xFF000000));

	public static void readConfig(JsonObject configs) {
		ob.parse(configs);
		ol.parse(configs);
		os.parse(configs);
		oe.parse(configs);
		oc.parse(configs);
	}

	public static void writeConfig() {
		config.write(
				ol,
				ob,
				os,
				oe,
				oc
		);
	}

	@Override
	public void onInitialize() {
		readConfig(config.read());
		writeConfig();
	}
}
