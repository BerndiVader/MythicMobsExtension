package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class GetBowtensionCondition
extends
mmCustomCondition 
implements
IEntityCondition {
	private boolean debug;
	private RangedDouble r1;
	
	public GetBowtensionCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.debug=mlc.getBoolean("debug",false);
		r(mlc.getString(new String[] {"range","r"},"-1.0"));
	}

	@Override
	public boolean check(AbstractEntity e) {
		double d1=-1;
		if (e.isPlayer()) {
			Player p=(Player)e.getBukkitEntity();
			if (p.getInventory().getItemInMainHand().getType()==Material.BOW) {
				d1=(double)Main.getPlugin().getVolatileHandler().getBowTension((Player)e.getBukkitEntity());
			}
		}
		if (debug) System.err.println("bowtension time:"+d1);
		return this.r1.equals(d1);
	}
	
	private void r(String s) {
		this.r1=new RangedDouble(s);
	}

}
