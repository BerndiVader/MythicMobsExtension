package com.gmail.berndivader.mythicmobsext.externals;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ConditionAnnotation {
	public String name();
	public String author();
}

