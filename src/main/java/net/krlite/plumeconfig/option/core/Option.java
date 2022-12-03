package net.krlite.plumeconfig.option.core;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class Option<T> {
	@Nullable
	protected final String name;
	protected final String key;
	protected T value;
	protected final T defaultValue;
	@Nullable
	protected final String comment;

	protected Option(@Nullable String name, @NotNull String key, @NotNull T defaultValue, @Nullable String comment) {
		this.name = name;
		this.key = key;
		this.value = this.defaultValue = defaultValue;
		this.comment = comment;
	}

	public String getKey() {
		return key;
	}

	public T getValue() {
		return value;
	}

	public T resetValue() {
		return value = defaultValue;
	}

	public void set(T value) {
		this.value = value;
	}

	public abstract T parse(String source);

	public abstract T parse(JsonObject source);

	public String getValueRaw() {
		return value.toString();
	}

	public String getDefaultValueRaw() {
		return defaultValue.toString();
	}
}
