package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;

public class mmParsedStanceCondition extends mmCustomCondition
implements
IEntityComparisonCondition {
	
	protected String stance;
	protected boolean compareToSelf;
	protected MobManager mobmanager = Main.getPlugin().getMobManager();

	public mmParsedStanceCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.stance = mlc.getString(new String[]{"stance","s"});
		this.compareToSelf = mlc.getBoolean(new String[]{"compareself","cs"},false);
		if (this.stance!=null 
				&& (this.stance.startsWith("\"") 
						&& this.stance.endsWith("\""))) this.stance = this.stance.substring(1, this.stance.length() - 1);
		this.stance = SkillString.parseMessageSpecialChars(this.stance);
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity ae) {
		ActiveMob am = this.mobmanager.getMythicMobInstance(caster);
		String stance = SkillString.parseMobVariables(this.stance, am, ae, null);
		ActiveMob target = this.compareToSelf ? am : mobmanager.getMythicMobInstance(ae);
		return target.getStance().contains(stance);
	}

}
