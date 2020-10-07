package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "villager", author = "Seyarada")
public class VillagerMechanic extends SkillMechanic implements ITargetedEntitySkill {
	private String profession;
	private int level;
	private String type;

	public VillagerMechanic(String line, MythicLineConfig mlc) {
		super(line, mlc);
		profession  = mlc.getString(new String[] { "profession", "p"}, null);
		level  = mlc.getInteger(new String[] { "level", "l"}, -1);
		type  = mlc.getString(new String[] { "type", "t"}, null);

	}
	
	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e) {
		if (e.getBukkitEntity().getType().equals(EntityType.valueOf("VILLAGER"))) {
			Villager v = (Villager) e.getBukkitEntity();
			if (profession!=null) v.setProfession(Villager.Profession.valueOf(profession));
			if (level>0) v.setVillagerLevel(level);
			if (type!=null) v.setVillagerType(Villager.Type.valueOf(type));
			return false;
		}
		return false;
	}

}