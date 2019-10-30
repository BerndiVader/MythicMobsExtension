package com.gmail.berndivader.mythicmobsext.items;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.utils.RandomDouble;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public 
class
ModdingItem
implements
Cloneable
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
	Optional<String>durability;
	Optional<String>bag_name;
	Optional<String>slot;
	Optional<SimpleEntry<String,FixedMetadataValue>>meta_entry;
	
	public ModdingItem(WhereEnum where,String slot,ACTION action,Material material,String[]lore_array,String name,RandomDouble amount,List<Enchant>enchants,String durability,String bag_name,SimpleEntry<String,FixedMetadataValue>meta_entry) {
		this.where=where;
		this.action=action;
		this.material=Optional.ofNullable(material);
		this.lore=Optional.ofNullable(lore_array);
		this.name=Optional.ofNullable(name);
		this.amount=Optional.ofNullable(amount);
		this.enchants=Optional.ofNullable(enchants);
		this.setDurability(durability);
		this.bag_name=Optional.ofNullable(bag_name);
		this.slot=Optional.ofNullable(slot);
		this.meta_entry=Optional.ofNullable(meta_entry);
	}
	
	public void setSlot(String slot) {
		this.slot=Optional.ofNullable(slot);
	}
	
	public int getSlot(SkillMetadata data,AbstractEntity target) {
		return data!=null
				?this.slot.isPresent()?Integer.parseInt(new PlaceholderString(this.slot.get()).get(data,target)):-1
				:this.slot.isPresent()?Integer.parseInt(new PlaceholderString(this.slot.get()).get(target)):-1;
	}
	
	public void setBagName(String bag_name) {
		this.bag_name=Optional.ofNullable(bag_name);
	}
	
	public String getBagName() {
		return bag_name.orElse(null);
	}
	
	public void setDurability(String durability) {
		this.durability=Optional.ofNullable(durability);
	}
	
	public ItemStack applyMods(SkillMetadata data,AbstractEntity target,ItemStack item_stack) {
		switch(action) {
			case SET:
				if(material.isPresent()) item_stack.setType(material.get());
				if(amount.isPresent()) item_stack.setAmount(amount.get().rollInteger());
				if(name.isPresent()) {
					String name=new PlaceholderString(this.name.get()).get(data,target);
					ItemMeta item_meta=item_stack.getItemMeta();
					item_meta.setDisplayName(name);
					item_stack.setItemMeta(item_meta);
				}
				if(lore.isPresent()) {
					String[]lore=this.lore.get().clone();
					int size=lore.length;
					for(int i1=0;i1<size;i1++) {
						String temp=new PlaceholderString(lore[i1]).get(data,target);
						lore[i1]=temp;
					}
					ItemMeta item_meta=item_stack.getItemMeta();
					item_meta.setLore(Arrays.asList(lore));
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
				if(durability.isPresent()) {
					short duration=item_stack.getDurability();
					String durability=new PlaceholderString(this.durability.get()).get(data,target);
					try {
						duration=Short.parseShort(durability);
					} catch (Exception ex) {
						Main.logger.warning("Error while parsing durability for "+durability);
					}
					item_stack.setDurability(duration);
				}
				break;
			case ADD:
				if(amount.isPresent()) item_stack.setAmount(item_stack.getAmount()+amount.get().rollInteger());
				if(name.isPresent()) {
					String name=new PlaceholderString(this.name.get()).get(data,target);
					ItemMeta item_meta=item_stack.getItemMeta();
					String old_name=item_meta.hasDisplayName()?item_meta.getDisplayName():"";
					item_meta.setDisplayName(old_name+name);
					item_stack.setItemMeta(item_meta);
				}
				if(lore.isPresent()) {
					String[]lore=this.lore.get().clone();
					int size=lore.length;
					for(int i1=0;i1<size;i1++) {
						lore[i1]=new PlaceholderString(lore[i1]).get(data,target);
					}
					ItemMeta item_meta=item_stack.getItemMeta();
					if(item_meta.hasLore()) {
						List<String>lores=item_meta.getLore();
						lores.addAll(Arrays.asList(lore));
						item_meta.setLore(lores);
					} else {
						item_meta.setLore(Arrays.asList(lore));
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
				if(durability.isPresent()) {
					short duration=0;
					String durability=new PlaceholderString(this.durability.get()).get(data,target);
					try {
						duration=Short.parseShort(durability);
					} catch (Exception ex) {
						Main.logger.warning("Error while parsing durability for "+durability);
					}
					duration+=item_stack.getDurability();
					item_stack.setDurability(duration);
				}
				break;
			case DEL:
				if(amount.isPresent()) {
					int new_size=amount.get().rollInteger();
					if(new_size>item_stack.getAmount()) new_size=item_stack.getAmount();
					item_stack.setAmount(new_size);
				}
				if(name.isPresent()&&item_stack.getItemMeta().hasDisplayName()) {
					String name=new PlaceholderString(this.name.get()).get(data,target);
					ItemMeta item_meta=item_stack.getItemMeta();
					String new_name=name;
					String old_name=item_meta.getDisplayName();
					item_meta.setDisplayName(old_name.replaceAll(new_name,""));
					item_stack.setItemMeta(item_meta);
				}
				if(lore.isPresent()&&item_stack.getItemMeta().hasLore()) {
					String[]lore=this.lore.get().clone();
					int size=lore.length;
					for(int i1=0;i1<size;i1++) {
						lore[i1]=new PlaceholderString(lore[i1]).get(data,target);
					}
					ItemMeta item_meta=item_stack.getItemMeta();
					String[]lores=lore;
					size=lores.length;
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
				if(durability.isPresent()) {
					short duration=0;
					String durability=new PlaceholderString(this.durability.get()).get(data,target);
					try {
						duration=Short.parseShort(durability);
					} catch (Exception ex) {
						Main.logger.warning("Error while parsing durability for "+durability);
					}
					duration-=item_stack.getDurability();
					item_stack.setDurability(duration);
				}
				break;
		}
		return item_stack;
	}
	
	public ItemStack getItemStackByWhere(SkillMetadata data,AbstractEntity target,LivingEntity entity) {
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
			if(entity instanceof Player&&this.getSlot(data,target)>-1) {
				Player player=(Player)entity;
				int slot=this.getSlot(data,target);
				if((output=player.getInventory().getItem(slot))!=null&&output.getType()!=Material.AIR) return output;
			}
			break;
		case BACKBAG:
			if(BackBagHelper.hasBackBag(entity.getUniqueId())) {
				Inventory inventory=BackBagHelper.getInventory(entity.getUniqueId(),bag_name.isPresent()?bag_name.get():BackBagHelper.str_name);
				if(inventory!=null) {
					int slot=this.getSlot(data,target);
					if(inventory.getSize()>=slot&&slot>-1) {
						if((output=inventory.getItem(slot))!=null&&output.getType()!=Material.AIR) return output;
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
