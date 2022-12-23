package net.krlite.plumeconfig.annotation;

import net.krlite.plumeconfig.io.LineBreak;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Comments.class)
public @interface Comment {
	String value() default "";
	LineBreak newLine() default LineBreak.NONE;
}
