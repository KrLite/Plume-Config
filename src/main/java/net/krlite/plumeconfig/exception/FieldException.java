package net.krlite.plumeconfig.exception;

import org.slf4j.Logger;

import java.io.File;
import java.lang.reflect.Field;

public class FieldException {
	private static void traceFieldException(Logger logger, Exception exception, String message, File file, Field field) {
		if (exception instanceof IllegalAccessException) {
			logger.debug("Field " + field.getName() + " in config file " + file.toPath() + " is inaccessible. Make sure it is not a final field.");
		}
		logger.debug("Failed " + message + " field " + field.getClass().getName() + "." + field.getName() + " in config file " + file.toPath() + ":", exception);
	}

	public static void traceKeyDoesntAppearException(Logger logger, File file, String key) {
		logger.error("Config file " + file.toPath() + " doesn't contain config '" + key + "'!");
	}

	public static void traceFieldInitializingException(Logger logger, Exception exception, File file, Field field) {
		traceFieldException(logger, exception, "initializing", file, field);
	}

	public static void traceFieldAccessingException(Logger logger, Exception exception, File file, Field field) {
		traceFieldException(logger, exception, "accessing", file, field);
	}
}
