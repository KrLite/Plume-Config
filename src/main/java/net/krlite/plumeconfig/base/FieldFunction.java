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
		Optional<Function> f = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_FUNCTION, Function.class).stream()
									   .filter(container -> container.getProvider().getMetadata().getId().equals(id) || container.getProvider().getMetadata().getId().equals("plumeconfig"))
									   .map(EntrypointContainer::getEntrypoint)
									   .filter(function -> function.getClass().isAnnotationPresent(Convertor.class))
									   .filter(function -> Arrays.asList(function.getClass().getAnnotation(Convertor.class).to()).contains(type))
									   .findFirst();

		if (f.isPresent()) {
			return f.get().apply(o);
		} else {
			return o;
		}
	}

	public static Object savingFunctions(String id, Object o) {
		Optional<SavingFunction> s = FabricLoader.getInstance().getEntrypointContainers(PlumeConfigMod.ENTRYPOINT_SAVING_FUNCTION, SavingFunction.class).stream()
											 .filter(container -> container.getProvider().getMetadata().getId().equals(id) || container.getProvider().getMetadata().getId().equals("plumeconfig"))
											 .map(EntrypointContainer::getEntrypoint)
											 .filter(function -> function.getClass().isAnnotationPresent(Convertor.class))
											 .filter(function -> Arrays.asList(function.getClass().getAnnotation(Convertor.class).from()).contains(o.getClass()))
											 .findFirst();

		if (s.isPresent()) {
			return s.get().apply(o);
		} else {
			return o;
		}
	}
}
