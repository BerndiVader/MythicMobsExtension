package com.gmail.berndivader.mythicmobsext.placeholders;

import java.util.function.BiFunction;

import org.bukkit.entity.Entity;

import io.lumine.xikage.mythicmobs.skills.placeholders.PlaceholderMeta;

public 
class
CasterMetatagPlaceholder
extends
MetatagPlaceholder
{
	static {
		placeholder_name="caster.meta";
	}
	
	public CasterMetatagPlaceholder() {
		super();
	}
	
	@Override
	public BiFunction<PlaceholderMeta, String, String> transformer() {
	    return (meta,tag_name) -> {
			String result="<"+placeholder_name+"."+tag_name+error;
			Entity entity=meta.getCaster().getEntity().getBukkitEntity();
			
			if(entity.hasMetadata(tag_name)) result=entity.getMetadata(tag_name).get(0).asString();
	    	return result;
	    };
	}	

}
