package com.gmail.berndivader.mythicmobsext.placeholders;

import java.util.function.BiFunction;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.skills.placeholders.PlaceholderMeta;

public 
class
TriggerMetatagPlaceholder
extends
MetatagPlaceholder
{
	public TriggerMetatagPlaceholder() {
		placeholder_name="trigger.meta";
		this.register();
	}
	
	@Override
	public BiFunction<PlaceholderMeta, String, String> transformer() {
	    return (meta,tag) -> {
	    	String[] tags=tag.split("\\.");
	    	String tag_name=tags[0];
			String result="<"+placeholder_name+"."+tag_name+error;
			Entity entity=meta.getTrigger().getBukkitEntity();
			if(entity.hasMetadata(tag_name)) result=entity.getMetadata(tag_name).get(0).asString();
			if(result.startsWith(Utils.SERIALIZED_ITEM)) {
				result=PlaceHolderUtils.parse_serialized_item(result,tags.length==2?tags[1]:"type");
			}
	    	return result;
	    };
	}	

}
