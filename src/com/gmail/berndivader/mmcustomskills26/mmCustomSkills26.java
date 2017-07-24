package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mmcustomskills26.customprojectiles.BlockProjectile;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.EffectProjectile;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.EntityProjectile;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.ItemProjectile;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.MythicOrbitalProjectile;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.MythicProjectile;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class mmCustomSkills26 implements Listener {
	
	private String MechName;
	private SkillMechanic skill;
	
	@EventHandler
	public void onMMSkillLoad(MythicMechanicLoadEvent e) {
		MechName = e.getMechanicName().toLowerCase();
		MechName = e.getMechanicName().toLowerCase();
		if (MechName.equals("damagearmor")) {
			skill = new mmDamageArmorSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("unequip")) {
			skill = new mmUnequipSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("grenade")) {
			skill = new mmGrenadeSkill(e.getContainer(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("setrandomlevel")) {
			skill = new mmRandomLevelSkill(e.getContainer(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("steal")) {
			skill = new mmStealSkill(e.getContainer(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("dropstolenitems")) {
			skill = new mmDropStolenItems(e.getContainer(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("equipskull")) {
			skill = new mmEquipFix(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("endereffect")) {
			skill = new mmEnderEffect(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("customdamage")) {
			skill = new mmCustomDamage(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("cure") || MechName.equals("removepotion")) {
			skill = new mmRemovePotionEffect(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("feed")) {
			skill = new mmFeedSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("oxygen")) {
			skill = new mmOxygenSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("customsummon")) {
			skill = new mmCustomSummonSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("stun")) {
			skill = new mmStunSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("customteleport")) {
			skill = new mmCustomTeleportSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("swap")) {
			skill = new mmSwapSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("setthreattarget")) {
			skill=new mmSetThreatTableTarget(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("clearthreattarget")) {
			skill=new mmClearThreatTableTarget(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("customparticleline")) {
			skill=new mmCustomParticleLineEffect(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("customparticles")) {
			skill=new CustomParticleEffect(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("dropmythicitem")) {
			skill=new mmDropItemSkill(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("itemprojectile")) {
			skill=new ItemProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("blockprojectile")) {
			skill=new BlockProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("entityprojectile")) {
			skill=new EntityProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("mythicprojectile")) {
			skill=new MythicProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("mythicorbitalprojectile")) {
			skill=new MythicOrbitalProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("mythiceffectprojectile")) {
			skill=new EffectProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("setmeta")) {
			skill=new SetMetatagMechanic(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("delmeta")) {
			skill=new DeleteMetatagMechanic(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			return;
		} if (MechName.equals("castif")) {
			skill=new CastIf(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			return;
		}
	}	
}
