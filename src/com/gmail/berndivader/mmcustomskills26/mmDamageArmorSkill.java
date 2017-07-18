package com.gmail.berndivader.mmcustomskills26;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;

public class mmDamageArmorSkill extends SkillMechanic implements ITargetedEntitySkill {

	protected String[] armortype;
	protected int rndMin,rndMax;
	protected String signal;

	public mmDamageArmorSkill(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.ASYNC_SAFE=false;
		this.armortype = mlc.getString(new String[]{"armor","a","armour"}, "all").toLowerCase().split(",");
		String[] maybeRnd = mlc.getString(new String[]{"damage","dmg","d"}, "1").split("to");
		if (maybeRnd.length>1) {
			this.rndMin = Integer.valueOf(maybeRnd[0]);
			this.rndMax = Integer.valueOf(maybeRnd[1]);
		} else {
			this.rndMin = Integer.valueOf(maybeRnd[0]);
			this.rndMax = Integer.valueOf(maybeRnd[0]);
		}
		this.signal = mlc.getString(new String[]{"signal","s"}, "");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target == null || !target.isLiving() || target.isDead()) {return false;}
		ActiveMob am = null;
		SkillCaster caster = data.getCaster();
		if (caster instanceof ActiveMob) {
			am = (ActiveMob)data.getCaster();
		}
		LivingEntity e = (LivingEntity)BukkitAdapter.adapt(target);
   		ItemStack armor = null; short dur = 0; boolean broken = false;
   		int damagevalue = (int)this.rndMin + (int)(Math.random() * ((this.rndMax - this.rndMin) + 1));
   		if (Arrays.asList(this.armortype).contains("offhand") || Arrays.asList(this.armortype).contains("all")) {
   			armor = MythicMobs.inst().getMinecraftVersion()>=9?e.getEquipment().getItemInOffHand():null;
        	if (armor!=null) {
        		dur = (short)(armor.getDurability()+damagevalue); armor.setDurability(dur);
        		if (armor.getDurability() > armor.getType().getMaxDurability()) {
        			e.getEquipment().setItemInOffHand(new ItemStack(Material.AIR)); broken = true;
        		}
        	}
        }
   		if (Arrays.asList(this.armortype).contains("hand") || Arrays.asList(this.armortype).contains("all")) {
   			armor = MythicMobs.inst().getMinecraftVersion()>=9?e.getEquipment().getItemInMainHand():e.getEquipment().getItemInHand();
        	if (armor!=null) {
        		dur = (short)(armor.getDurability()+damagevalue); armor.setDurability(dur);
        		if (armor.getDurability() > armor.getType().getMaxDurability()) {
        			if (MythicMobs.inst().getMinecraftVersion()>=9) {
            			e.getEquipment().setItemInMainHand(new ItemStack(Material.AIR)); broken = true;
        			} else {
            			e.getEquipment().setItemInHand(new ItemStack(Material.AIR)); broken = true;
        			}
        		}
        	}
        }
   		if (Arrays.asList(this.armortype).contains("helmet") || Arrays.asList(this.armortype).contains("all")) {
        	armor = e.getEquipment().getHelmet();
        	if (armor!=null) {
        		dur = (short)(armor.getDurability()+damagevalue); armor.setDurability(dur);
        		if (armor.getDurability() > armor.getType().getMaxDurability()) {
        			e.getEquipment().setHelmet(new ItemStack(Material.AIR)); broken = true;
        		}
        	}
        }
   		if (Arrays.asList(this.armortype).contains("chest") || Arrays.asList(this.armortype).contains("all")) {
        	armor = e.getEquipment().getChestplate();
        	if (armor!=null) {dur = (short)(armor.getDurability()+damagevalue); armor.setDurability(dur);
        		if (armor.getDurability() > armor.getType().getMaxDurability()) {
        			e.getEquipment().setChestplate(new ItemStack(Material.AIR)); broken = true;
        		}
        	}
        }
   		if (Arrays.asList(this.armortype).contains("leggings") || Arrays.asList(this.armortype).contains("all")) {
        	armor = e.getEquipment().getLeggings();
        	if (armor!=null) {dur = (short)(armor.getDurability()+damagevalue); armor.setDurability(dur);
        		if (armor.getDurability() > armor.getType().getMaxDurability()) {
        			e.getEquipment().setLeggings(new ItemStack(Material.AIR)); broken = true;
        		}
        	}
        }
   		if (Arrays.asList(this.armortype).contains("boots") || Arrays.asList(this.armortype).contains("all")) {
        	armor = e.getEquipment().getBoots();
        	if (armor!=null) {dur = (short)(armor.getDurability()+damagevalue); armor.setDurability(dur);
        		if (armor.getDurability() > armor.getType().getMaxDurability()) {
        			e.getEquipment().setBoots(new ItemStack(Material.AIR)); broken = true;
        		}
        	}
        }
        if (am!=null && broken && !this.signal.isEmpty()) {am.signalMob(BukkitAdapter.adapt(e), this.signal);}
        return true;
	}
}
