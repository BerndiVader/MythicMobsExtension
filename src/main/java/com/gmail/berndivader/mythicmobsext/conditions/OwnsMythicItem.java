package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name = "ownsmythicitem", author = "Seyarada")
public class OwnsMythicItem extends AbstractCustomCondition implements IEntityCondition {
	private ItemStack mythicItem;

	public OwnsMythicItem(String line, MythicLineConfig mlc) {
		super(line, mlc);
		String baseMLC = mlc.getString(new String[] {"item", "i"}, "STONE");
		try {
			Material baseMaterial = Material.valueOf(baseMLC);
			mythicItem = new ItemStack(baseMaterial);
		} catch (Exception e) {
			Optional<MythicItem> t = MythicMobs.inst().getItemManager().getItem(baseMLC);
            ItemStack item = BukkitAdapter.adapt(t.get().generateItemStack(1));
            mythicItem = item;
		}
	}

	@Override
	public boolean check(AbstractEntity target) {
		Player p = ( (Player) target.getBukkitEntity() );
		List<Boolean> b = new ArrayList<Boolean>();
		
		b.add(p.getInventory().containsAtLeast(mythicItem, 1));
		b.add(p.getEquipment().getItemInOffHand().isSimilar(mythicItem));
		
		for(Boolean i:b) {
			if(i) return i;
		}
		
		return false;
	}
}
