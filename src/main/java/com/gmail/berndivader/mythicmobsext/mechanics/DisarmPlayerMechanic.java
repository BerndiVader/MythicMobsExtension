package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.items.WhereEnum;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="disarm",author="BerndiVader")
public class DisarmPlayerMechanic 
extends
SkillMechanic 
implements
ITargetedEntitySkill 
{
	long dt;
	List<WhereEnum>whats;
	
	public DisarmPlayerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.dt=mlc.getLong(new String[] {"duration", "dur" }, 180);

		whats=new ArrayList<>();
		String[]temp=mlc.getString("what","ANY").toUpperCase().split(",");
		int size=temp.length;
		for(int i1=0;i1<size;i1++) {
			whats.add(WhereEnum.getWhere(temp[i1]));
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		HashMap<WhereEnum,ItemStack>stores=new HashMap<>();
		if (!target.isPlayer()) {
			if (target.isLiving()) {
				final LivingEntity e=(LivingEntity)target.getBukkitEntity();
				for(int i1=0;i1<whats.size();i1++) {
					WhereEnum what=whats.get(i1);
					switch(what) {
					case HAND:
						stores.put(WhereEnum.HAND,unequipHand(e));
						break;
					case OFFHAND:
						stores.put(WhereEnum.OFFHAND,unequipOffHand(e));
						break;
					case HELMET:
						stores.put(WhereEnum.HELMET,unequipHelmet(e));
						break;
					case CHESTPLATE:
						stores.put(WhereEnum.CHESTPLATE,unequipChest(e));
						break;
					case LEGGINGS:
						stores.put(WhereEnum.LEGGINGS,unequipLeggings(e));
						break;
					case BOOTS:
						stores.put(WhereEnum.BOOTS,unequipBoots(e));
						break;
					default:
						stores.put(WhereEnum.HAND,unequipHand(e));
						stores.put(WhereEnum.OFFHAND,unequipOffHand(e));
						stores.put(WhereEnum.HELMET,unequipHelmet(e));
						stores.put(WhereEnum.CHESTPLATE,unequipChest(e));
						stores.put(WhereEnum.LEGGINGS,unequipLeggings(e));
						stores.put(WhereEnum.BOOTS,unequipBoots(e));
					}
				}
				new BukkitRunnable() {
					@Override
					public void run() {
						if (e!=null&&!e.isDead()) {
							Iterator<Map.Entry<WhereEnum,ItemStack>>iter=stores.entrySet().iterator();
							while(iter.hasNext()) {
								Map.Entry<WhereEnum,ItemStack>entry=iter.next();
								switch(entry.getKey()) {
								case HELMET:
									equipHelmet(e,entry.getValue());
									break;
								case CHESTPLATE:
									equipChest(e,entry.getValue());
									break;
								case LEGGINGS:
									equipLeggings(e,entry.getValue());
									break;
								case BOOTS:
									equipBoots(e,entry.getValue());
									break;
								case HAND:
									equipHand(e,entry.getValue());
									break;
								case OFFHAND:
									equipOffHand(e,entry.getValue());
									break;
								default:
									break;
								}
							}
						}
					}
				}.runTaskLater(Main.getPlugin(), dt);
			}
		} else {
			Player p=(Player)target.getBukkitEntity();
			int first_empty=p.getInventory().firstEmpty();
			if (first_empty>-1) {
				for(int i1=0;i1<whats.size();i1++) {
					if(p.getInventory().firstEmpty()==-1) break;
					WhereEnum what=whats.get(i1);
					switch(what) {
					case HAND:
						p.getInventory().setItem(first_empty,unequipHand(p));
						break;
					case OFFHAND:
						p.getInventory().setItem(first_empty,unequipOffHand(p));
						break;
					case HELMET:
						p.getInventory().setItem(first_empty,unequipHelmet(p));
						break;
					case CHESTPLATE:
						p.getInventory().setItem(first_empty,unequipChest(p));
						break;
					case LEGGINGS:
						p.getInventory().setItem(first_empty,unequipLeggings(p));
						break;
					case BOOTS:
						p.getInventory().setItem(first_empty,unequipBoots(p));
						break;
					default:
						p.getInventory().setItem(first_empty,unequipHand(p));
						if((first_empty=p.getInventory().firstEmpty())==-1) break;
						p.getInventory().setItem(first_empty,unequipOffHand(p));
						if((first_empty=p.getInventory().firstEmpty())==-1) break;
						p.getInventory().setItem(first_empty,unequipHelmet(p));
						if((first_empty=p.getInventory().firstEmpty())==-1) break;
						p.getInventory().setItem(first_empty,unequipChest(p));
						if((first_empty=p.getInventory().firstEmpty())==-1) break;
						p.getInventory().setItem(first_empty,unequipLeggings(p));
						if((first_empty=p.getInventory().firstEmpty())==-1) break;
						p.getInventory().setItem(first_empty,unequipBoots(p));
					}
				}
			}
		}
		return true;
	}
	
	static ItemStack unequipHand(LivingEntity entity) {
		if(entity.getEquipment().getItemInMainHand()!=null) {
			ItemStack item_stack=entity.getEquipment().getItemInMainHand().clone();
			entity.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
			return item_stack;
		} else {
			return new ItemStack(Material.AIR); 
		}
	}
	static ItemStack unequipOffHand(LivingEntity entity) {
		if(entity.getEquipment().getItemInOffHand()!=null) {
			ItemStack item_stack=entity.getEquipment().getItemInOffHand().clone();
			entity.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
			return item_stack;
		} else {
			return new ItemStack(Material.AIR); 
		}
	}
	static ItemStack unequipHelmet(LivingEntity entity) {
		if(entity.getEquipment().getHelmet()!=null) {
			ItemStack item_stack=entity.getEquipment().getHelmet().clone();
			entity.getEquipment().setHelmet(new ItemStack(Material.AIR));
			return item_stack;
		} else {
			return new ItemStack(Material.AIR); 
		}
	}
	static ItemStack unequipChest(LivingEntity entity) {
		if(entity.getEquipment().getChestplate()!=null) {
			ItemStack item_stack=entity.getEquipment().getChestplate().clone();
			entity.getEquipment().setChestplate(new ItemStack(Material.AIR));
			return item_stack;
		} else {
			return new ItemStack(Material.AIR); 
		}
	}
	static ItemStack unequipLeggings(LivingEntity entity) {
		if(entity.getEquipment().getLeggings()!=null) {
			ItemStack item_stack=entity.getEquipment().getLeggings().clone();
			entity.getEquipment().setLeggings(new ItemStack(Material.AIR));
			return item_stack;
		} else {
			return new ItemStack(Material.AIR); 
		}
	}
	static ItemStack unequipBoots(LivingEntity entity) {
		if(entity.getEquipment().getHelmet()!=null) {
			ItemStack item_stack=entity.getEquipment().getBoots().clone();
			entity.getEquipment().setBoots(new ItemStack(Material.AIR));
			return item_stack;
		} else {
			return new ItemStack(Material.AIR); 
		}
	}
	static void equipHelmet(LivingEntity entity,ItemStack item) {
		entity.getEquipment().setHelmet(item);
	}
	static void equipChest(LivingEntity entity,ItemStack item) {
		entity.getEquipment().setChestplate(item);
	}
	static void equipLeggings(LivingEntity entity,ItemStack item) {
		entity.getEquipment().setLeggings(item);
	}
	static void equipBoots(LivingEntity entity,ItemStack item) {
		entity.getEquipment().setBoots(item);
	}
	static void equipHand(LivingEntity entity,ItemStack item) {
		entity.getEquipment().setItemInMainHand(item);
	}
	static void equipOffHand(LivingEntity entity,ItemStack item) {
		entity.getEquipment().setItemInOffHand(item);
	}
}
