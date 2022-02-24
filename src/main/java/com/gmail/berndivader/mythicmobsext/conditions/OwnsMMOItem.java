package com.gmail.berndivader.mythicmobsext.conditions;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;

import io.lumine.mythic.lib.api.item.NBTItem;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ExternalAnnotation(name = "ownsmmoitem,ownsmi", author = "Ulti")
public class OwnsMMOItem extends AbstractCustomCondition implements IEntityCondition {
	private String Type;
	private String ID;

	public OwnsMMOItem(String line, MythicLineConfig mlc) {
		super(line, mlc);
		Type = mlc.getString("type", "ANY");
		ID = mlc.getString("id", "ANY");
	}

	@Override
	public boolean check(AbstractEntity target) {
		Player player = ((Player) target.getBukkitEntity());
		NBTItem nbtItem = NBTItem.get(player.getEquipment().getItemInMainHand());
		System.out.println("pre-test");
		if(nbtItem.hasType() && nbtItem.getType() == Type) {
			System.out.println("has a type and is of the specific type");
			if(nbtItem.getString("MMOITEMS_ITEM_ID") == ID) {
				System.out.println("the right ID");
				return true;
			}
			System.out.println("Not the right ID");
		}
		System.out.println("not the type or doesn't have a type");
		return false;
	}
}
