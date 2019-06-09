package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.RandomDouble;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name="enchant,enchantweaponry",author="BerndiVader")
public 
class 
EnchantWeaponryMechanic 
extends 
SkillMechanic 
implements 
ITargetedEntitySkill {
	boolean set;
	byte what;
	ACTION action;
	
	class Enchant{
		Enchantment enchantment;
		RandomDouble level;
		
		public Enchant(Enchantment ench,String level) {
			this.enchantment=ench;
			this.level=new RandomDouble(level);
		}
	}
	List<Enchant>enchants;
	
	enum WHAT_ENUM{
		MAINHAND(	(byte)0b1),
		OFFHAND(	(byte)0b10),
		HEAD(		(byte)0b100),
		CHEST(		(byte)0b1000),
		LEGS(		(byte)0b10000),
		SHOES(		(byte)0b100000);
		
		private byte what;
		
		WHAT_ENUM(byte value){
			this.what=value;
		}
		
		public byte get() {
			return what;
		}
	}
	
	enum ACTION{
		SET,
		ADD,
		DEL;
	}

	public EnchantWeaponryMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		
		action=ACTION.SET;
		try {
			action=ACTION.valueOf(mlc.getString("action","SET").toUpperCase());
		} catch(Exception ex) {
			Main.logger.warning("Use default because of wrong action in enchant skill "+line);
		}
		
		String arr1[]=mlc.getString("what","").toUpperCase().split(",");
		int length=arr1.length;
		what=0;
		WHAT_ENUM what_enum=null;
		for(int i1=0;i1<length;i1++) {
			try {
				what_enum=WHAT_ENUM.valueOf(arr1[i1]);
			} catch(Exception ex) {
				Main.logger.warning("Ignore what position "+arr1[i1]+" in "+line);
				continue;
			}
			what|=what_enum.get();
		}
		
		enchants=new ArrayList<Enchant>();
		arr1=mlc.getString("enchants","").toUpperCase().split(",");
		length=arr1.length;
		for(int i1=0;i1<length;i1++) {
			String parse[]=arr1[i1].split(":");
			String name=parse[0];
			Enchantment ench;
			String level="1";
			if(parse.length>0) {
				try{
					level=parse[1];
				} catch(Exception ex) {
					Main.logger.warning("Error parsing level in "+line);
					level="1";
				}
			}
			if((ench=Enchantment.getByName(name))!=null) {
				enchants.add(new Enchant(ench,level));
			} else {
				Main.logger.warning("Ignore enchantment "+name+" in "+line);
			}
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if(!target.isLiving()) return false;
		LivingEntity entity=(LivingEntity)target.getBukkitEntity();
		ItemStack stack;
		for(int i1=0;i1<8;i1++) {
			if(((what>>i1)&1)==1) {
				switch(i1) {
					case 0:
						stack=entity.getEquipment().getItemInMainHand();
						if(isValidMaterial(stack)) {
							stack=new ItemStack(stack);
							stack=enchantAction(stack,action,enchants);
							entity.getEquipment().setItemInMainHand(stack.clone());
						}
						break;
					case 1:
						stack=entity.getEquipment().getItemInOffHand();
						if(isValidMaterial(stack)) {
							stack=new ItemStack(stack);
							stack=enchantAction(stack,action,enchants);
							entity.getEquipment().setItemInOffHand(stack.clone());
						}
						break;
					case 2:
						stack=entity.getEquipment().getHelmet();
						if(isValidMaterial(stack)) {
							stack=new ItemStack(stack);
							stack=enchantAction(stack,action,enchants);
							entity.getEquipment().setHelmet(stack.clone());
						}
						break;
					case 3:
						stack=entity.getEquipment().getChestplate();
						if(isValidMaterial(stack)) {
							stack=new ItemStack(stack);
							stack=enchantAction(stack,action,enchants);
							entity.getEquipment().setChestplate(stack.clone());
						}
						break;
					case 4:
						stack=entity.getEquipment().getLeggings();
						if(isValidMaterial(stack)) {
							stack=new ItemStack(stack);
							stack=enchantAction(stack,action,enchants);
							entity.getEquipment().setLeggings(stack.clone());
						}
						break;
					case 5:
						stack=entity.getEquipment().getBoots();
						if(isValidMaterial(stack)) {
							stack=new ItemStack(stack);
							stack=enchantAction(stack,action,enchants);
							entity.getEquipment().setBoots(stack.clone());
						}
						break;
				}
			}
		}
		return true;
	}
	
	static boolean isValidMaterial(ItemStack stack) {
		return stack!=null&&stack.getType()!=Material.AIR;
	}
	
	static ItemStack enchantAction(ItemStack stack,ACTION action,List<Enchant>enchants) {
		int length=enchants.size();
		switch(action) {
			case SET:
				for(int i1=0;i1<length;i1++) {
					Enchantment enchant=enchants.get(i1).enchantment;
					if(stack.containsEnchantment(enchant)) {
						stack.removeEnchantment(enchant);
					} else {
						stack.addUnsafeEnchantment(enchant,(int)enchants.get(i1).level.rollInteger());
					}
				}
				break;
			case ADD:
				for(int i1=0;i1<length;i1++) {
					Enchantment enchant=enchants.get(i1).enchantment;
					stack.addUnsafeEnchantment(enchant,(int)enchants.get(i1).level.rollInteger());
				}
				break;
			case DEL:
				for(int i1=0;i1<length;i1++) {
					Enchantment enchant=enchants.get(i1).enchantment;
					if(stack.containsEnchantment(enchant)) stack.removeEnchantment(enchant);
				}
				break;
		}
		return stack;
	}
	static ItemStack enchantAction_old(ItemStack stack,ACTION action,List<Enchant>enchants) {
		int length=enchants.size();
		switch(action) {
			case SET:
				for(int i1=0;i1<length;i1++) {
					Enchantment enchant=enchants.get(i1).enchantment;
					if(stack.containsEnchantment(enchant)) {
						stack.removeEnchantment(enchant);
					} else if(enchant.canEnchantItem(stack)) {
						stack.addEnchantment(enchant,(int)enchants.get(i1).level.rollInteger());
					}
				}
				break;
			case ADD:
				for(int i1=0;i1<length;i1++) {
					Enchantment enchant=enchants.get(i1).enchantment;
					if(enchant.canEnchantItem(stack)) stack.addEnchantment(enchant,(int)enchants.get(i1).level.rollInteger());
				}
				break;
			case DEL:
				for(int i1=0;i1<length;i1++) {
					Enchantment enchant=enchants.get(i1).enchantment;
					if(stack.containsEnchantment(enchant)) stack.removeEnchantment(enchant);
				}
				break;
		}
		return stack;
	}

}
