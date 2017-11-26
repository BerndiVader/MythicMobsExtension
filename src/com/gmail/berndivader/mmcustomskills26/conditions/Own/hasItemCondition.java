package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;

import com.gmail.berndivader.jboolexpr.BooleanExpression;
import com.gmail.berndivader.jboolexpr.MalformedBooleanException;
import com.gmail.berndivader.mmcustomskills26.Main;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class hasItemCondition extends mmCustomCondition
implements
IEntityCondition {

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

	private String conditionLine;
	private LinkedHashSet<ItemHolding> holdinglist;

	public hasItemCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.holdinglist=new LinkedHashSet<>();
		final String tmp=mlc.getString(new String[]{"list","l"},null);
		this.conditionLine=SkillString.parseMessageSpecialChars(tmp);
		if (tmp!=null) {
			String[]list=tmp.split("&&|\\|\\|");
			for (int a=0;a<list.length;a++) {
				String parse=list[a];
				ItemHolding itemholding=new ItemHolding();
				parse=SkillString.parseMessageSpecialChars(parse);
				this.conditionLine=this.conditionLine.replaceFirst(parse,"\\$"+Integer.toString(a));
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
			}
		}
	}

	@Override
	public boolean check(AbstractEntity t) {
		if (t.isLiving()) {
			String c=this.conditionLine;
			boolean bool;
			final boolean isPlayer=t.isPlayer();
			final LivingEntity target=(LivingEntity)t.getBukkitEntity();
			int a=0;
			for(ItemHolding entry:hasItemCondition.this.holdinglist) {
				bool=false;
				if (entry.where.equals(WhereType.ANY)||entry.where.equals(WhereType.HAND)) {
					if (checkContent(new ItemStack[]{target.getEquipment().getItemInMainHand()},entry)) bool=true;
				} else if (entry.where.equals(WhereType.ANY)||entry.where.equals(WhereType.OFFHAND)) {
					if (checkContent(new ItemStack[]{target.getEquipment().getItemInOffHand()},entry)) bool=true;
				} else if (entry.where.equals(WhereType.ANY)||entry.where.equals(WhereType.ARMOR)) {
					if (checkContent(target.getEquipment().getArmorContents(),entry)) bool=true;
				} else if (isPlayer&&(entry.where.equals(WhereType.ANY)||entry.where.equals(WhereType.INVENTORY))) {
					if (checkContent(((Player)target).getInventory().getContents(),entry)) bool=true;
				}
				c=c.replaceFirst("\\$"+Integer.toString(a),Boolean.toString(bool));
				a++;
			}
			BooleanExpression be;
			try {
				be = BooleanExpression.readLR(c);
			} catch (MalformedBooleanException e) {
				Main.logger.warning("Invalid bool expression: "+this.conditionLine);
				return false;
			}
			return be.booleanValue();
		}
		return false;
	}

	private static boolean checkContent(ItemStack[]i, ItemHolding entry) {
		for (int a=0;a<i.length;a++) {
			ItemStack is = i[a];
			if (is==null) continue;
			if ((entry.isMaterialAny()||entry.material.equals(is.getType()))
					&& entry.amount.equals(is.getAmount())) {
				if (entry.lore.equals("ANY")) return true;
				if (is.hasItemMeta()&&is.getItemMeta().hasLore()) {
					for(Iterator<String>it=is.getItemMeta().getLore().iterator();it.hasNext();) {
						String l=it.next();
						if (l.contains(entry.lore)) return true;
					}
				}

			}
		}
		return false;
	}
}
