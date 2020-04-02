package com.gmail.berndivader.mythicmobsext.backbags.mechanics;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagInventory;
import com.gmail.berndivader.mythicmobsext.compatibilitylib.NMSUtils;
import com.gmail.berndivader.mythicmobsext.items.HoldingItem;
import com.gmail.berndivader.mythicmobsext.items.WhereEnum;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public class RestoreFromBackBag extends SkillMechanic implements INoTargetSkill, ITargetedEntitySkill {
	int to_slot;
	WhereEnum what;
	boolean override;
	PlaceholderString meta_name, from_slot, bag_name;
	HoldingItem holding;

	public RestoreFromBackBag(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE = false;
		from_slot = mlc.getPlaceholderString("fromslot", "-1");
		to_slot = mlc.getInteger("toslot", -1);
		override = mlc.getBoolean("override", true);
		what = WhereEnum.getWhere(mlc.getString("to"));
		bag_name = mlc.getPlaceholderString(new String[] { "title", "name" }, BackBagHelper.str_name);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return castAtEntity(data, data.getCaster().getEntity());
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		int inventory_slot = -1;
		try {
			inventory_slot = Integer.parseInt(from_slot.get(data, abstract_entity));
		} catch (Exception ex) {
			Main.logger.warning("Invalid Integer for slot (reset to default -1) in skillline: " + this.line);
		}
		if (abstract_entity.isLiving() && BackBagHelper.hasBackBag(abstract_entity.getUniqueId())) {
			LivingEntity holder = (LivingEntity) abstract_entity.getBukkitEntity();
			BackBagInventory bag = BackBagHelper.getBagInventory(holder.getUniqueId(),
					bag_name.get(data, abstract_entity));
			holding.parseSlot(data, abstract_entity);
			Inventory inventory = bag.getInventory();
			if (inventory.getSize() >= inventory_slot && inventory_slot > -1) {
				ItemStack old_item = inventory.getItem(inventory_slot);
				if (old_item != null) {
					if (what == WhereEnum.TAG)
						what = WhereEnum.getWhere(NMSUtils.getMetaString(old_item, Utils.meta_BACKBACKTAG));
					switch (what) {
					case SLOT:
					case INVENTORY:
						if (abstract_entity.isPlayer()) {
							Player player = (Player) holder;
							int tmp_slot = to_slot;
							if (to_slot > -1 && player.getInventory().getMaxStackSize() >= to_slot) {
								if (override) {
									player.getInventory().setItem(to_slot, old_item.clone());
									clearItem(old_item);
								} else if (player.getInventory().getItem(to_slot) == null
										|| player.getInventory().getItem(inventory_slot).getType() == Material.AIR) {
									player.getInventory().setItem(to_slot, old_item.clone());
									clearItem(old_item);
								}
							} else if ((tmp_slot = player.getInventory().firstEmpty()) > -1) {
								player.getInventory().setItem(tmp_slot, old_item.clone());
								clearItem(old_item);
							}
						}
						break;
					case HELMET:
						holder.getEquipment().setHelmet(old_item.clone());
						clearItem(old_item);
						break;
					case CHESTPLATE:
						holder.getEquipment().setChestplate(old_item.clone());
						clearItem(old_item);
						break;
					case LEGGINGS:
						holder.getEquipment().setLeggings(old_item.clone());
						clearItem(old_item);
						break;
					case OFFHAND:
						holder.getEquipment().setItemInOffHand(old_item.clone());
						clearItem(old_item);
						break;
					case HAND:
						holder.getEquipment().setItemInMainHand(old_item.clone());
						clearItem(old_item);
						break;
					default:
						break;
					}
				}
			}
		}
		return true;
	}

	static void clearItem(ItemStack item) {
		item.setAmount(0);
		item.setType(Material.AIR);
	}

}
