package com.gmail.berndivader.mythicmobsext.placeholders;

import java.util.function.BiFunction;

import io.lumine.xikage.mythicmobs.skills.placeholders.PlaceholderMeta;
import io.lumine.xikage.mythicmobs.skills.placeholders.types.MetaPlaceholder;

public 
class
MetatagPlaceholder
implements
IPlaceHolder
<PlaceholderMeta>
{
	String placeholder_name;
	static final String error="#NOTFOUND>";
	
	public MetatagPlaceholder() {
	}
	
	void register() {
		PlaceholderRegistery.manager.register(placeholder_name,new MetaPlaceholder(this.transformer()));
	}

	@Override
	public BiFunction<PlaceholderMeta, String, String> transformer() {
		return null;
	}

}
