package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;

@ExternalAnnotation(name="isbehind",author="BerndiVader")
public class IsBehindCondition 
extends
AbstractCustomCondition 
implements 
IEntityComparisonCondition {
	private double viewAngle;

	public IsBehindCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.viewAngle=MathUtils.round(mlc.getDouble(new String[]{"view","angle","v"},45.0D),3);
	}

	@Override
	public boolean check(AbstractEntity source, AbstractEntity target) {
		Location s=source.getBukkitEntity().getLocation();
		Location t=target.getBukkitEntity().getLocation();
        double dT=Math.cos(this.viewAngle);
        Vector f=s.getDirection();
        Vector r=s.subtract(t).toVector().normalize();
        return Math.toDegrees(Math.asin(f.dot(r)))>=dT;
	}

}
