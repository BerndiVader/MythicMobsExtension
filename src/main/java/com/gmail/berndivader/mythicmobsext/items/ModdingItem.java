package com.gmail.berndivader.mythicmobsext.items;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.berndivader.mythicmobsext.backbags.BackBag;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.utils.RandomDouble;

public 
class
ModdingItem
{
	public enum ACTION{
		SET,
		ADD,
		DEL;
	} ACTION action;
	
	WhereEnum where;
	
	Optional<Material>material;
	Optional<String[]>lore;
	Optional<String>name;
	Optional<RandomDouble>amount;
	Optional<List<Enchant>>enchants;
	Optional<String>bag_name;
	Optional<Integer>slot;
	
	public ModdingItem(WhereEnum where,int slot,ACTION action,Material material,String[]lore_array,String name,RandomDouble amount,List<Enchant>enchants,String bag_name) {
		this.where=where;
		this.setSlot(slot);
		this.action=action;
		this.material=Optional.ofNullable(material);
		this.lore=Optional.ofNullable(lore_array);
		this.name=Optional.ofNullable(name);
		this.amount=Optional.ofNullable(amount);
		this.enchants=Optional.ofNullable(enchants);
		this.bag_name=Optional.ofNullable(bag_name);
	}
	
	public void setSlot(int slot) {
		this.slot=Optional.ofNullable(slot);
	}
	
	public int getSlot() {
		return this.slot.isPresent()?this.slot.get():-1;
	}
	
	public ItemStack applyMods(ItemStack item_stack) {
		switch(action) {
			case SET:
				if(material.isPresent()) item_stack.setType(material.get());
				if(amount.isPresent()) item_stack.setAmount(amount.get().rollInteger());
				if(name.isPresent()) {
					ItemMeta item_meta=item_stack.getItemMeta();
					item_meta.setDisplayName(name.get());
					item_stack.setItemMeta(item_meta);
				}
				if(lore.isPresent()) {
					ItemMeta item_meta=item_stack.getItemMeta();
					item_meta.setLore(Arrays.asList(lore.get()));
					item_stack.setItemMeta(item_meta);
				}
				if(enchants.isPresent()) {
					List<Enchant>enchant_map=enchants.get();
					ListIterator<Enchant>iter=enchant_map.listIterator();
					item_stack.getEnchantments().clear();
					while(iter.hasNext()) {
						Enchant enchant=iter.next();
						item_stack.addUnsafeEnchantment(enchant.enchantment,enchant.level.rollInteger());
					}
				}
				break;
			case ADD:
				if(amount.isPresent()) item_stack.setAmount(item_stack.getAmount()+amount.get().rollInteger());
				if(name.isPresent()) {
					ItemMeta item_meta=item_stack.getItemMeta();
					String old_name=item_meta.hasDisplayName()?item_meta.getDisplayName():"";
					item_meta.setDisplayName(old_name+name.get());
					item_stack.setItemMeta(item_meta);
				}
				if(lore.isPresent()) {
					ItemMeta item_meta=item_stack.getItemMeta();
					if(item_meta.hasLore()) {
						List<String>lores=item_meta.getLore();
						lores.addAll(Arrays.asList(lore.get()));
						item_meta.setLore(lores);
					} else {
						item_meta.setLore(Arrays.asList(lore.get()));
					}
					item_stack.setItemMeta(item_meta);
				}
				if(enchants.isPresent()) {
					List<Enchant>enchant_map=enchants.get();
					ListIterator<Enchant>iter=enchant_map.listIterator();
					while(iter.hasNext()) {
						Enchant enchant=iter.next();
						item_stack.addUnsafeEnchantment(enchant.enchantment,enchant.level.rollInteger());
					}
				}
				break;
			case DEL:
				if(amount.isPresent()) {
					int new_size=amount.get().rollInteger();
					if(new_size>item_stack.getAmount()) new_size=item_stack.getAmount();
					item_stack.setAmount(new_size);
				}
				if(name.isPresent()&&item_stack.getItemMeta().hasDisplayName()) {
					ItemMeta item_meta=item_stack.getItemMeta();
					String new_name=name.get();
					String old_name=item_meta.getDisplayName();
					item_meta.setDisplayName(old_name.replaceAll(new_name,""));
					item_stack.setItemMeta(item_meta);
				}
				if(lore.isPresent()&&item_stack.getItemMeta().hasLore()) {
					ItemMeta item_meta=item_stack.getItemMeta();
					String[]lores=lore.get();
					int size=lores.length;
					List<String>lore_list=item_meta.getLore();
					for(int i1=0;i1<size;i1++) {
						if(lore_list.contains(lores[i1])) lore_list.remove(lores[i1]);
					}
					item_meta.setLore(lore_list);
					item_stack.setItemMeta(item_meta);
				}
				if(enchants.isPresent()) {
					for(Enchant enchant:enchants.get()) {
						if(item_stack.containsEnchantment(enchant.enchantment)) item_stack.removeEnchantment(enchant.enchantment);
					}
					break;
				}
				break;
		}
		return item_stack;
	}
	
	public ItemStack getItemStackByWhere(LivingEntity entity) {
		ItemStack output;
		switch(where) {
		case HAND:
			if((output=entity.getEquipment().getItemInMainHand())!=null&&output.getType()!=Material.AIR) return output;
			break;
		case OFFHAND:
			if((output=entity.getEquipment().getItemInOffHand())!=null&&output.getType()!=Material.AIR) return output;
			break;
		case HELMET:
			if((output=entity.getEquipment().getHelmet())!=null&&output.getType()!=Material.AIR) return output;
			break;
		case CHESTPLATE:
			if((output=entity.getEquipment().getChestplate())!=null&&output.getType()!=Material.AIR) return output;
			break;
		case LEGGINGS:
			if((output=entity.getEquipment().getLeggings())!=null&&output.getType()!=Material.AIR) return output;
			break;
		case BOOTS:
			if((output=entity.getEquipment().getBoots())!=null&&output.getType()!=Material.AIR) return output;
			break;
		case INVENTORY:
			if(entity instanceof Player&&this.getSlot()>-1) {
				Player player=(Player)entity;
				if((output=player.getInventory().getItem(this.getSlot()))!=null&&output.getType()!=Material.AIR) return output;
			}
			break;
		case BACKBAG:
			if(BackBagHelper.hasBackBag(entity.getUniqueId())) {
				BackBag bag=bag_name.isPresent()?new BackBag(entity,bag_name.get()):new BackBag(entity);
				if(bag!=null) {
					if(bag.getSize()>=this.getSlot()&&this.getSlot()>-1) {
						if((output=bag.getInventory().getItem(this.getSlot()))!=null&&output.getType()!=Material.AIR) return output;
					}
				}
			}
			break;
		default:
			break;
		}
		return null;
	}
	
}
