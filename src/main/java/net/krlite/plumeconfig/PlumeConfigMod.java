package net.krlite.plumeconfig;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.krlite.plumeconfig.config.api.PlumeConfigApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlumeConfigMod implements ModInitializer {
	public static final String MOD_ID = "plumeconfig";
	public static final Logger LOGGER = LoggerFactory.getLogger("Plume Config");
	public static long modCount = 0;

	@Override
	public void onInitialize() {
		FabricLoader.getInstance().getEntrypointContainers(MOD_ID, PlumeConfigApi.class).forEach(entrypoint -> {
			PlumeConfigApi api = entrypoint.getEntrypoint();
			try {
				api.onInitialize();
				modCount++;
			} catch (Throwable e) {
				LOGGER.error("Failed initializing Plume Config for mod: " + entrypoint.getProvider().getMetadata().getId(), e);
			}
		});
		if (modCount > 0) {
			LOGGER.info("< Plume 🪶 Config > has successfully initialized configs for " + modCount + " mod" + (modCount > 1 ? "s" : "") + "!");
		}
	}
}
