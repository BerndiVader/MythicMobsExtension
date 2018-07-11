package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name="blockatcrosshair,crosshairblock",author="BerndiVader")
public class BlockAtCrosshair
extends
AbstractCustomCondition
implements
IEntityCondition {
	int i1;
	HashSet<Material>contains,filters;
	
	public BlockAtCrosshair(String line, MythicLineConfig mlc) {
		super(line, mlc);
		contains=new HashSet<Material>();
		filters=new HashSet<Material>();
		filters.add(Material.AIR);
		i1=Main.getPlugin().getServer().getViewDistance();
		String[]arr1=mlc.getString("material","AIR").toUpperCase().split(",");
		for(int i1=0;i1<arr1.length;i1++) {
			try {
				contains.add(Material.valueOf(arr1[i1]));
			} catch (Exception ex) {
				//
			}
		}
		arr1=mlc.getString("filter","").toUpperCase().split(",");
		for(int i1=0;i1<arr1.length;i1++) {
			try {
				filters.add(Material.valueOf(arr1[i1]));
			} catch (Exception ex) {
				//
			}
		}
	}

	@Override
	public boolean check(AbstractEntity e) {
		if(!e.isPlayer()) return false;
		Material m1=((Player)e.getBukkitEntity()).getTargetBlock(filters,i1).getType();
		return contains.contains(m1);
	}
}
