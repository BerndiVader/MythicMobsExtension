package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;

public class mmIsBehindCondition extends mmCustomCondition 
implements IEntityComparisonCondition {
	protected double viewAngle;

	public mmIsBehindCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.viewAngle = CustomSkillStuff.round(mlc.getDouble(new String[]{"view","angle","v"},90.0D),3);
	}

	@Override
	public boolean check(AbstractEntity source, AbstractEntity target) {
		Location s = source.getBukkitEntity().getLocation();
		Location t = target.getBukkitEntity().getLocation();
        double dT = Math.cos(this.viewAngle);
        Vector f = s.getDirection();
        Vector r = s.subtract(t).toVector().normalize();
        return f.dot(r) >= dT;
	}

}
