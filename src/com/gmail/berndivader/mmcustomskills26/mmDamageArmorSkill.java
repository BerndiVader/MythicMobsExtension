package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.in.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;

public class mmDamageArmorSkill extends SkillMechanic implements ITargetedEntitySkill {

	protected String armortype;
	protected int damagevalue;
	protected String signal;
	protected ActiveMob am;
	protected AbstractEntity target;

	public mmDamageArmorSkill(CustomMechanic h, MythicLineConfig mlc) {
		super(h.getConfigLine(), mlc);
		this.ASYNC_SAFE=false;
		this.armortype = mlc.getString(new String[]{"armor","a","armour"}, "all");
		this.damagevalue = mlc.getInteger(new String[]{"damage","dmg","d"}, 1);
		this.signal = mlc.getString(new String[]{"signal","s"}, "");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target == null || !target.isPlayer() || target.isDead()) {return false;}
		SkillCaster caster = data.getCaster();
		if (!(caster instanceof ActiveMob)) return false;
		this.am = (ActiveMob)data.getCaster();
		Player e = (Player)BukkitAdapter.adapt(target);
   		ItemStack armor = null; short dur = 0; boolean broken = false;
   		if (this.armortype.equalsIgnoreCase("helmet") || this.armortype.equalsIgnoreCase("all")) {
        	armor = e.getEquipment().getHelmet();
        	if (armor!=null) {
        		dur = (short)(armor.getDurability()+this.damagevalue); armor.setDurability(dur);
        		if (armor.getDurability() > armor.getType().getMaxDurability()) {
        			e.getInventory().setHelmet(new ItemStack(Material.AIR)); broken = true;
        		}
        	}
        }
        if (this.armortype.equalsIgnoreCase("chest") || this.armortype.equalsIgnoreCase("all")) {
        	armor = e.getEquipment().getChestplate();
        	if (armor!=null) {dur = (short)(armor.getDurability()+this.damagevalue); armor.setDurability(dur);
        		if (armor.getDurability() > armor.getType().getMaxDurability()) {
        			e.getInventory().setChestplate(new ItemStack(Material.AIR)); broken = true;
        		}
        	}
        }
        if (this.armortype.equalsIgnoreCase("leggings") || this.armortype.equalsIgnoreCase("all")) {
        	armor = e.getEquipment().getLeggings();
        	if (armor!=null) {dur = (short)(armor.getDurability()+this.damagevalue); armor.setDurability(dur);
        		if (armor.getDurability() > armor.getType().getMaxDurability()) {
        			e.getInventory().setLeggings(new ItemStack(Material.AIR)); broken = true;
        		}
        	}
        }
        if (this.armortype.equalsIgnoreCase("boots") || this.armortype.equalsIgnoreCase("all")) {
        	armor = e.getEquipment().getBoots();
        	if (armor!=null) {dur = (short)(armor.getDurability()+this.damagevalue); armor.setDurability(dur);
        		if (armor.getDurability() > armor.getType().getMaxDurability()) {
        			e.getInventory().setBoots(new ItemStack(Material.AIR)); broken = true;
        		}
        	}
        }
        if (broken && !this.signal.isEmpty()) {am.signalMob(BukkitAdapter.adapt(e), this.signal);}
        return true;
	}
}
