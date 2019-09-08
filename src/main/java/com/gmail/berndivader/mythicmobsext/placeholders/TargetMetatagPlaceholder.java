package com.gmail.berndivader.mythicmobsext.placeholders;

import java.util.function.BiFunction;

import org.bukkit.entity.Entity;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;

public 
class
TargetMetatagPlaceholder
extends
EntityPlaceholder
{
	static {
		placeholder_name="target.meta";
	}
	
	public TargetMetatagPlaceholder() {
		super();
	}
	
	@Override
	public BiFunction<AbstractEntity, String, String> transformer() {
	    return (abstract_entity,tag_name) -> {
			String result="<"+placeholder_name+"."+tag_name+error;
			Entity entity=abstract_entity.getBukkitEntity();
			
			if(entity.hasMetadata(tag_name)) result=entity.getMetadata(tag_name).get(0).asString();
	    	return result;
	    };
	}	

}
