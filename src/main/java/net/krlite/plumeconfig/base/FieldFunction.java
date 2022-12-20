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
		Optional<IFunction> function = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_FUNCTION, IFunction.class).stream()
									   .filter(container -> container.getProvider().getMetadata().getId().equals(id) || container.getProvider().getMetadata().getId().equals("plumeconfig"))
									   .map(EntrypointContainer::getEntrypoint)
									   .filter(f -> f.getClass().isAnnotationPresent(Convertor.class))
									   .filter(f -> Arrays.asList(f.getClass().getAnnotation(Convertor.class).to()).contains(type))
									   .findFirst();

		if (function.isPresent()) {
			return function.get().apply(o);
		} else {
			return o;
		}
	}

	public static Object savingFunctions(String id, Object o) {
		Optional<ISavingFunction> savingFunction = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_SAVING_FUNCTION, ISavingFunction.class).stream()
											 .filter(container -> container.getProvider().getMetadata().getId().equals(id) || container.getProvider().getMetadata().getId().equals("plumeconfig"))
											 .map(EntrypointContainer::getEntrypoint)
											 .filter(f -> f.getClass().isAnnotationPresent(Convertor.class))
											 .filter(f -> Arrays.asList(f.getClass().getAnnotation(Convertor.class).from()).contains(o.getClass()))
											 .findFirst();

		if (savingFunction.isPresent()) {
			return savingFunction.get().apply(o);
		} else {
			return o;
		}
	}
}
