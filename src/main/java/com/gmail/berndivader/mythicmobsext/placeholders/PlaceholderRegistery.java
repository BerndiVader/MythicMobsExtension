package com.gmail.berndivader.mythicmobsext.placeholders;

import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.skills.placeholders.Placeholder;
import io.lumine.xikage.mythicmobs.skills.placeholders.PlaceholderManager;

public 
class
PlaceholderRegistery 
{
	
	static PlaceholderManager manager;
	
	static {
		manager=Utils.mythicmobs.getPlaceholderManager();
	}
	
	public PlaceholderRegistery() {
		new CasterMetatagPlaceholder();
		new TriggerMetatagPlaceholder();
		new TargetMetatagPlaceholder();
		
        manager.register("caster.l.dx",Placeholder.meta((meta,arg)-> {
        	return Double.toString(meta.getCaster().getLocation().getX());
        }));
        manager.register("caster.l.dy",Placeholder.meta((meta,arg)-> {
        	return Double.toString(meta.getCaster().getLocation().getY());
        }));
        manager.register("caster.l.dz",Placeholder.meta((meta,arg)-> {
        	return Double.toString(meta.getCaster().getLocation().getZ());
        }));
        manager.register("trigger.l.dx",Placeholder.meta((meta,arg)-> {
        	return Double.toString(meta.getTrigger().getLocation().getX());
        }));
        manager.register("trigger.l.dy",Placeholder.meta((meta,arg)-> {
        	return Double.toString(meta.getTrigger().getLocation().getY());
        }));
        manager.register("trigger.l.dz",Placeholder.meta((meta,arg)-> {
        	return Double.toString(meta.getTrigger().getLocation().getZ());
        }));
        manager.register("target.l.dx",Placeholder.entity((entity,arg)-> {
        	return Double.toString(entity.getLocation().getX());
        }));
        manager.register("target.l.dy",Placeholder.entity((entity,arg)-> {
        	return Double.toString(entity.getLocation().getY());
        }));
        manager.register("target.l.dz",Placeholder.entity((entity,arg)-> {
        	return Double.toString(entity.getLocation().getZ());
        }));
		
		
	}
	

}
