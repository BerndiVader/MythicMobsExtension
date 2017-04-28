package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmCustomDamage extends SkillMechanic implements ITargetedEntitySkill {

	private boolean pk, pi, ia, iabs, ip;
	private double amount;
	
	public mmCustomDamage(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		
		this.ASYNC_SAFE=false;
		this.pk = mlc.getBoolean(new String[]{"preventknockback", "pkb", "pk"}, false);
        this.amount = mlc.getDouble(new String[]{"amount", "a"}, 1.0);
        this.ia = mlc.getBoolean(new String[]{"ignorearmor", "ia", "i"}, false);
        this.pi = mlc.getBoolean(new String[]{"preventimmunity", "pi"}, false);
        this.iabs = mlc.getBoolean(new String[]{"ignoreabs", "iabs"}, false);
        this.ip = mlc.getBoolean(new String[]{"ignorepower", "ip"}, false);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity t) {
        if (t.isDead() || t.getHealth() <= 0.0 || data.getCaster().isUsingDamageSkill()) return false;
        double dmg = this.ip?this.amount * (double)data.getPower():this.amount;
        SkillCaster am = data.getCaster();
        if (am instanceof ActiveMob) ((ActiveMob)am).setLastDamageSkillAmount(dmg);
        LivingEntity source = (LivingEntity)BukkitAdapter.adapt(data.getCaster().getEntity());
        LivingEntity target = (LivingEntity)BukkitAdapter.adapt(t);
        target.setMetadata("MythicDamage", new FixedMetadataValue(Main.getPlugin(),true));
        target.setMetadata("DamageAmount", new FixedMetadataValue(Main.getPlugin(),dmg));
        target.setMetadata("PreventKnockback", new FixedMetadataValue(Main.getPlugin(),this.pk));
        target.setMetadata("IgnoreArmor", new FixedMetadataValue(Main.getPlugin(),this.ia));
        target.setMetadata("IgnoreAbs", new FixedMetadataValue(Main.getPlugin(),this.iabs));
        target.damage(dmg, source);
	    if (this.pi) t.setNoDamageTicks(0);
	    am.setUsingDamageSkill(false);
        return true;
	}
}
