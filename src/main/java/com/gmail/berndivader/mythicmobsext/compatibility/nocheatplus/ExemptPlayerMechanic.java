package com.gmail.berndivader.mythicmobsext.compatibility.nocheatplus;

import io.lumine.xikage.mythicmobs.skills.*;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.Main;

import fr.neatmonster.nocheatplus.checks.CheckType;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;

public class ExemptPlayerMechanic extends SkillMechanic implements INoTargetSkill, ITargetedEntitySkill {
	CheckType[] types;

	public ExemptPlayerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		types = new CheckType[0];
		String[] arr1 = mlc.getString(new String[] { "types", "type", "t" }, "ALL").toUpperCase().split(",");
		for (int i1 = 0; i1 < arr1.length; i1++) {
			CheckType c1 = null;
			try {
				c1 = CheckType.valueOf(arr1[i1]);
			} catch (Exception ex) {
				Main.logger.warning("Unable to add NCP CheckType: " + arr1[i1] + " because the tpye is invalid!");
				continue;
			}
			if (c1 == CheckType.ALL) {
				types = new CheckType[] { c1 };
				break;
			}
			types = NoCheatPlusSupport.mkarr(types, new CheckType[] { c1 });
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e) {
		NoCheatPlusSupport.exempt((Player) e.getBukkitEntity(), types);
		return false;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return castAtEntity(data, data.getCaster().getEntity());
	}

}
