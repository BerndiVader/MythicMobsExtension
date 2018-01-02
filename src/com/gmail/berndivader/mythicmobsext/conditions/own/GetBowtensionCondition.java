package com.gmail.berndivader.mythicmobsext.conditions.own;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.gmail.berndivader.MythicPlayers.PlayerManager;
import com.gmail.berndivader.mythicmobsext.CustomSkillStuff;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class GetBowtensionCondition
extends
AbstractCustomCondition 
implements
IEntityCondition {
	private boolean debug;
	private RangedDouble r1;
	private char c1;
	
	public GetBowtensionCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.debug=mlc.getBoolean("debug",false);
		r(mlc.getString(new String[] {"range","r"},"-1.0"));
		c(line);
	}

	@Override
	public boolean check(AbstractEntity e) {
		double d1=-1;
		if (e.isPlayer()) {
			Player p=(Player)e.getBukkitEntity();
			if (this.c1=='l') {
				if (p.hasMetadata(PlayerManager.meta_BOWTENSIONLAST)) {
					d1=p.getMetadata(PlayerManager.meta_BOWTENSIONLAST).get(0).asDouble();
				}
			} else {
				if (p.getInventory().getItemInMainHand().getType()==Material.BOW) {
					d1=(double)CustomSkillStuff.getBowTension((Player)e.getBukkitEntity());
				}
			}
		}
		if (debug) System.err.println("bowtension time:"+d1);
		return this.r1.equals(d1);
	}
	
	private void c(String s) {
		this.c1=s.charAt(0);
	}
	private void r(String s) {
		this.r1=new RangedDouble(s);
	}

}
