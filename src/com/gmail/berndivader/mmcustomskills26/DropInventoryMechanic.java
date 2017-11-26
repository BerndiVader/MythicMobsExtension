package com.gmail.berndivader.mmcustomskills26;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class DropInventoryMechanic 
extends
SkillMechanic 
implements
ITargetedEntitySkill {
	public enum DropWhere {
		GROUND,
		INVENTORY;

		public static DropWhere get(String s) {
	        if (s==null) return null;
	        try {
	            return DropWhere.valueOf(s.toUpperCase());
	        }
	        catch (Exception ex) {
                return DropWhere.GROUND;
            }
	    }
	}	
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
		RangedDouble amount;
		boolean matAny;
		WhereType where;

		public ItemHolding() {
			this.material=null;
			this.matAny=true;
			this.lore="ANY";
			this.amount=new RangedDouble(">0");
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
		public void setAmount(String a) {
			this.amount=a==null||a.isEmpty()?new RangedDouble(">0"):new RangedDouble(a);
		}
		public void setWhere(String w) { this.where=WhereType.get(w); }
		public boolean isMaterialAny(){
			return this.matAny;
		}
	}
	
	private long dt;
	private ItemHolding holding;
	private DropWhere dropwhere;
	
	public DropInventoryMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.dt=mlc.getLong(new String[] {"dt","duration"},180);
		this.dropwhere=DropWhere.get(mlc.getString(new String[] {"dropwhere","where"},DropWhere.INVENTORY.toString()));
		String tmp=mlc.getString(new String[] {"item"},null);
		if (tmp==null) {
			this.holding=new ItemHolding();
			this.holding.setMaterial("ANY");
			this.holding.setWhere("HAND");
			this.holding.setLore("ANY");
			this.holding.setAmount(">0");
		} else {
			tmp=SkillString.parseMessageSpecialChars(tmp);
			String[] p=tmp.split(",");
			Arrays.stream(p).forEach(parse1->{
				if(parse1.startsWith("material=")) {
					parse1=parse1.substring(9, parse1.length());
					this.holding.setMaterial(parse1);
				} else if(parse1.startsWith("lore=")) {
					parse1=parse1.substring(5, parse1.length());
					this.holding.setLore(parse1);
				} else if(parse1.startsWith("amount=")) {
					parse1=parse1.substring(7, parse1.length());
					this.holding.setAmount(parse1);
				} else if(parse1.startsWith("where=")) {
					parse1=parse1.substring(6,parse1.length());
					this.holding.setWhere(parse1);
				}
			});
			
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!target.isPlayer()) {
			if (target.isLiving()) {
				final LivingEntity e=(LivingEntity)target.getBukkitEntity();
				if (!e.getEquipment().getItemInMainHand().getData().getItemType().equals(Material.AIR)) {
					final ItemStack is=e.getEquipment().getItemInMainHand().clone();
					e.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
					new BukkitRunnable() {
						@Override
						public void run() {
							if (e!=null&&!e.isDead()) e.getEquipment().setItemInMainHand(is.clone());
						}
					}.runTaskLater(Main.getPlugin(), dt);
				}
			} else {
				return false;
			}
		} else {
			Player p=(Player)target.getBukkitEntity();
			int es=p.getInventory().firstEmpty();
			if (!p.getEquipment().getItemInMainHand().getData().getItemType().equals(Material.AIR)
					&&es>-1) {
				ItemStack is=p.getEquipment().getItemInMainHand().clone();
				p.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
				p.getInventory().setItem(es, is.clone());
			}
		}
		return true;
	}

}
