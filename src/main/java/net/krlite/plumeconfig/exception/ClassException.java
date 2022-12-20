package net.krlite.plumeconfig.exception;

import org.slf4j.Logger;

public class ClassException {
	public static void traceClassConstructingException(Logger logger, Exception exception, Class<?> clazz) {
		logger.error("Failed constructing " + clazz.getName() + ":", exception);
	}

	public static void traceClassAccessingException(Logger logger, Exception exception, Class<?> clazz) {
		logger.error("Failed accessing " + clazz.getName() + ":", exception);
	}
}
