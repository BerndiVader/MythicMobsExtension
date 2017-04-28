package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;

public class CustomStuff {

	public static void doDamage(SkillCaster am, AbstractEntity t, double damage, 
			boolean ignorearmor, 
			boolean preventKnockback, 
			boolean preventImmunity, 
			boolean ignoreabs) {
        LivingEntity target;
        am.setUsingDamageSkill(true);
        if (am instanceof ActiveMob) ((ActiveMob)am).setLastDamageSkillAmount(damage);
        LivingEntity source = (LivingEntity)BukkitAdapter.adapt(am.getEntity());
        target = (LivingEntity)BukkitAdapter.adapt(t);
        target.setMetadata("IgnoreArmor", new FixedMetadataValue(Main.getPlugin(),ignorearmor));
        target.setMetadata("PreventKnockback", new FixedMetadataValue(Main.getPlugin(),preventKnockback));
        target.setMetadata("IgnoreAbs", new FixedMetadataValue(Main.getPlugin(),ignoreabs));
        target.setMetadata("MythicDamage", new FixedMetadataValue(Main.getPlugin(),true));
        target.setMetadata("DamageAmount", new FixedMetadataValue(Main.getPlugin(),damage));
		target.damage(damage, source);
	    if (preventImmunity) target.setNoDamageTicks(0);
	    am.setUsingDamageSkill(false);
	}
	
    public static boolean createActivePlayer(LivingEntity l, MythicMob mm) {
        ActiveMob am = new ActiveMob(l.getUniqueId(), BukkitAdapter.adapt((Entity)l), mm, 1);
        if (mm.hasFaction()) {
            am.setFaction(mm.getFaction());
            l.setMetadata("Faction", new FixedMetadataValue(MythicMobs.inst(),mm.getFaction()));
        }
        MythicMobs.inst().getMobManager().registerActiveMob(am);
        new TriggeredSkill(SkillTrigger.SPAWN, am, null);
        l.setMetadata("MythicPlayer", new FixedMetadataValue(Main.getPlugin(),mm.getInternalName()));
        return true;
    }
}
