package net.krlite.plumeconfig.config.api;

import net.krlite.plumeconfig.option.core.Option;

public interface PlumeConfigApi {
	/**
	 * Called when the config class should be initialized. <br />
	 * It's recommended to first call {@link net.krlite.plumeconfig.config.PlumeConfig#read() read} and save the configs returned by the <code>JsonObject</code>, then call {@link net.krlite.plumeconfig.config.PlumeConfig#write(Option[]) write} to fully initialize the config file in this method.
	 */
	void onInitialize();
}
