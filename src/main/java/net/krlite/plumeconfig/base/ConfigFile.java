package net.krlite.plumeconfig.base;

import net.fabricmc.loader.api.FabricLoader;
import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.annotation.Category;
import net.krlite.plumeconfig.annotation.Comment;
import net.krlite.plumeconfig.annotation.Comments;
import net.krlite.plumeconfig.annotation.Option;
import net.krlite.plumeconfig.api.EnumLocalizable;
import net.krlite.plumeconfig.exception.ClassException;
import net.krlite.plumeconfig.exception.FieldException;
import net.krlite.plumeconfig.exception.FileException;
import net.krlite.plumeconfig.io.MappedString;
import net.krlite.plumeconfig.io.Reader;
import net.krlite.plumeconfig.io.Writer;
import org.jetbrains.annotations.NotNull;
import org.tomlj.TomlParseResult;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;

/**
 * A class that represents a configuration file.
 * @param config	The configuration class.
 * @param modid		The id of the mod.
 * @param file		The file to be saved/loaded.
 * @param formatted	Whether the file should be formatted.
 * @param <T>		The type of the configuration class.
 */
public record ConfigFile<T>(@NotNull Config<T> config, @NotNull String modid, @NotNull File file, boolean formatted) {
	/**
	 * Creates a new ConfigFile instance.
	 * @param clazz	The class which the config instance belongs.
	 * @param modid	The id of the mod.
	 */
	public ConfigFile(Class<T> clazz, String modid) {
		this(clazz, modid, true);
	}

	/**
	 * Creates a new ConfigFile instance.
	 * @param clazz		The class which the config instance belongs.
	 * @param modid		The id of the mod.
	 * @param formatted	Whether the config file should be formatted.
	 */
	public ConfigFile(Class<T> clazz, String modid, boolean formatted) {
		this(clazz, modid, FabricLoader.getInstance().getConfigDir().resolve(modid + ".toml").toFile(), formatted);
	}

	/**
	 * Creates a new ConfigFile instance.
	 * @param clazz		The class which the config instance belongs.
	 * @param modid		The id of the mod.
	 * @param fileName	The name of the file that this config will be saved to, no need to be suffixed.
	 */
	public ConfigFile(Class<T> clazz, String modid, String fileName) {
		this(clazz, modid, fileName, true);
	}

	/**
	 * Creates a new ConfigFile instance.
	 * @param clazz		The class which the config instance belongs.
	 * @param modid		The id of the mod.
	 * @param fileName	The name of the file that this config will be saved to, no need to be suffixed.
	 * @param formatted	Whether the config file should be formatted.
	 */
	public ConfigFile(Class<T> clazz, String modid, String fileName, boolean formatted) {
		this(clazz, modid, FabricLoader.getInstance().getConfigDir()
							.resolve(modid)
							.resolve(fileName.replaceAll(".toml$" /* Replace .toml suffix in case for duplication */, "") + ".toml")
							.toFile(),
				formatted
		);
	}

	/**
	 * Create a new ConfigFile instance.
	 * @param clazz	The class which the config instance belongs.
	 * @param modid	The if of the mod.
	 * @param file	The file to be saved/loaded.
	 */
	public ConfigFile(Class<T> clazz, String modid, File file) {
		this(clazz, modid, file, true);
	}

	/**
	 * Creates a new ConfigFile instance.
	 * @param clazz		The class which the config instance belongs.
	 * @param modid		The id of the mod.
	 * @param file		The file to be saved/loaded.
	 * @param formatted	Whether the config file should be formatted.
	 */
	public ConfigFile(Class<T> clazz, String modid, File file, boolean formatted) {
		this(new Config<T>(clazz), modid, file, formatted);
	}

	/**
	 * Loads the config instance from the file, and then save it.
	 * @return	The config instance.
	 */
	public T operate() {
		return save(load());
	}

	/**
	 * Loads the config instance from the file, and then save it.
	 * @param forceLoad	Whether to force load the config from the file.<br />
	 *                  <code>true</code>:	reload the config from the file.<br />
	 *                  <code>false</code>:	load the config from the file only if not loaded.
	 * @return			The config instance.
	 */
	public T operate(boolean forceLoad) {
		return save(load(forceLoad));
	}

	/**
	 * Saves the config instance to file.
	 * @param instance	The config instance to be saved.
	 * @return			The config instance itself.
	 */
	public T save(T instance) {
		Writer writer = new Writer(file);
		try {
			writer.initialize();
		} catch (IOException ioException) {
			FileException.traceFileWritingException(PlumeConfigMod.LOGGER, ioException, file);
		}

		// Writes the class comments if annotated by @Comments, otherwise writes the comment if annotated by @Comment
		if (instance.getClass().isAnnotationPresent(Comments.class)) {
			Comments comments = instance.getClass().getAnnotation(Comments.class);
			Arrays.stream(comments.value()).filter(Objects::nonNull).forEach(comment -> {
				if (comment.end().isBefore()) writeLine("");
				writeLine(comment.value());
				if (comment.end().isAfter()) writeLine("");
			});
		} else if (instance.getClass().isAnnotationPresent(Comment.class)) {
			Comment comment = instance.getClass().getAnnotation(Comment.class);
			if (comment.end().isBefore()) writeLine("");
			writeLine(comment.value());
			if (comment.end().isAfter()) writeLine("");
		}

		// Fields annotated by @Comment or @Option
		Field[] fields = Arrays.stream(instance.getClass().getDeclaredFields())
								 .filter(field -> field.isAnnotationPresent(Option.class)).toArray(Field[]::new);

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

		return instance;
	}

	/**
	 * Loades the config instance from the file if not loaded.
	 * @return	The config instance.
	 */
	public T load() {
		return load(false);
	}

	/**
	 * Loads the config instance from the file.
	 * @param forceLoad	Whether to force load the config from the file.<br />
	 *                  <code>true</code>:	reload the config from the file.<br />
	 *                  <code>false</code>:	load the config from the file only if not loaded.
	 * @return			The config instance.
	 */
	public T load(boolean forceLoad) {
		// Avoid parallel loading
		if (!forceLoad && config.hasInstance()) return config.instance;

		try {
			T instance = config.clazz.getDeclaredConstructor().newInstance();
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

			// Override the config instance
			return config.accept(instance);
		} catch (IllegalAccessException illegalAccessException) {
			ClassException.traceClassAccessingException(PlumeConfigMod.LOGGER, illegalAccessException, config.clazz);
			throw new RuntimeException(illegalAccessException);
		} catch (InvocationTargetException | InstantiationException | NoSuchMethodException exception) {
			ClassException.traceClassConstructingException(PlumeConfigMod.LOGGER, exception, config.clazz);
			throw new RuntimeException(exception);
		}
	}

	/**
	 * Iterates the field's attributes and saves it into the file.
	 * @param field	The field to be iterated.
	 */
	private void iteratorFieldSave(Object instance, Field field) {
		// Writes the category if exist
		if (field.isAnnotationPresent(Category.class)) writeCategory(field.getAnnotation(Category.class));
		field.setAccessible(true);

		// Writes the field's comment(s) before field
		if (field.isAnnotationPresent(Comments.class)) iteratorFieldComment(Arrays.stream(field.getAnnotation(Comments.class).value()).filter(Comment::beforeField).toArray(Comment[]::new));
		else if (field.isAnnotationPresent(Comment.class) && field.getAnnotation(Comment.class).beforeField()) iteratorFieldComment(new Comment[]{field.getAnnotation(Comment.class)});

		// Writes the field option
		if (field.isAnnotationPresent(Option.class)) {
			Option option = field.getAnnotation(Option.class);
			String key = !option.key().isEmpty() ? option.key() : field.getName();
			try {
				if (field.getType().isEnum()) {
					if (EnumLocalizable.class.isAssignableFrom(field.getType())) { // Localizable enum (Enum<?> extends EnumLocalizable)
						writeLine(
								key, FieldFunction.savingFunctions(modid,
										((EnumLocalizable) field.get(instance)).getLocalizedName()),
								option.name(), option.comment()
						);
					} else { // Non-localizable enum (Enum<?>)
						writeLine(
								key, FieldFunction.savingFunctions(modid,
										((Enum<?>) field.get(instance)).name()),
								option.name(), option.comment()
						);
					}
				} else { // Non-enum
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

		// Writes the field's comment(s) after field
		if (field.isAnnotationPresent(Comments.class)) iteratorFieldComment(Arrays.stream(field.getAnnotation(Comments.class).value()).filter(comment -> !comment.beforeField()).toArray(Comment[]::new));
		else if (field.isAnnotationPresent(Comment.class) && !field.getAnnotation(Comment.class).beforeField()) iteratorFieldComment(new Comment[]{field.getAnnotation(Comment.class)});
	}

	/**
	 * Iterates and writes the field's comments.
	 * @param comments	The comments to be iterated.
	 */
	private void iteratorFieldComment(Comment[] comments) {
		Arrays.stream(comments).filter(Objects::nonNull).forEach(comment -> {
			if (comment.end().isBefore()) writeLine("");
			writeLine(comment.value());
			if (comment.end().isAfter()) writeLine("");
		});
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
					if (EnumLocalizable.class.isAssignableFrom(field.getType())) { // Localizable enum (Enum<?> extends EnumLocalizable)
						field.set(
								instance,
								Arrays.stream((EnumLocalizable[]) field.get(instance).getClass().getEnumConstants())
										.filter(enumLocalizable -> enumLocalizable.getLocalizedName().equals(value))
										.findFirst().orElse((EnumLocalizable) field.get(instance))
						);
					} else { // Non-localizable enum (Enum<?>)
						field.set(
								instance,
								Arrays.stream((Enum<?>[]) field.get(instance).getClass().getEnumConstants())
										.filter(enumConstant -> enumConstant.name().equals(value))
										.findFirst().orElse((Enum<?>) field.get(instance))
						);
					}
				} else { // Non-enum
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
		contents = Arrays.stream(contents).filter(Objects::nonNull).toArray(); // Filter null values
		if (contents.length <= 1) { // A comment
			Arrays.stream(contents[0].toString().split("\n"))
					.forEach(line -> {
						if (!line.isEmpty()) writer.writeAndEndLine("# " + new MappedString(line).mapLineBreaks().get());
						else writer.writeAndEndLine("");
					});
		} else { // An option (contents are at least key+value)
			contents = Arrays.stream(contents).filter(content -> !content.toString().isEmpty()).toArray(); // Filter empty values
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
