# `< Plume 🪶 Config >`

### `Fabric` `Minecraft 1.18.2` `...` `0.0.1 α`

## `📖 Introduction`

**Plume Config is a lightweight Configuration Library for Minecraft Mods.**

If you are looking for an easy way to create, write and read configuration files for your mod, this is the library for you. Just a few simple steps will get you up and running in no time.

Plume Config is using `.properties` files to store the configuration. **This is a simple and easy to read format from Java.** This means that you can easily edit the configuration files with any text editor.

## `📦 Installation`

**Plume Config is currently in `alpha.`**
You can download alpha releases from GitHub for now.

If you meet the requirements, feel free to build the library yourself.

## `📝 Configuration`

### `🧑‍💻 Example Config Content`

```properties
# | Category Booleans |

# The name of the example boolean
option.example.boolean=false
# Default: false


# | Category Numbers |

option.example.double=0.0
# Default: 0.0

option.example.color=FFFFFFFF
# Default: FFFFFFFF
# Expected color value: AARRGGBB

# My example string
option.example.string=abc
# Default: abc
# Hey, this is an example comment!

option.example.int=0
# Default: 0


# | Category String Values |
```

> **Note:**
> 
> The `#` character is used to comment out lines. This means that the line will be ignored by the library. You can define the comments in your codes to add descriptions to your configuration.
>
> The `=` character is used to separate the key from the value. The key must be unique to identify the value. The value can be a boolean, a number, a string or a hex-string stored integer color (custom value types will be allowed in future).

> **Please refer to our [wiki pages](wiki/index.md) for example codes.**

### `📁 Structure`

The configuration files are stored in the `config` folder of your Minecraft instance. The folder structure is as follows:

    config
    ├── <modid>
    │   ├── <config1>.properties
    │   └── <config2>.properties
    └── <another_modid>
        └── <another_config>.properties

Folder names and file names are defined by the mod developer, and there could be multiple configuration files under one folder. Plume Config will create the folders and files if they do not exist.

**Please use your modid as the folder name in case of repetition with another mod.**

## `📜 License`

This mod is available under the `MIT license.`
