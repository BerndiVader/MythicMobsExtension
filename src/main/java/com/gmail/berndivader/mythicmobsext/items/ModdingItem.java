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

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.backbags.BackBag;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.utils.RandomDouble;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

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
	
	public ModdingItem(WhereEnum where,String slot,ACTION action,Material material,String[]lore_array,String name,RandomDouble amount,List<Enchant>enchants,String durability,String bag_name) {
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
	}
	
	public void setSlot(String slot) {
		this.slot=Optional.ofNullable(slot);
	}
	
	public int getSlot(SkillMetadata data,AbstractEntity target) {
		return data!=null
				?this.slot.isPresent()?Integer.parseInt(Utils.parseMobVariables(this.slot.get(),data,data.getCaster().getEntity(),target,null)):-1
				:this.slot.isPresent()?Integer.parseInt(this.slot.get()):-1;
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
	
	private void parseVars(SkillMetadata data,AbstractEntity target) {
		AbstractEntity caster=data.getCaster().getEntity();
		if(name.isPresent()) {
			this.name=Optional.of(Utils.parseMobVariables(this.name.get(),data,caster,target,null));
		}
		if(lore.isPresent()) {
			String[]lores=lore.get();
			int size=lores.length;
			for(int i1=0;i1<size;i1++) {
				lores[i1]=Utils.parseMobVariables(lores[i1],data,caster,target,null);
			}
			this.lore=Optional.of(lores);
		}
		if(durability.isPresent()) {
			setDurability(Utils.parseMobVariables(this.durability.get(),data,caster,target,null));
		}
		if(bag_name.isPresent()) {
			this.bag_name=Optional.of(Utils.parseMobVariables(this.bag_name.get(),data,caster,target,null));
		}
		if(this.slot.isPresent()) setSlot(Utils.parseMobVariables(this.slot.get(),data,caster,target,null));
	}
	
	public ItemStack applyMods(SkillMetadata data,AbstractEntity target,ItemStack item_stack) {
		boolean use_vars=data!=null;
		AbstractEntity caster=use_vars?data.getCaster().getEntity():null;
		switch(action) {
			case SET:
				if(material.isPresent()) item_stack.setType(material.get());
				if(amount.isPresent()) item_stack.setAmount(amount.get().rollInteger());
				if(name.isPresent()) {
					String name=Utils.parseMobVariables(this.name.get(),data,caster,target,null);
					ItemMeta item_meta=item_stack.getItemMeta();
					item_meta.setDisplayName(name);
					item_stack.setItemMeta(item_meta);
				}
				if(lore.isPresent()) {
					String[]lore=this.lore.get();
					int size=lore.length;
					for(int i1=0;i1<size;i1++) {
						lore[i1]=Utils.parseMobVariables(lore[i1],data,caster,target,null);
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
					String durability=Utils.parseMobVariables(this.durability.get(),data,caster,target,null);
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
					String name=Utils.parseMobVariables(this.name.get(),data,caster,target,null);
					ItemMeta item_meta=item_stack.getItemMeta();
					String old_name=item_meta.hasDisplayName()?item_meta.getDisplayName():"";
					item_meta.setDisplayName(old_name+name);
					item_stack.setItemMeta(item_meta);
				}
				if(lore.isPresent()) {
					String[]lore=this.lore.get();
					int size=lore.length;
					for(int i1=0;i1<size;i1++) {
						lore[i1]=Utils.parseMobVariables(lore[i1],data,caster,target,null);
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
					String durability=Utils.parseMobVariables(this.durability.get(),data,caster,target,null);
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
					String name=Utils.parseMobVariables(this.name.get(),data,caster,target,null);
					ItemMeta item_meta=item_stack.getItemMeta();
					String new_name=name;
					String old_name=item_meta.getDisplayName();
					item_meta.setDisplayName(old_name.replaceAll(new_name,""));
					item_stack.setItemMeta(item_meta);
				}
				if(lore.isPresent()&&item_stack.getItemMeta().hasLore()) {
					String[]lore=this.lore.get();
					int size=lore.length;
					for(int i1=0;i1<size;i1++) {
						lore[i1]=Utils.parseMobVariables(lore[i1],data,caster,target,null);
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
					String durability=Utils.parseMobVariables(this.durability.get(),data,caster,target,null);
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
				BackBag bag=bag_name.isPresent()?new BackBag(entity,bag_name.get()):new BackBag(entity);
				if(bag!=null) {
					int slot=this.getSlot(data,target);
					if(bag.getSize()>=slot&&slot>-1) {
						if((output=bag.getInventory().getItem(slot))!=null&&output.getType()!=Material.AIR) return output;
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
