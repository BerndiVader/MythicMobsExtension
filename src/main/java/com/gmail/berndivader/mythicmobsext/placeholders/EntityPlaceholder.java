package com.gmail.berndivader.mythicmobsext.placeholders;

import java.util.function.BiFunction;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;

public class EntityPlaceholder implements IPlaceHolder<AbstractEntity> {
	String placeholder_name;
	static final String error = "#NOTFOUND>";

	public EntityPlaceholder() {
	}

	void register() {
		PlaceholderRegistery.manager.register(placeholder_name,
				new io.lumine.xikage.mythicmobs.skills.placeholders.types.EntityPlaceholder(this.transformer()));
	}

	@Override
	public BiFunction<AbstractEntity, String, String> transformer() {
		return null;
	}

}
