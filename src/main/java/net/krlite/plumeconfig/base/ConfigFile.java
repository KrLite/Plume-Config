package net.krlite.plumeconfig.base;

import net.fabricmc.loader.api.FabricLoader;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.annotation.Category;
import net.krlite.plumeconfig.annotation.Comment;
import net.krlite.plumeconfig.annotation.Option;
import net.krlite.plumeconfig.api.EnumLocalizable;
import net.krlite.plumeconfig.exception.ClassException;
import net.krlite.plumeconfig.exception.FieldException;
import net.krlite.plumeconfig.exception.FileException;
import net.krlite.plumeconfig.io.MappedString;
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
	private final boolean formatted;

	/**
	 * Creates a new config file.
	 * @param modid	The modid of the mod that this config file belongs to.
	 * @param file	The file with absolute path, <strong>which file name must be <code>.toml</code> suffixed.</strong>
	 */
	public ConfigFile(String modid, File file) {
		this(modid, file, true);
	}

	/**
	 * @param formatted	Whether the config file should be formatted.
	 */
	public ConfigFile(String modid, File file, boolean formatted) {
		this.modid = modid;
		this.file = file;
		this.formatted = formatted;
	}

	/**
	 * Creates a new config file named <code>&lt;modid&gt;.toml</code> under the game config directory.
	 * @param modid	The modid of the mod that this config file belongs to.
	 */
	public ConfigFile(String modid) {
		this(modid, true);
	}

	/**
	 * @param formatted	Whether the config file should be formatted.
	 */
	public ConfigFile(String modid, boolean formatted) {
		this(modid, FabricLoader.getInstance().getConfigDir().resolve(modid + ".toml").toFile(), formatted);
	}

	/**
	 * Creates a new config file under a subdirectory named <code>&lt;modid&gt;</code> under the game config directory.
	 * @param modid		The modid of the mod that this config file belongs to.
	 * @param fileName	The name of the config file, <strong>which will be automatically suffixed.</strong>
	 */
	public ConfigFile(String modid, String fileName) {
		this(modid, fileName, true);
	}

	/**
	 * @param formatted	Whether the config file should be formatted.
	 */
	public ConfigFile(String modid, String fileName, boolean formatted) {
		this(modid, FabricLoader.getInstance().getConfigDir()
							.resolve(modid)
							.resolve(fileName.replaceAll(".toml$" /* Replace .toml suffix in case for duplication */, "") + ".toml")
							.toFile(),
				formatted
		);
	}

	/**
	 * Load the config file into an instance, and save it right away.
	 * @param clazz	The class of the config instance.
	 * @return		The loaded config instance.
	 * @param <T>	The parameter of the instance class.
	 */
	public <T> T loadAndSave(Class<T> clazz) {
		T instance = load(clazz);
		save(instance);
		return instance;
	}

	/**
	 * Saves the config instance into the file.
	 * @param instance	The config instance to be saved.
	 * @param <T>		The parameter of the instance class.
	 */
	public <T> void save(T instance) {
		Writer writer = new Writer(file);
		try {
			writer.initialize();
		} catch (IOException ioException) {
			FileException.traceFileWritingException(PlumeConfigMod.LOGGER, ioException, file);
		}

		// Fields annotated by @Comment or @Option
		Field[] fields = Arrays.stream(instance.getClass().getDeclaredFields())
								 .filter(field -> field.isAnnotationPresent(Option.class) || field.isAnnotationPresent(Comment.class)).toArray(Field[]::new);

		// Save Uncategorized fields
		Arrays.stream(fields).filter(field -> !field.isAnnotationPresent(Category.class)).forEach(field -> iteratorFieldSave(instance, field));

		// Categories
		Category[] categories = Arrays.stream(fields)
										.filter(field -> field.isAnnotationPresent(Category.class))
										.map(field -> field.getAnnotation(Category.class))
										.distinct().toArray(Category[]::new);

		// Save categorized fields
		Arrays.stream(categories).forEach(
				category -> Arrays.stream(fields).filter(field -> field.isAnnotationPresent(Category.class))
						.filter(field -> field.getAnnotation(Category.class).equals(category))
						.forEach(field -> iteratorFieldSave(instance, field))
		);

		// Format the config file
		if (formatted) writer.format();
	}

	/**
	 * Loads the config file into an instance.
	 * @param clazz	The class of the config instance.
	 * @return		The loaded config instance.
	 * @param <T>	The parameter of the instance class.
	 */
	@Nullable
	public <T> T load(Class<T> clazz) {
		try {
			// Create a new instance
			T instance = clazz.getDeclaredConstructor().newInstance();
			TomlParseResult toml = new Reader(file).read();

			// If empty, save the new instance
			if (toml.isEmpty()) {
				save(instance);
				return instance;
			}
			Field[] fields = instance.getClass().getDeclaredFields();

			// Load all fields annotated by @Option into the instance
			Arrays.stream(fields).filter(field -> field.isAnnotationPresent(Option.class))
					.forEach(field -> iteratorFieldLoad(instance, field, toml));
			return instance;
		} catch (IllegalAccessException illegalAccessException) {
			ClassException.traceClassAccessingException(PlumeConfigMod.LOGGER, illegalAccessException, clazz);
		} catch (InvocationTargetException | InstantiationException | NoSuchMethodException exception) {
			ClassException.traceClassConstructingException(PlumeConfigMod.LOGGER, exception, clazz);
		}
		return null;
	}

	/**
	 * Iterates the field's attributes and saves it into the file.
	 * @param field	The field to be iterated.
	 */
	private void iteratorFieldSave(Object instance, Field field) {
		// Writes the category if exist
		if (field.isAnnotationPresent(Category.class)) writeCategory(field.getAnnotation(Category.class));
		field.setAccessible(true);

		// Writes the field as a comment, if annotated by @Comment
		if (field.isAnnotationPresent(Comment.class)) {
			try {
				writeLine(field.get(instance));
			} catch (IllegalAccessException illegalAccessException) {
				FieldException.traceFieldAccessingException(PlumeConfigMod.LOGGER, illegalAccessException, file, field);
			}
			return;
		}

		// Writes the field as an option, if annotated by @Option and not annotated by @Comment
		if (field.isAnnotationPresent(Option.class)) {
			Option option = field.getAnnotation(Option.class);
			String key = !option.key().isEmpty() ? option.key() : field.getName();
			try {
				if (field.getType().isEnum()) {
					if (EnumLocalizable.class.isAssignableFrom(field.getType())) {
						writeLine(
								key, FieldFunction.savingFunctions(modid,
										((EnumLocalizable) field.get(instance)).getLocalizedName()),
								option.name(), option.comment()
						);
					} else {
						writeLine(
								key, FieldFunction.savingFunctions(modid,
										((Enum<?>) field.get(instance)).name()),
								option.name(), option.comment()
						);
					}
				} else {
					writeLine(
							key, FieldFunction.savingFunctions(modid,
									field.get(instance)),
							option.name(), option.comment()
					);
				}
			} catch (IllegalAccessException illegalAccessException) {
				FieldException.traceFieldAccessingException(PlumeConfigMod.LOGGER, illegalAccessException, file, field);
			}
		}
	}

	/**
	 * Iterates the field's attributes and loads it from the file.
	 * @param field	The field to be iterated.
	 * @param toml	The TomlParseResult from which the file to be loaded.
	 */
	private void iteratorFieldLoad(Object instance, Field field, TomlParseResult toml) {
		Option option = field.getAnnotation(Option.class);
		field.setAccessible(true);

		// The key equals to the field name if not specified, or the specified category dotted key
		String key = !option.key().isEmpty() ? option.key() : field.getName();
		if (field.isAnnotationPresent(Category.class)) key = new MappedString(field.getAnnotation(Category.class).value()).mapLineBreaks().mapSpaces().get() + "." + key; // Line breaks are needed to be replaced by dots, but spaces are ok.

		if (toml.contains(key) && toml.get(key) != null) {
			try {
				if (field.getType().isEnum()) {
					String value = toml.getString(key);
					if (EnumLocalizable.class.isAssignableFrom(field.getType())) {
						field.set(
								instance,
								Arrays.stream((EnumLocalizable[]) field.get(instance).getClass().getEnumConstants())
										.filter(enumLocalizable -> enumLocalizable.getLocalizedName().equals(value))
										.findFirst().orElseThrow()
						);
					} else {
						field.set(
								instance,
								Arrays.stream((Enum<?>[]) field.get(instance).getClass().getEnumConstants())
										.filter(enumConstant -> enumConstant.name().equals(value))
										.findFirst().orElseThrow()
						);
					}
				} else {
					field.set(instance, FieldFunction.functions(modid, field.getType(), toml.get(key)));
				}
			} catch (IllegalAccessException illegalAccessException) {
				FieldException.traceFieldInitializingException(PlumeConfigMod.LOGGER, illegalAccessException, file, field);
			}
		} else {
			// The config file doesn't contain the key, why?
			FieldException.traceKeyDoesntAppearException(PlumeConfigMod.LOGGER, file, field.getName());
		}
	}

	/**
	 * Writes a line into the file with the given {@link Object}s.
	 * @param contents	<strong>COMMENT: </strong>{comment}, <strong>OPTION: </strong>{key, value, name, comment}, anything else will be ignored.
	 */
	private void writeLine(Object... contents) {
		if (contents.length == 0) return;
		Writer writer = new Writer(file);
		contents = Arrays.stream(contents).filter(Objects::nonNull).filter(value -> !value.toString().isEmpty()).toArray(); // Filter null and empty values
		if (contents.length <= 1) { // It's a comment
			Arrays.stream(contents[0].toString().split("\n"))
					.forEach(line -> writer.writeAndEndLine("# " + line.replaceAll("\n", "")));
		} else { // It's an option (contents are at least key+value)
			writer.write(contents[0].toString() + " = " + contents[1].toString()); // key = value
			if (contents.length >= 4) {
				// It has both a name and a comment
				writer.writeAndEndLine(
						" # " + new MappedString(contents[2].toString()).mapLineBreaks().get() + " | "
								+ new MappedString(contents[3].toString()).mapLineBreaks().get()
				);
			} else if (contents.length == 3) {
				// It has either a name or a comment
				writer.writeAndEndLine(
						" # " + new MappedString(contents[2].toString()).mapLineBreaks().get()
				);
			} else writer.nextLine(); // None of the above
		}
	}

	/**
	 * Writes a category into the file.
	 * @param category	The {@link Category} to be written.
	 */
	private void writeCategory(Category category) {
		Writer writer = new Writer(file);
		writer.writeAndEndLine("[" + new MappedString(category.value(), ".").mapLineBreaks().get() + "]"); // [category], line breaks are replaced by dots
	}
}
