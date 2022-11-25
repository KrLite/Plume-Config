### [< Index](Index.md)

# Creating a Config Instance

**Example class holding a config file:**

```java
public class ExampleConfig extends PlumeConfig {
	// The constructor, you can modify the path and file name here or leave them as parameters
	public ExampleConfig(String path, String fileName) {
		super(path, fileName);
	}

	// Define your options here using the Option* classes
	public OptionBoolean exampleBoolean = new OptionBoolean("The name of the example boolean", "option.example.boolean", false);
	public OptionDouble exampleDouble = new OptionDouble(/* The name can be null */ "option.example.double", 0.0 /* The comment can also be null */);
	public OptionLong exampleInt = new OptionLong("option.example.int", 0 /* The key and the default value cannot be null */);
	public OptionString exampleString = new OptionString("My example string", "option.example.string", "abc", "Hey, this is an example comment!");
	public OptionColor exampleColor = new OptionColor("option.example.color", Color.WHITE, "Expected color value: AARRGGBB");

	/**
	 * Use config.parse() method to read from the config file.
	 *
	 * @param config	A HashMap represents the config file, can be read by the config keys.
	 */
	public void read(HashMap<String, String> config) {
		exampleBoolean.parse(config.get(exampleBoolean.getKey()));
		exampleDouble.parse(config.get(exampleDouble.getKey()));
		exampleInt.parse(config.get(exampleInt.getKey()));
		exampleString.parse(config.get(exampleString.getKey()));
		exampleColor.parse(config.get(exampleColor.getKey()));
	}

	/**
	 * Put the values you want to append to the config file here.
	 *
	 * @param configBuilder		A ConfigBuilder object, use configBuilder.accept() to append to the config file.
	 */
	public void save(ConfigBuilder configBuilder) {
		// We can append categories
		configBuilder.accept("Category Booleans");
		// Append an option (do not do it twice to the same option)
		configBuilder.accept(exampleBoolean);

		// Another category
		configBuilder.accept("Category Numbers");
		configBuilder.accept(exampleDouble);
		configBuilder.accept(exampleInt);

		configBuilder.accept("Category String Values");
		configBuilder.accept(exampleString);
		configBuilder.accept(exampleColor);

		// ...More options and categories
	}
}
```

**Use the following code to create an instance:**

```java
ExampleConfig exampleConfig = new ExampleConfig("example", "exampleConfig");
```

**Accessing the options:**

```java
boolean got = exampleConfig.exampleBoolean.getValue();

exampleConfig.exampleBoolean.set(true);
```
