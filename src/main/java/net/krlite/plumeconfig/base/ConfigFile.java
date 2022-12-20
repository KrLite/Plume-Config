package net.krlite.plumeconfig.base;

import net.fabricmc.loader.api.FabricLoader;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.annotation.Comment;
import net.krlite.plumeconfig.annotation.Option;
import net.krlite.plumeconfig.exception.ClassException;
import net.krlite.plumeconfig.exception.FieldException;
import net.krlite.plumeconfig.exception.FileException;
import net.krlite.plumeconfig.io.Reader;
import net.krlite.plumeconfig.io.Writer;
import org.jetbrains.annotations.Nullable;
import org.tomlj.TomlParseResult;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

public class ConfigFile {
	private final String modid;
	private final File file;

	/**
	 * Creates a new config file.
	 * @param modid	The modid of the mod that this config file belongs to.
	 * @param file	The file with absolute path, <strong>which file name must be <code>.toml</code> suffixed.</strong>
	 */
	public ConfigFile(String modid, File file) {
		this.modid = modid;
		this.file = file;
	}

	/**
	 * Creates a new config file named <code>&lt;modid&gt;.toml</code> under the game config directory.
	 * @param modid	The modid of the mod that this config file belongs to.
	 */
	public ConfigFile(String modid) {
		this(modid, FabricLoader.getInstance().getConfigDir().resolve(modid + ".toml").toFile());
	}

	/**
	 * Creates a new config file under a subdirectory named <code>&lt;modid&gt;</code> under the game config directory.
	 * @param modid		The modid of the mod that this config file belongs to.
	 * @param fileName	The name of the config file, <strong>which will be automatically suffixed.</strong>
	 */
	public ConfigFile(String modid, String fileName) {
		this(modid, FabricLoader.getInstance().getConfigDir()
							.resolve(modid)
							.resolve(fileName.replaceAll(".toml$" /* Replace .toml suffix in case for duplication */, "") + ".toml")
							.toFile()
		);
	}

	public <T> T loadAndSave(Class<T> clazz) {
		T instance = load(clazz);
		save(instance);
		return instance;
	}

	public <T> void save(T instance) {
		Writer writer = new Writer(file);
		try {
			writer.initialize();
		} catch (IOException ioException) {
			FileException.traceFileWritingException(PlumeConfigMod.LOGGER, ioException, file);
		}
		Field[] fields = instance.getClass().getDeclaredFields();
		Arrays.stream(fields).forEach(this::iteratorFieldSave);
	}

	@Nullable
	public <T> T load(Class<T> clazz) {
		try {
			T instance = clazz.getDeclaredConstructor().newInstance();
			TomlParseResult toml = new Reader(file).read();
			if (toml.isEmpty()) {
				save(instance);
				return instance;
			}
			Field[] fields = instance.getClass().getDeclaredFields();
			Arrays.stream(fields).filter(field -> field.isAnnotationPresent(Option.class)).forEach(field -> iteratorFieldLoad(field, toml));
			return instance;
		} catch (IllegalAccessException illegalAccessException) {
			ClassException.traceClassAccessingException(PlumeConfigMod.LOGGER, illegalAccessException, clazz);
		} catch (InvocationTargetException | InstantiationException | NoSuchMethodException exception) {
			ClassException.traceClassConstructingException(PlumeConfigMod.LOGGER, exception, clazz);
		}
		return null;
	}

	private void iteratorFieldSave(Field field) {
		if (field.isAnnotationPresent(Comment.class)) {
			field.setAccessible(true);
			try {
				writeLine(field.get(field.getDeclaringClass().getDeclaredConstructor().newInstance()));
			} catch (Exception exception) {
				FieldException.traceFieldAccessingException(PlumeConfigMod.LOGGER, exception, file, field);
			}
			return;
		}

		if (field.isAnnotationPresent(Option.class)) {
			Option option = field.getAnnotation(Option.class);
			field.setAccessible(true);
			String key = !option.key().isEmpty() ? option.key() : field.getName();
			try {
				writeLine(
						key, FieldFunction.savingFunctions(modid,
								field.get(field.getDeclaringClass().getDeclaredConstructor().newInstance())),
						option.name(), option.comment()
				);
			} catch (IllegalAccessException illegalAccessException) {
				FieldException.traceFieldAccessingException(PlumeConfigMod.LOGGER, illegalAccessException, file, field);
			} catch (InvocationTargetException | InstantiationException | NoSuchMethodException exception) {
				ClassException.traceClassConstructingException(PlumeConfigMod.LOGGER, exception, field.getDeclaringClass());
			}
		}
	}

	private void iteratorFieldLoad(Field field, TomlParseResult toml) {
		Option option = field.getAnnotation(Option.class);
		String key = !option.key().isEmpty() ? option.key() : field.getName();
		field.setAccessible(true);
		if (toml.contains(key)) {
			try {
				field.set(field.getDeclaringClass().getDeclaredConstructor().newInstance(),
						FieldFunction.functions(modid, field.getType(), toml.get(key)));
			} catch (IllegalAccessException illegalAccessException) {
				FieldException.traceFieldInitializingException(PlumeConfigMod.LOGGER, illegalAccessException, file, field);
			} catch (InvocationTargetException | InstantiationException | NoSuchMethodException exception) {
				ClassException.traceClassConstructingException(PlumeConfigMod.LOGGER, exception, field.getDeclaringClass());
			}
		} else {
			FieldException.traceKeyDoesntAppearException(PlumeConfigMod.LOGGER, file, field.getName());
		}
	}

	/**
	 * @param contents	<strong>COMMENT: </strong>{comment}, <strong>OPTION: </strong>{key, value, name, comment}, anything else will be ignored.
	 */
	private void writeLine(Object... contents) {
		if (contents.length == 0) {
			return;
		}
		Writer writer = new Writer(file);
		contents = Arrays.stream(contents).filter(Objects::nonNull).filter(value -> !value.toString().isEmpty()).toArray(); // Filter null and empty values
		if (contents.length <= 1) { // It's a comment
			writer.write("# " + contents[0].toString(), true);
		} else { // It's an option (contents are at least key+value)
			writer.write(contents[0].toString() + " = " + contents[1].toString()); // key = value
			if (contents.length >= 4) { // It has both a name and a comment
				writer.write(" # " + contents[2].toString() + " | " + contents[3].toString(), true);
			} else if (contents.length == 3) { // It has either a name or a comment
				writer.write(" # " + contents[2].toString(), true);
			} else { // None of the above
				writer.nextLine();
			}
		}
	}
}
