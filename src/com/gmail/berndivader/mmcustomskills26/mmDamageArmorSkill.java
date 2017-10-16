package com.gmail.berndivader.mmcustomskills26;

import java.util.Arrays;
import java.util.HashSet;

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

public class mmDamageArmorSkill extends SkillMechanic implements ITargetedEntitySkill {

	protected MythicMobs mythicmobs = Main.getPlugin().getMythicMobs();

	private HashSet<String> armortype;
	protected Integer rndMin;
	protected Integer rndMax;
	private String signal;

	public mmDamageArmorSkill(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.ASYNC_SAFE = false;
		this.armortype = new HashSet<>();
		this.armortype.addAll(Arrays.asList(mlc.getString(new String[] { "armor", "a", "armour" }, "all").toLowerCase().split(",")));
		String[] maybeRnd = mlc.getString(new String[] { "damage", "dmg", "d" }, "1").split("to");
		if (maybeRnd.length > 1) {
			this.rndMin = Integer.valueOf(maybeRnd[0]);
			this.rndMax = Integer.valueOf(maybeRnd[1]);
		} else {
			this.rndMin = Integer.valueOf(maybeRnd[0]);
			this.rndMax = Integer.valueOf(maybeRnd[0]);
		}
		this.signal = mlc.getString(new String[] { "signal", "s" }, "");
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		int ver = this.mythicmobs.getMinecraftVersion();
		if (target == null || !target.isLiving() || target.isDead()) {
			return false;
		}
		ActiveMob am = null;
		SkillCaster caster = data.getCaster();
		if (caster instanceof ActiveMob) {
			am = (ActiveMob) data.getCaster();
		}
		LivingEntity e = (LivingEntity) BukkitAdapter.adapt(target);
		ItemStack armor;
		short dur;
		boolean broken = false;
		int damagevalue = this.rndMin + (int) (Math.random() * ((this.rndMax - this.rndMin) + 1));
		if (this.armortype.contains("offhand") || this.armortype.contains("all")) {
			armor = ver >= 9 ? e.getEquipment().getItemInOffHand() : null;
			if (armor != null) {
				dur = (short) (armor.getDurability() + damagevalue);
				armor.setDurability(dur);
				if (armor.getDurability() > armor.getType().getMaxDurability()) {
					e.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
					broken = true;
				}
			}
		}
		if (this.armortype.contains("hand")||this.armortype.contains("all")) {
			armor = ver >= 9 ? e.getEquipment().getItemInMainHand() : e.getEquipment().getItemInHand();
			if (armor != null) {
				dur = (short) (armor.getDurability() + damagevalue);
				armor.setDurability(dur);
				if (armor.getDurability() > armor.getType().getMaxDurability()) {
					if (ver >= 9) {
						e.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
						broken = true;
					} else {
						e.getEquipment().setItemInHand(new ItemStack(Material.AIR));
						broken = true;
					}
				}
			}
		}
		if (this.armortype.contains("helmet") || this.armortype.contains("all")) {
			armor = e.getEquipment().getHelmet();
			if (armor != null) {
				dur = (short) (armor.getDurability() + damagevalue);
				armor.setDurability(dur);
				if (armor.getDurability() > armor.getType().getMaxDurability()) {
					e.getEquipment().setHelmet(new ItemStack(Material.AIR));
					broken = true;
				}
			}
		}
		if (this.armortype.contains("chest") || this.armortype.contains("all")) {
			armor = e.getEquipment().getChestplate();
			if (armor != null) {
				dur = (short) (armor.getDurability() + damagevalue);
				armor.setDurability(dur);
				if (armor.getDurability() > armor.getType().getMaxDurability()) {
					e.getEquipment().setChestplate(new ItemStack(Material.AIR));
					broken = true;
				}
			}
		}
		if (this.armortype.contains("leggings") || this.armortype.contains("all")) {
			armor = e.getEquipment().getLeggings();
			if (armor != null) {
				dur = (short) (armor.getDurability() + damagevalue);
				armor.setDurability(dur);
				if (armor.getDurability() > armor.getType().getMaxDurability()) {
					e.getEquipment().setLeggings(new ItemStack(Material.AIR));
					broken = true;
				}
			}
		}
		if (this.armortype.contains("boots") || this.armortype.contains("all")) {
			armor = e.getEquipment().getBoots();
			if (armor != null) {
				dur = (short) (armor.getDurability() + damagevalue);
				armor.setDurability(dur);
				if (armor.getDurability() > armor.getType().getMaxDurability()) {
					e.getEquipment().setBoots(new ItemStack(Material.AIR));
					broken = true;
				}
			}
		}
		if (am != null && broken && !this.signal.isEmpty()) {
			am.signalMob(BukkitAdapter.adapt(e), this.signal);
		}
		return true;
	}
}
