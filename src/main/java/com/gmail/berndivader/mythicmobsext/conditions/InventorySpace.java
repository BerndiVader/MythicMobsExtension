package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name = "inventoryspace", author = "BerndiVader")
public class InventorySpace extends AbstractCustomCondition implements IEntityCondition {

	RangedDouble free_slots;

	public InventorySpace(String line, MythicLineConfig mlc) {
		super(line, mlc);

		free_slots = new RangedDouble(mlc.getString("free", ">0"));
	}

	@Override
	public boolean check(AbstractEntity entity) {
		if (!entity.isPlayer())
			return false;
		Player player = (Player) entity.getBukkitEntity();
		int count = 0;
		for (ListIterator<ItemStack> list = player.getInventory().iterator(); list.hasNext();) {
			ItemStack item_stack = list.next();
			if (item_stack == null || item_stack.getType() == Material.AIR)
				count++;
		}
		return free_slots.equals(count);
	}
}
