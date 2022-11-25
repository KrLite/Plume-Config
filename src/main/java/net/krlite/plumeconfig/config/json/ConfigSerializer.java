package net.krlite.plumeconfig.config.json;

import com.google.gson.JsonObject;
import net.krlite.plumeconfig.config.Config;

public interface ConfigSerializer<T extends Config> {
	T serialize(JsonObject json);
	JsonObject deSerialize(T config);
}
