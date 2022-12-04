package net.krlite.plumeconfig.option.core;

import java.util.Arrays;

public class EnumParser {

	/**
	 * Gets the enum constant by the localized name.
	 * @param localizedName	the localized name of the enum constant.
	 * @return			    the enum constant (the first constant if more than 1 or an {@link EnumConstantNotPresentException} if it doesn't exist).
	 */
	public static <E extends Enum<E> & ILocalizable> E parseValue(String localizedName, Class<E> enumClass) {
		return Arrays.stream(enumClass.getEnumConstants()).filter(value -> value.getLocalizedName().equals(localizedName)).findFirst().orElseThrow(() -> new EnumConstantNotPresentException(enumClass, localizedName));
	}
}
