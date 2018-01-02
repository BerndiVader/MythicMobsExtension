package com.gmail.berndivader.mythicmobsext.conditions.factions;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class HasFactionCondition 
extends
AbstractCustomCondition 
implements 
IEntityCondition {
	protected MobManager mobmanager;
	private String[] faction;

	public HasFactionCondition(String line, MythicLineConfig mlc) {
		super(line,mlc);
		this.mobmanager = Main.getPlugin().getMobManager();
		this.faction = mlc.getString(new String[] { "factions", "faction", "f" }, "ANY").toLowerCase().split(",");
	}

	@Override
	public boolean check(AbstractEntity caster) {
		ActiveMob am = this.mobmanager.getMythicMobInstance(caster);
		if (am==null||!am.hasFaction()) return false;
		if (this.faction.length==1&&this.faction[0].equals("ANY")) return true;
		String s2=am.getFaction();
		for(String s1:this.faction) {
			if (s1.equals(s2)) return true;
		}
		return false;
	}

	public boolean check(AbstractEntity caster, AbstractEntity target) {
		ActiveMob cam = this.mobmanager.getMythicMobInstance(caster);
		ActiveMob tam = this.mobmanager.getMythicMobInstance(target);
		if (cam == null || tam == null || !cam.hasFaction() || !tam.hasFaction())
			return false;
		return cam.getFaction().toLowerCase().equals(tam.getFaction().toLowerCase());
	}
}
