package com.gmail.berndivader.mythicmobsext.compatibility.nocheatplus;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class HasExemptionCondition extends AbstractCustomCondition implements IEntityCondition {
	CheckType[] types;
	char op;

	public HasExemptionCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		types = new CheckType[0];
		String[] arr1 = mlc.getString(new String[] { "types", "type", "t" }, "ALL").toUpperCase().split(",");
		op = mlc.getString("op", "&").charAt(0);
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
	public boolean check(AbstractEntity e) {
		if (e.isPlayer()) {
			boolean bl1 = true;
			Player p = (Player) e.getBukkitEntity();
			for (int i1 = 0; i1 < types.length; i1++) {
				boolean bl2 = NCPExemptionManager.isExempted(p, types[i1]);
				bl1 = op == '^' ? bl1 ^= bl2 : op == '|' ? bl1 |= bl2 : op == '&' ? bl1 &= bl2 : false;
			}
		}
		return false;
	}

}
