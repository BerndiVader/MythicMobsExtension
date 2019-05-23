package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name="hotbar",author="BerndiVader")
public class PlayersHotbarChanged 
extends 
AbstractCustomCondition 
implements 
IEntityCondition 
{
	RangedDouble millis;

	public PlayersHotbarChanged(String line, MythicLineConfig mlc) {
		super(line, mlc);
		millis=new RangedDouble(mlc.getString(new String[] {"milliseconds","millis","ms","m"},"<1"));
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (entity.isPlayer()&&entity.getBukkitEntity().hasMetadata(Utils.meta_SLOTCHANGEDSTAMP)) {
			Player player=(Player)entity.getBukkitEntity();
			double time=System.currentTimeMillis()-player.getMetadata(Utils.meta_SLOTCHANGEDSTAMP).get(0).asDouble();
			return millis.equals(time);
		}
		return false;
	}
}
