package net.krlite.plumeconfig.annotation;

import net.krlite.plumeconfig.io.LineBreak;

import java.lang.annotation.*;

/**
 * Appends a comment to the field.
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Comments.class)
public @interface Comment {
	String value() default "";
	LineBreak end() default LineBreak.NONE;
	boolean beforeField() default false;
}
