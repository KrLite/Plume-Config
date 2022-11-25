### [< Index](index.md)

# Saving Options

**Simply load the config after you created a config instance, the files will be automatically created or read:**

```java
exampleConfig.load();
```

You can put the method in your mod initializer.

***

**Use the following code to save the config as you want:**

```java
exampleConfig.write();
```

All the configs in the dedicated file will be overwritten, missing ones will be added as the default values.
