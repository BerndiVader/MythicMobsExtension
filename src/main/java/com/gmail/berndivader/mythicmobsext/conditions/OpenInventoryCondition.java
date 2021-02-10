package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.event.inventory.InventoryType;

import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name = "openinventory", author = "nicochulo2001")
public class OpenInventoryCondition extends AbstractCustomCondition implements IEntityCondition {
	
	private String type;
	private String name;

	public OpenInventoryCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);

		type = mlc.getString(new String[] { "type", "t"}, "CRAFTING");
		name = mlc.getString(new String[] { "name", "n"}, null);
	}

	@Override
	public boolean check(AbstractEntity target) {
		if(target.getBukkitEntity() instanceof Player) {
			Player p = ( (Player) target.getBukkitEntity() );
			InventoryView invView = p.getOpenInventory();
			InventoryType invType = invView.getType();
			String invName = invView.getTitle();
			if(invType.toString().equals(type)) {
				if(name != null) {
					return invName.equals(name);
				}
				return true;
			}
			else if(type.equals("BACKBAG")) {
				return invView.getTopInventory() == BackBagHelper.getInventory(target.getUniqueId(), name);
			}
			return false;
		}
		return false;
	}
}
