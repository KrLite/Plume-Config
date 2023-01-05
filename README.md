# _This README Needs a Rewrite._

![Banner](artwork/banner.png)

### <p align=right>[Modrinth →](https://modrinth.com/mod/plumeconfig)<br /><sub>[Based on `TOML`](https://toml.io)&ensp;&emsp;</sub></p>

# Plume Config

Plume Config is a lightweight configuration library made for Minecraft Mods, based on [Tom's Obvious Minimal Language `TOML`](https://toml.io).

## Implementation

### TL;DR

`build.gradle`

```groovy
repositories {
    maven { url = "https://api.modrinth.com/maven" }
}

dependencies {
    modImplementation include("maven.modrinth:plumeconfig:xxx")
}
```

> Remember to replace `xxx` above to the latest `version id` of Plume Config(starts with `v`). You can check the versions on [Modrinth,](https://modrinth.com/mod/plumeconfig/versions) or just copy the latest release tag on [GitHub,](https://github.com/KrLite/Plume-Config/tags) they are the same.

## Demonstration

[`example.toml`](src/main/java/example/config/example.toml)

```toml
# This is a comment
# at the top of the file

# An integer
integer = 1 # Int
# A boolean
bool = false

[abc]
# Multi-line
# comment
s = "string" # String | A String Comment
d = 1.0

[def]
# A color
# Which is under supported
color = 0xff000000
```

## Usage

An example class is available: [`Example.java`](src/main/java/example/Example.java).

When you're readily implemented, you'll find it's easy to use, thanks to all the annotations and instance-based methods.

### Instance

First of all, declare a `ConfigFile` instance:

```java
public class MyModInitializer {
    public static final ConfigFile CONFIG = new ConfigFile("my_modid", "my_config.toml"); // There are other constructors available, see javadoc for more
}
```

Then, design your own config class:

```java
public class MyConfig {
    // Keep reading...
}
```

### `@Option`

In the class, when you want to declare a field as an option, just use the annotation `@Option`:

```java
@Option // You can write them before the field, or...
public String s = "string";

private static @Option int integer = 1; // ...after the modifier
// An option field can be either public or private, but not protected
// An option field can also be final, but it's not recommended and the value won't change
```

```toml
s = "string"
integer = 1
```

> The default key of an option is the field name, in the rest of the document you will know the way to change the key.

The annotation `@Option` is also used to add additional information to the option, for example, a different key:

```java
@Option(key = "different_key")
public String s = "string";
```

```toml
different_key = "string"
```

> You cannot use duplicate keys in the same file, Plume Config will not check that, but may crash or cause unexpected behavior.

In special cases, multi-line string contents will be in multi-line format:

```java
@Option
public String s = "Multi-\nline\\\nString!"
```

```toml
s = """
Multi
line\
String!"""
```

You can also add a name and a comment to an option:

```java
@Option(name = "String", comment = "A String Comment")
public String s = "string";

@Option(key = "integer", name = "Integer")
private static int i = 1;
```

```toml
s = "string" # String | A String Comment
integer = 1 # Integer
```

### `@Comment`

Beside the `@Option` annotation, you can also use the `@Comment` annotation to append comments to fields:

```java
@Comment("Commented")
@Comment("By two")
public @Option String s = "String";

@Comment(value = "Also commented", newLine = LineBreak.BEFORE)
private @Option static int i = 12345;
```

```toml
s = "String"
# Commented
# By two
i = 12345

# Also commented
```

> `@Comment` annotations only work when the target field is already annotated by `@Option`.
> 
> It is fine to use as many `@Comment` annotations as you want, each will take a line.
> 
> The enum class `LineBreak` can be used to define the behaviour of the line breaks.

The `@Comment` annotations can not only be used on fields, but also be used before class definations.

```java
@Comment("This is a comment")
@Comment(value = "at the top of the file", newLine = LineBreak.AFTER)
public class MyConfig {
	//...
}
```

```toml
# This is a comment
# at the top of the file



```

### `@Category`

One of the main features of TOML is categories(work as dotted keys), and Plume Config also supports it by using the `@Category` annotation:

```java
public @Option String uncat="Uncategorized";

@Comment("Categorized comment")
@Category("abc")
public @Option String cat="Categorized";
```

```toml
uncat = "Uncategorized"

[abc]
cat = "Categorized"
# Categorized comment
```

> The order of the fields is not important, Plume Config will sort them automatically by categories.

That's how it works. Now write some configs into your class:

```java
import java.awt.*;

@Comment("This file controls your player behaviour in game", newLine = LineBreak.AFTER)
public class MyConfig {
	@Option(key = "player_name", name = "Player Name", comment = "Your name in the game")
	public String playerName = "Sigma";

	@Option(comment = "Health bar length")
	public double health = 1.0;

	@Comment("Don't change this unless in development environment")
	@Category("misc")
	@Option(comment = "Your only identity")
	public int identity = Color.BLACK;
	
	@Option(key = "streaming_switch", comment = "Toggle streaming mode")
	public boolean streamingSwitch = false;
}
```

Run your mod as you load your config instance:

```java
public class MyModInitializer implements ModInitializer {
    public static final ConfigFile CONFIG = new ConfigFile("my_modid", "my_config.toml");
    public static final MyConfig CONFIG_INSTANCE = new MyConfig();

    @Override
    public void onInitialize() {
        // Don't worry about nonexistent files, Plume Config will create them.
        CONFIG_INSTANCE = CONFIG.load(MyConfig.class); // Insert your config class to let Plume Config know what to load.
    }
}
```

Your config file(`run/config/my_modid/my_config.toml`) will look like this:

```toml
# This file controls your player behaviour in game

player_name = "Sigma" # Player Name | Your name in the game
health = 1.0 # Health bar length
streaming_switch = false # Toggle streaming mode

[misc]
identity = 0xff000000 # Your only identity in the map
# Don't change this unless in development environment
```

In your code, make sure everytime you load your mod, you read your config first.

Now, just access the fields as usual:

```java
public class MyModInitializer implements ModInitializer {
    public static final ConfigFile CONFIG = new ConfigFile("my_modid", "my_config.toml");
    public static final MyConfig CONFIG_INSTANCE = new MyConfig();

    @Override
    public void onInitialize() {
        CONFIG_INSTANCE = CONFIG.load(MyConfig.class);
		
	// They are good now
        System.out.println(CONFIG_INSTANCE.playerName);
        System.out.println(CONFIG_INSTANCE.health);
        System.out.println(CONFIG_INSTANCE.identity);
        System.out.println(CONFIG_INSTANCE.streamingSwitch);

	CONFIG_INSTANCE.playerName = "Lambda";
	CONFIG_INSTANCE.identity = Color.RED;

	// Don't forget to save your config after changes!
        CONFIG.save(CONFIG_INSTANCE); // Now don't insert MyConfig.class, instead, use the config instance.
    }
}
```

Pretty cool, right? Plume Config may not supporting all toml features now, but we are always making everything better. Just watch our project if you are interested in the progress!

## License

Plume Config is licensed under the [GNU Public License](LICENSE).
