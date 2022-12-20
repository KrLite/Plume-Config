package net.krlite.plumeconfig.io;

import net.krlite.plumeconfig.PlumeConfigMod;
import net.krlite.plumeconfig.exception.FileException;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Writer {
	private final File file;

	public Writer(File file) {
		this.file = file;
	}

	public void format() {
		try {
			create();
		} catch (IOException ioException) {
			FileException.traceFileInitializingException(PlumeConfigMod.LOGGER, ioException, file);
		}
		try {
			FileReader reader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(reader);
			if (bufferedReader.ready()) {
				ArrayList<String> raw = new ArrayList<>();
				String line, lastLine = null;
				while ((line = bufferedReader.readLine()) != null) {
					if (line.startsWith("[") && line.endsWith("]") && !(lastLine == null)) {
						raw.add(""); // Add empty lines before categories
					}
					raw.add(line);
					lastLine = line;
				}
				flipStream(raw);
				ArrayList<String> formatted = raw.stream().dropWhile(String::isEmpty).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
				flipStream(formatted);

				initialize();
				formatted.stream().limit(formatted.size() - 1).forEach(this::writeAndEndLine);
				formatted.stream().skip(formatted.size() - 1).forEach(this::write);
			}
		} catch (IOException fileNotFoundException) {
			FileException.traceFileReadingException(PlumeConfigMod.LOGGER, fileNotFoundException, file);
		}
	}

	private void flipStream(ArrayList<String> arrayList) {
		AtomicInteger index = new AtomicInteger(0);
		arrayList.stream().limit(arrayList.size() / 2).forEach(l -> {
			arrayList.set(index.get(), arrayList.get(arrayList.size() - 1 - index.get()));
			arrayList.set(arrayList.size() - 1 - index.get(), l);
			index.getAndIncrement();
		});
	}

	public void create() throws IOException {
		if (!file.exists()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
			PlumeConfigMod.LOGGER.info("Created config file " + file.toPath());
		}
	}

	public void initialize() throws IOException {
		create();
		FileWriter writer = new FileWriter(file);
		writer.write("");
		writer.flush();
		writer.close();
	}

	public void writeAndEndLine(String content) {
		write(content, true);
	}

	public void write(String content) {
		write(content, false);
	}

	public void write(String content, boolean lineBreak) {
		try {
			create();
		} catch (IOException ioException) {
			FileException.traceFileInitializingException(PlumeConfigMod.LOGGER, ioException, file);
		}
		try {
			FileWriter writer = new FileWriter(file, true);
			writer.write(content);
			if (lineBreak) writer.write("\n");
			writer.flush();
			writer.close();
		} catch (IOException ioException) {
			FileException.traceFileWritingException(PlumeConfigMod.LOGGER, ioException, file);
		}
	}

	public void nextLine() {
		write("", true);
	}
}
