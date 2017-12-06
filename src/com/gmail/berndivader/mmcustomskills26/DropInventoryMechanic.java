package com.gmail.berndivader.mmcustomskills26;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;

public class DropInventoryMechanic 
extends
SkillMechanic 
implements
ITargetedEntitySkill {
	public enum WhereType {
		HAND,
		OFFHAND,
		ARMOR,
		INVENTORY,
		ANY;

		public static WhereType get(String s) {
	        if (s==null) return null;
	        try {
	            return WhereType.valueOf(s.toUpperCase());
	        }
	        catch (Exception ex) {
                return WhereType.ANY;
            }
	    }
	}	
	
	public class ItemHolding {
		Material material;
		String lore;
		int amount;
		boolean matAny;
		WhereType where;

		public ItemHolding() {
			this.material=null;
			this.matAny=true;
			this.lore="ANY";
			this.amount=1;
			this.where=WhereType.ANY;
		}
		
		public void setMaterial(String m) {
			if (m.toUpperCase().equals("ANY")) {
				this.material=null;
				this.matAny=true;
				return;
			}
			Material material;
			try {
				material = Material.valueOf(m.toUpperCase());
			} catch (Exception ex) {
				this.matAny=true;
				return;
			}
			this.matAny=false;
			this.material=material;
		}
		public void setLore(String l) {
			this.lore=(l==null||l.isEmpty()||l.toUpperCase().equals("ANY"))?"ANY":l;
		}
		public void setAmount(int a) {
			this.amount=a;
		}
		public void setWhere(String w) { this.where=WhereType.get(w); }
		public boolean isMaterialAny(){
			return this.matAny;
		}
	}
	
	private ItemHolding holding;
	private int pd,p;
	
	public DropInventoryMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		String tmp=mlc.getString(new String[] {"item"},null);
		this.holding=new ItemHolding();
		if (tmp==null) {
			this.holding.setMaterial("ANY");
			this.holding.setWhere("HAND");
			this.holding.setLore("ANY");
			this.holding.setAmount(1);
		} else {
			if(tmp.startsWith("\"")) tmp=tmp.substring(1,tmp.length()-1);
			tmp=SkillString.parseMessageSpecialChars(tmp);
			String[] p=tmp.split(",");
			for(int a=0;a<p.length;a++) {
				String parse1=p[a];
				if(parse1.startsWith("material=")) {
					parse1=parse1.substring(9, parse1.length());
					this.holding.setMaterial(parse1);
				} else if(parse1.startsWith("lore=")) {
					parse1=parse1.substring(5, parse1.length());
					this.holding.setLore(parse1);
				} else if(parse1.startsWith("amount=")) {
					parse1=parse1.substring(7, parse1.length());
					this.holding.setAmount(Integer.parseInt(parse1));
				} else if(parse1.startsWith("where=")) {
					parse1=parse1.substring(6,parse1.length());
					this.holding.setWhere(parse1);
				}
			}
		}
		this.pd=mlc.getInteger(new String[] {"pickupdelay","pd"},20);
		this.p=mlc.getInteger(new String[] {"pieces","amount","a","p"},1);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isLiving()) {
			final boolean isPlayer=target.isPlayer();
			final LivingEntity e=(LivingEntity)target.getBukkitEntity();
			final Location l=target.getBukkitEntity().getLocation();
			for(int a=0;a<this.p;a++) {
				List<ItemStack> iis=new ArrayList<ItemStack>();
				ItemHolding entry=this.holding;
				if (entry.where.equals(WhereType.ANY)) {
					if (isPlayer) {
						iis.addAll(Arrays.asList(((Player)e).getInventory().getContents()));
					} else {
						iis.addAll(Arrays.asList(e.getEquipment().getArmorContents()));
						iis.add(e.getEquipment().getItemInMainHand());
						iis.add(e.getEquipment().getItemInOffHand());
					}
				} else {
					if (isPlayer&&entry.where.equals(WhereType.INVENTORY)) {
						iis.addAll(Arrays.asList(((Player)e).getInventory().getStorageContents()));
						iis.remove(((Player)e).getEquipment().getItemInMainHand());
					} else if (entry.where.equals(WhereType.HAND)) {
						iis.add(e.getEquipment().getItemInMainHand());
					} else if (entry.where.equals(WhereType.OFFHAND)) {
						iis.add(e.getEquipment().getItemInOffHand());
					} else if (entry.where.equals(WhereType.ARMOR)) {
						iis.addAll(Arrays.asList(e.getEquipment().getArmorContents()));
					}
				}
				checkContentAndDrop(iis,entry,l,this.pd);
			}
		}
		return true;
	}
	
	private static boolean checkContentAndDrop(List<ItemStack> i, ItemHolding entry, Location l,int pd) {
		Collections.shuffle(i);
		for(ListIterator<ItemStack>it=i.listIterator();it.hasNext();) {
			ItemStack is = it.next();
			if (is==null||is.getType().equals(Material.AIR)) continue;
			int a=is.getAmount()<entry.amount?is.getAmount():entry.amount;
			if (entry.isMaterialAny()||entry.material.equals(is.getType())) {
				if (entry.lore.equals("ANY")) {
					ItemStack ti=is.clone();
					ti.setAmount(a);
					Item di=l.getWorld().dropItem(l,ti);
					di.setPickupDelay(pd);
					if (is.getAmount()<=entry.amount) {
						is.setAmount(0);
						is.setType(Material.AIR);
					} else {
						is.setAmount(is.getAmount()-a);
					}
					return true;
				}
				if (is.hasItemMeta()&&is.getItemMeta().hasLore()) {
					for(Iterator<String>it1=is.getItemMeta().getLore().iterator();it1.hasNext();) {
						String ll=it1.next();
						if (ll.contains(entry.lore)) {
							ItemStack ti=is.clone();
							ti.setAmount(a);
							Item di=l.getWorld().dropItem(l,ti);
							di.setPickupDelay(pd);
							if (is.getAmount()<=entry.amount) {
								is.setAmount(0);
								is.setType(Material.AIR);
							} else {
								is.setAmount(is.getAmount()-a);
							}
							return true;
						}
					}
				}

			}
		}
		return false;
	}
}
