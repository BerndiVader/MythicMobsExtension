package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

@ExternalAnnotation(name = "lookatme,looksatme", author = "BerndiVader")
public class LookingAtMeCondition extends AbstractCustomCondition implements IEntityComparisonCondition {
	private double yO;
	private RangedDouble FOV;
	private boolean debug;

	public LookingAtMeCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.yO = mlc.getDouble(new String[] { "yoffset", "y", "yo", "o" }, -0.4D);
		String fov = mlc.getString("fov", ">1.999");
		try {
			Double.parseDouble(fov);
			fov = ">" + fov;
		} catch (NumberFormatException ex) {
			// empty
		}
		this.FOV = new RangedDouble(fov);
		this.debug = mlc.getBoolean("debug", false);
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity target) {
		if (caster.isLiving() && target.isLiving()) {
			Vector Vcaster = ((LivingEntity) caster.getBukkitEntity()).getEyeLocation().toVector();
			Vcaster.setY(Vcaster.getY() + this.yO);
			Vector Vtarget = ((LivingEntity) target.getBukkitEntity()).getEyeLocation().toVector();
			Vector Vdirection = target.getBukkitEntity().getLocation().getDirection();
			Vector delta = Vtarget.subtract(Vcaster);
			double angle = delta.angle(Vdirection);
			if (this.debug) {
				Main.logger.info("fov-ratio:" + Double.toString(angle));
				Main.logger.info("yVecOff:::" + Double.toString(Vcaster.getY()));
			}
			if (this.FOV.equals(angle))
				return true;
		}
		return false;
	}
}
