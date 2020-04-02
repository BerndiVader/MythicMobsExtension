package com.gmail.berndivader.mythicmobsext.externals;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface TargeterAnnotation {
	public String name();

	public String author();
}
