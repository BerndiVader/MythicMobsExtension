package com.gmail.berndivader.mmcustomskills26;

import java.util.Optional;

import org.bukkit.entity.LivingEntity;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmEquipFix extends SkillMechanic implements ITargetedEntitySkill {
	
	private String itemString;

	public mmEquipFix(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.itemString = mlc.getString(new String[]{"skull","s"});
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		SkillCaster caster = data.getCaster();
		if (!(caster.getEntity().getBukkitEntity() instanceof LivingEntity)) return false;
		getItem((LivingEntity)caster.getEntity().getBukkitEntity(), this.itemString);
		return true;
	}
	
	private boolean getItem(LivingEntity caster, String item) {
        Optional<MythicItem> maybeItem;
        if ((maybeItem = MythicMobs.inst().getItemManager().getItem(itemString)).isPresent()) {
            MythicItem mi = maybeItem.get();
            caster.getEquipment().setHelmet(BukkitAdapter.adapt(mi.generateItemStack(1)));
            return true;
        }
        return false;
	}
}
