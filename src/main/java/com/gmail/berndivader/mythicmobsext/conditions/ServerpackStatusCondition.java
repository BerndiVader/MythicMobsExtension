package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name="serverpackstatus",author="BerndiVader")
public 
class 
ServerpackStatusCondition
extends
AbstractCustomCondition
implements
IEntityCondition 
{
	
	List<EnumResourcePackStatus>stats;
	
	public ServerpackStatusCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		stats=new ArrayList<>();
		String[]parse=mlc.getString("status","SUCCESSFULLY_LOADED").toUpperCase().split(",");
		for(String s1:parse) {
			try {
				stats.add(EnumResourcePackStatus.valueOf(s1));
			} catch(Exception ex) {
				Main.logger.warning("Error with serverpack status. Ignore");
			}
		}
	}

	@Override
	public boolean check(AbstractEntity e) {
		Entity entity=e.getBukkitEntity();
		if(entity.hasMetadata(Utils.meta_RESOURCEPACKSTATUS)) {
			EnumResourcePackStatus pack_status=EnumResourcePackStatus.valueOf(entity.getMetadata(Utils.meta_RESOURCEPACKSTATUS).get(0).asString());
			return stats.contains(pack_status);
		}
		return false;
	}
	
	private static enum EnumResourcePackStatus {
		SUCCESSFULLY_LOADED,
		DECLINED,
		FAILED_DOWNLOAD,
		ACCEPTED;

        private EnumResourcePackStatus() {
        }
    }
}
