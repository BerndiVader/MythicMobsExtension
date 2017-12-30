package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import org.bukkit.entity.Entity;

import com.gmail.berndivader.NMS.NMSUtils;
import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class OwnerAliveCondition extends mmCustomCondition 
implements
IEntityCondition {
	
	public OwnerAliveCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
	}

	@Override
	public boolean check(AbstractEntity e) {
		if (Main.mythicmobs.getMobManager().isActiveMob(e)) {
			ActiveMob am=Main.mythicmobs.getMobManager().getMythicMobInstance(e);
			if (am.getOwner().isPresent()) {
				Entity o=NMSUtils.getEntity(e.getBukkitEntity().getWorld(),am.getOwner().get());
				if (o!=null&&!o.isDead()) return true;
			}
		}
		return false;
	}
}
