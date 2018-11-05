package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="bloodyscreen",author="BerndiVader")
public class BloodyScreenMechanic
extends 
SkillMechanic 
implements
ITargetedEntitySkill 
{
	static String str="mme_bloodyscreen";
	boolean bl1,max_alpha;
	int timer;

	public BloodyScreenMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		bl1=mlc.getBoolean("play",true);
		timer=mlc.getInteger("timer",-1);
		max_alpha=false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity var2) {
		if(var2.isPlayer()) {
			final Player player=(Player)var2.getBukkitEntity();
			if(timer==-1) {
				if(!this.bl1) player.removeMetadata(str, Main.getPlugin());
				Volatile.handler.setWorldborder((Player)var2.getBukkitEntity(),0,this.bl1);
			} else {
				if(!player.hasMetadata(str)) {
					player.setMetadata(str, new FixedMetadataValue(Main.getPlugin(), true));
					Volatile.handler.setWorldborder((Player)var2.getBukkitEntity(),0,true);
					new BukkitRunnable() {
						@Override
						public void run() {
							if(player!=null&&player.isOnline()&&player.hasMetadata(str)) {
								player.removeMetadata(str,Main.getPlugin());
								Volatile.handler.setWorldborder((Player)var2.getBukkitEntity(),0,false);
							}
						}
					}.runTaskLater(Main.getPlugin(),timer);
				}

			}
		}
		return true;
	}

}
