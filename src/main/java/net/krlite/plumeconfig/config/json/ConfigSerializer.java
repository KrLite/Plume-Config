package net.krlite.plumeconfig.config.json;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.krlite.plumeconfig.config.PlumeConfig;

public interface ConfigSerializer<T extends PlumeConfig> {
	T serialize(JsonObject json);
	JsonObject deSerialize(T config);
}
