package net.krlite.plumeconfig.base;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class Config<T> {
	@NotNull
	public final Class<T> clazz;
	@Nullable
	public T instance;

	public Config(@NotNull Class<T> clazz) {
		this.clazz = clazz;
	}

	public Config(@NotNull Class<T> clazz, @Nullable T instance) {
		this.clazz = clazz;
		this.instance = instance;
	}

	public boolean hasInstance() {
		return instance != null;
	}

	public T accept(@Nullable T instance) {
		return this.instance = instance;
	}
}
