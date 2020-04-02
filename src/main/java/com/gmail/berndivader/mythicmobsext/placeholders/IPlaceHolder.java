package com.gmail.berndivader.mythicmobsext.placeholders;

import java.util.function.BiFunction;

public interface IPlaceHolder<T> {
	BiFunction<T, String, String> transformer();

}
