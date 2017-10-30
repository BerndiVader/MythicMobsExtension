package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.mmcustomskills26.customprojectiles.BStatueMechanic;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.BlockProjectile;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.EStatueMechanic;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.EffectProjectile;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.EntityProjectile;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.IStatueMechanic;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.ItemProjectile;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.MStatueMechanic;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.MythicOrbitalProjectile;
import com.gmail.berndivader.mmcustomskills26.customprojectiles.MythicProjectile;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class mmCustomSkills26 implements Listener {

	public mmCustomSkills26(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onMMSkillLoad(MythicMechanicLoadEvent e) {
		String mech;
		SkillMechanic skill;
		mech = e.getMechanicName().toLowerCase();
		
		switch (mech) {
		case "damagearmor": {
			skill = new mmDamageArmorSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "unequip": {
			skill = new mmUnequipSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "grenade": {
			skill = new mmGrenadeSkill(e.getContainer(), e.getConfig());
			e.register(skill);
			break;
		} case "setrandomlevel": {
			skill = new mmRandomLevelSkill(e.getContainer(), e.getConfig());
			e.register(skill);
			break;
		} case "steal": {
			skill = new mmStealSkill(e.getContainer(), e.getConfig());
			e.register(skill);
			break;
		} case "dropstolenitems": {
			skill = new mmDropStolenItems(e.getContainer(), e.getConfig());
			e.register(skill);
			break;
		} case "equipskull": {
			skill = new mmEquipFix(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "endereffect": {
			skill = new mmEnderEffect(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "customdamage": {
			skill = new mmCustomDamage(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "cure":
		case "removepotion": {
			skill = new mmRemovePotionEffect(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "feed": {
			skill = new mmFeedSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "oxygen": {
			skill = new mmOxygenSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "customsummon": {
			skill = new mmCustomSummonSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "stun": {
			skill = new mmStunSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "customteleport": {
			skill = new mmCustomTeleportSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "swap": {
			skill = new mmSwapSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "setthreattarget": {
			skill = new mmSetThreatTableTarget(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "clearthreattarget": {
			skill = new mmClearThreatTableTarget(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "customparticleline": {
			skill = new mmCustomParticleLineEffect(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "customparticles": {
			skill = new CustomParticleEffect(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "dropmythicitem": {
			skill = new mmDropItemSkill(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "itemprojectile": {
			skill = new ItemProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "blockprojectile": {
			skill = new BlockProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "entityprojectile": {
			skill = new EntityProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "mythicprojectile": {
			skill = new MythicProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "mythicorbitalprojectile": {
			skill = new MythicOrbitalProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "mythiceffectprojectile": {
			skill = new EffectProjectile(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "setmeta": {
			skill = new SetMetatagMechanic(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "delmeta": {
			skill = new DeleteMetatagMechanic(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "castif": {
			skill = new CastIf(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		} case "parsedstance":
		  case "pstance": {
			skill = new ParsedStance(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		} case "advaipathfinder": {
			skill = new advAIPathFinderSelector(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		} case "advrandomskill": {
			skill = new advRandomSkillMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "renameentity": {
			skill = new RenameEntityMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "parseddisguise": {
			skill=new parsedDisguiseMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "setrotation": {
			skill=new SetRotationMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "setfaction": {
			skill=new SetFactionMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "customvelocity": {
			skill=new CustomVelocityMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "asequip": {
			skill=new EquipArmorstandMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "playerweather": {
			skill=new PlayerWeatherMechanic(e.getContainer().getConfigLine(), e.getConfig());
			e.register(skill);
			break;
		}case "itemfloating": {
			skill=new IStatueMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "blockfloating":{
			skill=new BStatueMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "entityfloating":{
			skill=new EStatueMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "mythicfloating":{
			skill=new MStatueMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "astar":{
			skill=new AStarMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "storecooldown":{
			skill=new StoreCooldownMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}case "extinguish":{
			skill=new ExtinguishMechanic(e.getContainer().getConfigLine(),e.getConfig());
			e.register(skill);
			break;
		}
		}
	}
}
