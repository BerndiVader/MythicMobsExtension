package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.Arrays;
import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Snowman;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "damagearmor", author = "BerndiVader")
public class DamageArmorMechanic extends SkillMechanic implements ITargetedEntitySkill {
	protected HashSet<String> armortype;
	protected int rndMin, rndMax;
	protected String signal;

	public DamageArmorMechanic(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.ASYNC_SAFE = false;
		this.armortype = new HashSet<>();
		this.armortype.addAll(
				Arrays.asList(mlc.getString(new String[] { "armor", "a", "armour" }, "all").toLowerCase().split(",")));
		String[] maybeRnd = mlc.getString(new String[] { "damage", "dmg", "d" }, "1").split("to");
		if (maybeRnd.length > 1) {
			this.rndMin = Integer.valueOf(maybeRnd[0]);
			this.rndMax = Integer.valueOf(maybeRnd[1]);
		} else {
			this.rndMin = Integer.valueOf(maybeRnd[0]);
			this.rndMax = Integer.valueOf(maybeRnd[0]);
		}
		this.signal = mlc.getString(new String[] { "signal", "s" }, null);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target == null || !target.isLiving() || target.isDead()) {
			return false;
		}
		if (target.getBukkitEntity().getType().equals(EntityType.SNOWMAN)) {
			NMSUtils.setSnowmanPumpkin((Snowman) target.getBukkitEntity(), false);
		}
		LivingEntity e = (LivingEntity) BukkitAdapter.adapt(target);
		ItemStack armor;
		short dur;
		boolean broken = false;
		int damagevalue = this.rndMin + (int) (Math.random() * ((this.rndMax - this.rndMin) + 1));
		if (this.armortype.contains("offhand") || this.armortype.contains("all")) {
			// MythicMobs stopped supporting versions below 1.12; removed old version check
			armor = e.getEquipment().getItemInOffHand();
			if (armor != null) {
				dur = (short) (armor.getDurability() + damagevalue);
				armor.setDurability(dur);
				if (armor.getDurability() > armor.getType().getMaxDurability()) {
					e.getEquipment().setItemInOffHand(new ItemStack(Material.AIR));
					broken = true;
				}
			}
		}
		if (this.armortype.contains("hand") || this.armortype.contains("all")) {
			// MythicMobs stopped supporting versions below 1.12 - removed old version check
			armor = e.getEquipment().getItemInMainHand();
			if (armor != null) {
				dur = (short) (armor.getDurability() + damagevalue);
				armor.setDurability(dur);
				if (armor.getDurability() > armor.getType().getMaxDurability()) {
						e.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
					broken = true;
				} else {
					e.getEquipment().setItemInMainHand(new ItemStack(armor));
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
				} else {
					e.getEquipment().setHelmet(new ItemStack(armor));
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
				} else {
					e.getEquipment().setChestplate(new ItemStack(armor));
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
				} else {
					e.getEquipment().setLeggings(new ItemStack(armor));
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
				} else {
					e.getEquipment().setBoots(new ItemStack(armor));
				}
			}
		}
		ActiveMob am = null;
		if (data.getCaster() instanceof ActiveMob)
			am = (ActiveMob) data.getCaster();
		if (am != null && broken && this.signal != null)
			am.signalMob(BukkitAdapter.adapt(e), this.signal);
		return true;
	}
}
