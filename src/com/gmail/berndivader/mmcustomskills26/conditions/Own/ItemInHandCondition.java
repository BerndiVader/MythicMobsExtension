package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.util.types.RangedDouble;

public class ItemInHandCondition extends mmCustomCondition
implements
IEntityCondition {
	
	private enum WhereType {
		HAND,
		EQUIPPED,
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
		private HashSet<Material> materials;
		private HashSet<String> lores;
		private RangedDouble amount;
		private boolean matAny;
		private HashSet<WhereType> wheres;
		
		public ItemHolding() {
			this.materials=new HashSet<>();
			this.lores=new HashSet<>();
			this.wheres=new HashSet<>();
			this.matAny=false;
		}
		public HashSet<Material>getMaterials() {
			return this.materials;
		}
		public HashSet<String>getLores() {
			if (this.lores.isEmpty()||this.lores.contains("ANY")) {
				return Stream.of("ANY").collect(Collectors.toCollection(HashSet::new));
			}
			return this.lores;
		}
		public RangedDouble getAmount() {
			if (this.amount!=null) return this.amount;
			return new RangedDouble("0");
		}
		public HashSet<WhereType>getWhere(){
			if (this.wheres.isEmpty()||this.wheres.contains(WhereType.ANY)) {
				return Stream.of(WhereType.ANY).collect(Collectors.toCollection(HashSet::new));
			}
			return this.wheres;
		}
		public void addMaterial(String m) {
			if (m.toUpperCase().equals("ANY")) {
				this.matAny=true;
				return;
			}
			Material material=null;
			try {
				material = Material.valueOf(m.toUpperCase());
			} catch (Exception ex) {
				// empty
			}
			if (material!=null) this.materials.add(material);
		}
		public void addLore(String l) {
			if (l!=null
					&& !l.isEmpty()) {
				if (l.toUpperCase().equals("ANY")) l=l.toUpperCase();
				this.lores.add(l);
			}
		}
		public void addWhere(String a) {
			if (a!=null 
					&& !a.isEmpty()) this.wheres.add(WhereType.get(a));
		}
		public void setAmount(String a) {
			this.amount=new RangedDouble(a);
		}
		public boolean isMaterialAny(){
			return this.matAny||this.materials.isEmpty()?true:false;
		}
	}
	
	final private HashSet<ItemHolding> holdinglist;
	
	public ItemInHandCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.holdinglist=new HashSet<>();
		String tmp=mlc.getString(new String[]{"list","l"},null);
		if (tmp!=null) {
			String[]list=tmp.split(",");
			for(int a=0;a<list.length;a++) {
				ItemHolding itemholding=null;
				String parse=list[a];
				parse=SkillString.parseMessageSpecialChars(parse);
				if (parse.startsWith("\"")
						&& parse.endsWith("\"")) {
					itemholding=new ItemHolding();
					parse=parse.substring(1, parse.length()-1);
					String[]p=parse.split(";");
					for (int b=0;b<p.length;b++) {
						String parse1=p[b];
						int z=0;
						if(parse1.startsWith("material=")) {
							z=1;
							parse1=parse1.substring(9, parse1.length());
						} else if(parse1.startsWith("lore=")) {
							z=2;
							parse1=parse1.substring(5, parse1.length());
						} else if(parse1.startsWith("amount=")) {
							z=3;
							parse1=parse1.substring(7, parse1.length());
						} else if(parse1.startsWith("where=")) {
							z=4;
							parse1=parse1.substring(6,parse1.length());
						}
						String[]p2=parse1.split(",");
						for(int c=0;c<p2.length;c++){
							if (z==1) {
								itemholding.addMaterial(p2[c]);
								continue;
							}
							if (z==2) {
								itemholding.addLore(p2[c]);
								continue;
							}
							if (z==3) {
								itemholding.setAmount(p2[c]);
								continue;
							}
							if (z==4) {
								itemholding.addWhere(p2[c]);
							}
						}
					}
				}
				if (itemholding!=null) this.holdinglist.add(itemholding);
			}
		}
	}

	@Override
	public boolean check(AbstractEntity t) {
		if (t.isLiving()) {
			final boolean isPlayer=t.isPlayer()?true:false;
			final LivingEntity target=(LivingEntity)t.getBukkitEntity();
			
			this.holdinglist.stream().forEach(entry-> {
				boolean match=false;
				if (!entry.isMaterialAny()) {
					entry.getMaterials().parallelStream().forEach(material-> {
						Main.logger.info(material.toString());
					});
				} else {
					Main.logger.info("any");
				}
				Main.logger.info("Lores");
				if (!entry.getLores().contains("ANY")) {
					entry.getLores().parallelStream().forEach(lore-> {
						Main.logger.info(lore);
					});
				} else {
					Main.logger.info("any");
				}
				Main.logger.info("Wheres");
				if (!entry.getWhere().contains(WhereType.ANY)) {
					entry.getWhere().parallelStream().forEach(where-> {
						Main.logger.info(where.toString());
					});
				} else {
					Main.logger.info("any");
				}
				Main.logger.info("Amount");
				Main.logger.info(entry.getAmount().toString());
			});
		}
		return false;
	}

}
