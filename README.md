### <p align=right>[Modrinth →](https://modrinth.com/mod/plumeconfig)</p>

# Plume 🪶 Config

**Available on `Fabric` `Quilt` for Minecraft `~1.18` `~1.19`**

![banner](artwork/banner-big.png)

## `📖 Introduction`

**Plume Config is a lightweight Configuration Library for Minecraft Mods.**

If you are looking for an easy way to create, write and read configuration files for your mod, Plume Config is the library made for you. Just a few simple steps will get you up and running in no time.

Plume Config is using [`Json 5`](https://github.com/json5/json5) language to store configurations, which is **a humanized marking language based on [`JSON.`](https://tools.ietf.org/html/rfc7159)** This means that anyone can easily read and edit the configurations by hand!

## `📦 Implementation`

You can include Plume Config in your project by adding the following to your `build.gradle` file:

**Plume Config is simply using the [Modrinth Maven.](https://docs.modrinth.com/docs/tutorials/maven/#loom-fabric-quilt-architectury)**

```groovy
repositories {
    // Other repositories can go above or below Modrinth's. We don't need priority :)
    // Note: this is NOT the `repositories` block in `pluginManagement`! This is its own block.
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

```groovy
dependencies {
    modApi include("maven.modrinth:plumeconfig:xxx")
}
```

**Remember to replace `xxx` above with the latest [`version id`](https://modrinth.com/mod/plumeconfig/versions) of Plume Config.**

If you meet the requirements, feel free to build the library yourself.

## `📝 Configuration`

### `🧑‍💻 Example Config Content`

**Plume Config is using [GSON](https://github.com/google/gson) to handle config files, which is not very compatible with `JSON 5,` so the final result written in the files will be of `JSON` format.**
However, you can still use `JSON 5` to write your config files, as the library will handle the conversion for you.

In the future, we will add native support for `JSON 5` format files.

```json5
{
  "ol": "3",
  "ob": "true",
  "os": "abc",
  "oe": "EXAMPLE_ENUM_2",
  "oc": "FF000001"
}
```

**Please refer to our [wiki pages](wiki/index.md) for example usages.**

### `📁 Structure`

The configuration files are stored in the `config` folder of your Minecraft instance. The folder structure is as follows:

    config
    ├── <modid>
    │   ├── <config1>.properties
    │   └── <config2>.properties
    └── <another_modid>
        └── <another_config>.properties

Folder names and file names are defined by yourself, and there could be multiple configuration files under one folder. Plume Config will create the folders and files if they do not exist.

**Please use your modid as the folder name in case of repetition with other mods.**

## `📜 License`

This mod is available under the [`MIT license.`](LICENSE)
