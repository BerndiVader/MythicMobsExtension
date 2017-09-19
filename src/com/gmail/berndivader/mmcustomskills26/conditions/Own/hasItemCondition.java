package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;
import me.lucko.helper.Scheduler;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class hasItemCondition extends mmCustomCondition
implements
IEntityCondition {
	
	private enum WhereType {
		HAND,
		ARMOR,
		INVENTORY,
		ANY;
		private WhereType() {
		}
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
	
	private class ItemHolding {
		Material material;
		String lore;
		RangedDouble amount;
		boolean matAny;
		WhereType where;
		
		public ItemHolding() {
			this.material=null;
			this.matAny=true;
			this.lore="ANY";
			this.amount=new RangedDouble("0to64");
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
			this.amount=a==null||a.isEmpty()?new RangedDouble("0to64"):new RangedDouble(a);
		}
		public void setWhere(String w) { this.where=WhereType.get(w); }
		public boolean isMaterialAny(){
			return this.matAny;
		}
	}
	
	private HashSet<ItemHolding> holdinglist;
	
	public hasItemCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.holdinglist=new HashSet<>();
		final String tmp=mlc.getString(new String[]{"list","l"},null);
		if (tmp!=null) {
			String[]list=tmp.split(",");
			Arrays.stream(list).forEach(parse-> {
				ItemHolding itemholding=new ItemHolding();
				parse=SkillString.parseMessageSpecialChars(parse);
				if (parse.startsWith("\"")
						&& parse.endsWith("\"")) {
					parse=parse.substring(1, parse.length()-1);
					String[]p=parse.split(";");
					Arrays.stream(p).forEach(parse1->{
						if(parse1.startsWith("material=")) {
							parse1=parse1.substring(9, parse1.length());
							itemholding.setMaterial(parse1);
						} else if(parse1.startsWith("lore=")) {
							parse1=parse1.substring(5, parse1.length());
							itemholding.setLore(parse1);
						} else if(parse1.startsWith("amount=")) {
							parse1=parse1.substring(7, parse1.length());
							itemholding.setAmount(parse1);
						} else if(parse1.startsWith("where=")) {
							parse1=parse1.substring(6,parse1.length());
							itemholding.setWhere(parse1);
						}
					});
				}
				this.holdinglist.add(itemholding);
			});
		}
	}

	@Override
	public boolean check(AbstractEntity t) {
		boolean check=false;
		if (t.isLiving()) {
			final boolean isPlayer=t.isPlayer();
			final LivingEntity target=(LivingEntity)t.getBukkitEntity();
			final CompletableFuture<Boolean>bool=Scheduler.supplyAsync(()-> {
				for(ItemHolding entry:hasItemCondition.this.holdinglist) {
					if (entry.where.equals(WhereType.ANY)||entry.where.equals(WhereType.HAND)) {
						if (checkContent(new ItemStack[]{target.getEquipment().getItemInMainHand()},entry)) {
							return true;
						}
					}
					if (entry.where.equals(WhereType.ANY)||entry.where.equals(WhereType.ARMOR)) {
						if (checkContent(target.getEquipment().getArmorContents(),entry)) {
							return true;
						}
					}
					if (isPlayer&&(entry.where.equals(WhereType.ANY)||entry.where.equals(WhereType.INVENTORY))) {
 						if (checkContent(((Player)target).getInventory().getContents(),entry)) {
							return true;
						}
					}
				}
				return false;
			});
			try {
				check=bool.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}
		return check;
	}

	private static boolean checkContent(ItemStack[]i, ItemHolding entry) {
		return Arrays.stream(i).filter(is -> is != null
				&& (entry.isMaterialAny() || entry.material.equals(is.getType()))
				&& entry.amount.equals(is.getAmount())).anyMatch(is -> entry.lore.equals("ANY") || (is.hasItemMeta() && is.getItemMeta().hasLore() && is.getItemMeta().getLore().contains(entry.lore)));
	}
}
