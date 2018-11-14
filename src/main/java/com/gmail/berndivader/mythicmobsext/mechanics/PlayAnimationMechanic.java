package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="playanimation",author="BerndiVader")
public class PlayAnimationMechanic 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill
{
	Integer[]ids;

	public PlayAnimationMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=true;
		String[]parse=mlc.getString(new String[] {"id","ids"},"0").split(",");
		ids=new Integer[parse.length];
		for(int i1=0;i1<parse.length;i1++) {
			try {
				int j1=Integer.parseInt(parse[i1]);
				if(j1<0||j1>5) {
					Main.logger.warning("Integer for animation at "+skill+" out of range.");
					j1=0;
				}
				ids[i1]=j1;
			} catch (Exception ex) {
				Main.logger.warning("Invalid Integer for animation at "+skill+" set to default 0");
				ids[i1]=0;
			}
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if(target.isLiving()) {
			LivingEntity e=(LivingEntity)target.getBukkitEntity();
			Volatile.handler.playAnimationPacket(e, this.ids);
		}
		return true;
	}

}
