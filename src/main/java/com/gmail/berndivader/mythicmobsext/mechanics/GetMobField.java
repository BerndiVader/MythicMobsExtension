package com.gmail.berndivader.mythicmobsext.mechanics;

import java.lang.reflect.Field;

import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class GetMobField 
extends 
SkillMechanic
implements
ITargetedEntitySkill,
INoTargetSkill {
	String s2;
	boolean bl1;
	Field f1;

	public GetMobField(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		s2=mlc.getString("meta",null);
		bl1=mlc.getBoolean("stance",false);
		try {
			f1=ActiveMob.class.getDeclaredField(mlc.getString("field",""));
			f1.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			Main.logger.warning(e.getLocalizedMessage());
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		ActiveMob am;
		Object o=null;
		if (f1!=null&&(am=Utils.mobmanager.getMythicMobInstance(target))!=null) {
			try {
				if ((o=f1.get(am))==null) return false;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			if (!bl1&&s2!=null) {
				target.getBukkitEntity().setMetadata(s2,new FixedMetadataValue(Main.getPlugin(),o.toString()));
			} else {
				am.setStance(o.toString());
			}
		}
		return true;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return castAtEntity(data,data.getCaster().getEntity());
	}

}
