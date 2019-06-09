package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.bossbars.BossBarHelper;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="createbossbar",author="BerndiVader")
public 
class
CreateBossBar 
extends
SkillMechanic
implements
ITargetedEntitySkill
{
	
	String bar_id;
	
	public CreateBossBar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		bar_id=mlc.getString("id","<target.uuid>");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		if(abstract_entity.isPlayer()) {
			BossBar bar=null;
			String id=Utils.parseMobVariables(bar_id,data,data.getCaster().getEntity(),abstract_entity,null);
			if(BossBarHelper.contains(id)) {
				
			} else {
				bar=Bukkit.createBossBar("bab",BarColor.RED,BarStyle.SEGMENTED_6);
			}
			bar.removeFlag(BarFlag.CREATE_FOG);
			bar.removeFlag(BarFlag.DARKEN_SKY);
			bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
			bar.addPlayer((Player)abstract_entity.getBukkitEntity());
			return true;
		}
		return false;
	}
	
}
