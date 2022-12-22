![Banner](artwork/banner.png)

# Plume Config [`based on TOML`](https://toml.io)

Plume Config is a lightweight configuration library made for Minecraft Mods, using [Tom's Obvious Minimal Language `TOML`](https://toml.io).

## Implementation

### TL;DR

`build.gradle`

```groovy
repositories {
    maven {
        url = "https://api.modrinth.com/maven"
    }
}

dependencies {
    /* Plume Config v3.2.0 */
    modApi include("maven.modrinth:plumeconfig:z9sRsmg6")
    
    /* Tomlj 1.1.0 for mod local runtime */
    modLocalRuntime("org.tomlj:tomlj:1.1.0")
}
```

### Explanation

Plume Config is simply using [Modrinth Maven](https://docs.modrinth.com/docs/tutorials/maven/), so you can add the followings to your `build.gradle` file:

`1. Add Modrinth Maven repository`

```groovy
repositories {
    maven {
        url = "https://api.modrinth.com/maven"
    }
}
```

`Modrinth's Way`

```groovy
repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = "https://api.modrinth.com/maven"
            }
        }
        filter {
            includeGroup "maven.modrinth"
        }
    }
}
```

`2. Add Plume Config dependency`

```groovy
dependencies {
    /* Plume Config v3.2.0 */
    modApi include("maven.modrinth:plumeconfig:z9sRsmg6")
}
```

> _**Important Notice**_
> 
> 1. Please use modApi instead of something else.
> 
> 2. Versions lower than v3.2.0 are outdated, and versions that are not the latest are not recommended.
> 
> 3. You are supposed to use the version identifier instead of the version number, please check them at [Modrinth Versions](https://modrinth.com/mod/plumeconfig/versions).
> 
> 4. Due to some *gradle* problems, only include Plume Config can cause crash at local runtime, but jar files are actually runnable. Just add `modLocalRuntime("org.tomlj:tomlj:1.1.0")` if you want to debug your mod at local runtime. See [TL;DR](#tldr) for the final gradle content.

## Demonstration

[`example.toml`](src/main/java/example/config/example.toml)

```toml
# 1
# comment2
# commented line
integer = 1 # Int
bool = false

[abc]
s = "string" # String | A String Comment
# comment3
d = 1.0

[def]
color = 0xff000000
```

## Usage

An example class is available: [`Example.java`](src/main/java/example/Example.java).

When you're readily implemented, you'll find it's easy to use, thanks to all the annotations and instance-based methods.

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

Beside the `@Option` annotation, you can also use the `@Comment` annotation to annotate fields to comments:

```java
@Comment
public String s = "Commented Line";

@Comment
private static int i = 12345;
```

```toml
# Commented Line
# 12345
```

> The `@Comment` annotation can only be used on fields, and it will not be operated as an option.
> If a field is annotated with both `@Option` and `@Comment`, the `@Option` annotation will be ignored, which means the field will be seen as a comment.

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

One of the main features of TOML is categories(work as dotted keys), and Plume Config also supports it by using the `@Category` annotation:

```java
@Category("abc")
public @Comment String catComment="Categorized Comment";

public @Option String uncat="Uncategorized";

@Category("abc")
public @Option String cat="Categorized";
```

```toml
uncat = "Uncategorized"

[abc]
# Categorized Comment
cat = "Categorized"
```

> The order of the fields is not important, Plume Config will sort them automatically by categories.

That's how it works. Now write some configs into your class:

```java
import java.awt.*;

public class MyConfig {
	@Option(key = "player_name", name = "Player Name", comment = "Your name in the game")
	public String playerName = "Lambda Sigma";

	@Option(comment = "Change this number smaller to make your mouse faster")
	public double d = 1.0;

	@Category("misc")
	@Comment public String comment = "=-Misc-=";

	@Option(comment = "The only identity in the map")
	public int color = Color.BLACK;

	@Category("misc")
	@Option(name = "Streaming switch", comment = "Toggle streaming mode")
	public boolean bool = false;
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
player_name = "Lambda Sigma" # Player Name | Your name in the game
d = 1.0 # Change this number smaller to make your mouse faster
color = 0xff000000 # The only identity in the map

[misc]
# =-Misc-=
bool = false # Streaming switch | Toggle streaming mode
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
        System.out.println(CONFIG_INSTANCE.d);
        System.out.println(CONFIG_INSTANCE.color);
        System.out.println(CONFIG_INSTANCE.bool);

	CONFIG_INSTANCE.playerName = "Lambda Sigma 2";
	CONFIG_INSTANCE.color = Color.RED;

	// Don't forget to save your config after changes!
        CONFIG.save(CONFIG_INSTANCE); // Now don't insert MyConfig.class, instead, use the config instance.
    }
}
```

Pretty cool, right? Plume Config doesn't support all toml features now, but we are always making everything better. Just watch our project if you are interested in the progress!

## License

Plume Config is licensed under the [MIT License](LICENSE).
