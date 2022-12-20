package example;

public class ExampleHolder {
	public static void main(String[] args) {
		System.out.println(resolve("config.toml"));
	}

	public static String resolve(String fileName) {
		return fileName.replaceAll(".toml$", "") + ".toml";
	}
}

// TODO: Sort fields by categories
// TODO: Add support for different types
// TODO: Deserialize TOML to Java object
