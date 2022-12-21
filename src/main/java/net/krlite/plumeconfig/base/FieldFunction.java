package net.krlite.plumeconfig.base;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.annotation.Convertor;
import net.krlite.plumeconfig.api.SavingFunction;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public class FieldFunction {
	public static Object functions(String id, Class<?> type, Object o) {
		// Build-in function
		Optional<Function> function = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_FUNCTION, Function.class).stream()
									   .filter(container -> container.getProvider().getMetadata().getId().equals(PlumeConfigMod.MOD_ID))
									   .map(EntrypointContainer::getEntrypoint)
									   .filter(f -> f.getClass().isAnnotationPresent(Convertor.class))
									   .filter(f -> Arrays.asList(f.getClass().getAnnotation(Convertor.class).to()).contains(type))
									   .findFirst();

		// Custom function
		Optional<Function> customFunction = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_FUNCTION, Function.class).stream()
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
		Optional<SavingFunction> savingFunction = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_SAVING_FUNCTION, SavingFunction.class).stream()
											 .filter(container -> container.getProvider().getMetadata().getId().equals(PlumeConfigMod.MOD_ID))
											 .map(EntrypointContainer::getEntrypoint)
											 .filter(f -> f.getClass().isAnnotationPresent(Convertor.class))
											 .filter(f -> Arrays.asList(f.getClass().getAnnotation(Convertor.class).from()).contains(o.getClass()))
											 .findFirst();

		// Custom function
		Optional<SavingFunction> customSavingFunction = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_SAVING_FUNCTION, SavingFunction.class).stream()
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
