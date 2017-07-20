package com.gmail.berndivader.mmcustomskills26;

import java.util.Arrays;
import java.util.Iterator;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmRemovePotionEffect extends SkillMechanic implements ITargetedEntitySkill {
	private String[] type;

	public mmRemovePotionEffect(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.type = mlc.getString(new String[]{"potion","p","type","t"},"ALL").toUpperCase().split(",");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		LivingEntity le = (LivingEntity) target.getBukkitEntity();
		if (this.type[0].equals("ALL")) {
			if (target.hasPotionEffect()) {
				Iterator<PotionEffect> i = le.getActivePotionEffects().iterator();
				while (i.hasNext()) {
					le.removePotionEffect(i.next().getType());
				}
			}
		} else {
			try {
				for (String pt : Arrays.asList(this.type)) {
					if (target.hasPotionEffect(pt)) {
						le.removePotionEffect(PotionEffectType.getByName(pt));
					}
				}
			} catch (Exception ex) {
				return false;
			}
		}
		return true;
	}

}
