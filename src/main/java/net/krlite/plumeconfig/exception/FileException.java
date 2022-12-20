package net.krlite.plumeconfig.exception;

import org.slf4j.Logger;

import java.io.File;

public class FileException {
	private static void traceFileException(Logger logger, Exception exception, String message, File file) {
		logger.debug("Failed " + message + " config file " + file.toPath() + ":", exception);
	}

	public static void traceFileInitializingException(Logger logger, Exception exception, File file) {
		traceFileException(logger, exception, "initializing", file);
	}

	public static void traceFileWritingException(Logger logger, Exception exception, File file) {
		traceFileException(logger, exception, "writing", file);
	}

	public static void traceFileReadingException(Logger logger, Exception exception, File file) {
		traceFileException(logger, exception, "reading", file);
	}
}
