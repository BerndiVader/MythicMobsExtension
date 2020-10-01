package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name = "asequip", author = "BerndiVader")
public class EquipArmorstandMechanic extends SkillMechanic implements INoTargetSkill {
	Optional<MythicItem> mythicItem;
	PlaceholderString item;
	Material material;
	String[] parse;
	int slot;
	int pos;
	
	public EquipArmorstandMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		parse = mlc.getString(new String[] { "item", "i" }).split(":");
		item = PlaceholderString.of(parse[0]);
		pos = Integer.parseInt(parse[1]);

	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (data.getCaster().getEntity().getBukkitEntity() instanceof ArmorStand) {
			AbstractEntity target = data.getCaster().getEntity();
			
			try {
				this.material = Material.valueOf(item.get(data, target));
			} catch (Exception e) {this.material = Material.DIRT;}
			
			if (parse.length == 2) this.slot = pos;
			mythicItem = Utils.mythicmobs.getItemManager().getItem(item.get(data, target));
			
			ArmorStand as = (ArmorStand) data.getCaster().getEntity().getBukkitEntity();
			ItemStack is = mythicItem.isPresent() ? BukkitAdapter.adapt(mythicItem.get().generateItemStack(1))
					: new ItemStack(this.material, 1);
			
			switch (slot) {
				case 0: {
					as.setItemInHand(is);
					break;
					}
				case 1: {
					as.setBoots(is);
					break;
					}
				case 2: {
					as.setLeggings(is);
					break;
					}
				case 3: {
					as.setChestplate(is);
					break;
					}
				case 4: {
					as.setHelmet(is);
					break;
					}
				case 5: {
					as.getEquipment().setItemInOffHand(is);
					break;
				}
			}
			return true;
		}
		return false;
	}
}
