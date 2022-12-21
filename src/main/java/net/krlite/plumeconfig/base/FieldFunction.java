package net.krlite.plumeconfig.base;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.ISavingFunction;
import net.krlite.plumeconfig.api.IFunction;

import java.util.Arrays;
import java.util.Optional;

public class FieldFunction {
	public static Object functions(String id, Class<?> type, Object o) {
		// Build-in function
		Optional<IFunction> function = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_FUNCTION, IFunction.class).stream()
									   .filter(container -> container.getProvider().getMetadata().getId().equals(PlumeConfigMod.MOD_ID))
									   .map(EntrypointContainer::getEntrypoint)
									   .filter(f -> f.getClass().isAnnotationPresent(Convertor.class))
									   .filter(f -> Arrays.asList(f.getClass().getAnnotation(Convertor.class).to()).contains(type))
									   .findFirst();

		// Custom function
		Optional<IFunction> customFunction = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_FUNCTION, IFunction.class).stream()
													 .filter(container -> container.getProvider().getMetadata().getId().equals(id))
													 .map(EntrypointContainer::getEntrypoint)
													 .filter(f -> f.getClass().isAnnotationPresent(Convertor.class))
													 .filter(f -> Arrays.asList(f.getClass().getAnnotation(Convertor.class).to()).contains(type))
													 .findFirst();

		if (customFunction.isPresent()) {
			return customFunction.get().apply(o);
		} else if (function.isPresent()) {
			return function.get().apply(o);
		} else {
			return o;
		}
	}

	public static Object savingFunctions(String id, Object o) {
		// Build-in function
		Optional<ISavingFunction> savingFunction = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_SAVING_FUNCTION, ISavingFunction.class).stream()
											 .filter(container -> container.getProvider().getMetadata().getId().equals(PlumeConfigMod.MOD_ID))
											 .map(EntrypointContainer::getEntrypoint)
											 .filter(f -> f.getClass().isAnnotationPresent(Convertor.class))
											 .filter(f -> Arrays.asList(f.getClass().getAnnotation(Convertor.class).from()).contains(o.getClass()))
											 .findFirst();

		// Custom function
		Optional<ISavingFunction> customSavingFunction = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_SAVING_FUNCTION, ISavingFunction.class).stream()
																 .filter(container -> container.getProvider().getMetadata().getId().equals(id))
																 .map(EntrypointContainer::getEntrypoint)
																 .filter(f -> f.getClass().isAnnotationPresent(Convertor.class))
																 .filter(f -> Arrays.asList(f.getClass().getAnnotation(Convertor.class).from()).contains(o.getClass()))
																 .findFirst();

		if (customSavingFunction.isPresent()) {
			return customSavingFunction.get().apply(o);
		} else if (savingFunction.isPresent()) {
			return savingFunction.get().apply(o);
		} else {
			return o;
		}
	}
}
