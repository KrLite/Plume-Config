package net.krlite.plumeconfig.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.krlite.plumeconfig.option.core.Option;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ConfigSerializer{
	public JsonObject serialize(FileReader reader) throws IOException {
		if (reader.ready()) {
			return JsonParser.parseReader(reader).getAsJsonObject();
		} else {
			return new JsonObject();
		}
	}

	public JsonObject deSerialize(Option<?>... options) {
		final JsonObject json = new JsonObject();
		Arrays.stream(options).forEach(value -> json.addProperty(value.getKey(), value.getValueRaw()));
		return json;
	}
}
