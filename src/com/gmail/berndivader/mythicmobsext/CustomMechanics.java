package com.gmail.berndivader.mythicmobsext;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.mythicmobsext.customprojectiles.*;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;

public class CustomMechanics implements Listener {

	public CustomMechanics(Plugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onMMSkillLoad(MythicMechanicLoadEvent e) {
		String mech;
		SkillMechanic skill;
		mech = e.getMechanicName().toLowerCase();
		
		switch (mech) {
		case "advaipathfinder":
		case "custompathfinder": {
				skill = new AdvAIPathFinderSelector(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "asequip": {
				skill=new EquipArmorstandMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "astar": {
				skill=new AStarMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "blockfloating": {
				skill=new BStatueMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "blockprojectile": {
				skill = new BlockProjectile(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "bloodyscreen": {
				skill = new BloodyScreen(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "castif": {
				skill = new CastIf(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "clearthreattarget": {
				skill = new ClearThreatTableMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "closeinventory": {
				skill=new CloseInventoryMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "cure":
			case "removepotion": {
				skill = new RemovePotionEffectMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "customdamage": {
				skill = new CustomDamage(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "customparticleline": {
				skill = new CustomParticleLineEffect(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "customparticles": {
				skill = new CustomParticleEffect(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "customrandomskill": 
			case "advrandomskill": {
				skill = new CustomRandomSkillMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "customsummon": {
				skill = new CustomSummonMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "customteleport": {
				skill = new CustomTeleportMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "customvelocity": {
				skill=new CustomVelocityMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "damagearmor": {
				skill = new DamageArmorMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "delmeta": {
				skill = new DeleteMetatagMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "disarm": {
				skill=new DisarmPlayerMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "dropcombat": {
				skill=new DropCombatMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "dropinventory": {
				skill=new DropInventoryMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "dropmythicitem": {
				skill = new DropMythicItemMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "dropstolenitems": {
				skill = new DropStolenItemsMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "endereffect": {
				skill = new EnderDragonDeathEffect(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "entityfloating": {
				skill=new EStatueMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "entitygoggle":
			case "entitygoggleat":
			case "entitylookin": {
				skill=new EntityGoogleMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "entityprojectile": {
				skill = new EntityProjectile(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "equipskull": {
				skill = new EquipFixMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "extinguish": {
				skill=new ExtinguishMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "fakedeath": {
				skill=new FakeEntityDeathMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "feed": {
				skill = new FeedMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "forcespectate": {
				skill=new ForceSpectateMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "grenade": {
				skill = new GrenadeMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "itemfloating": {
				skill=new IStatueMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "itemprojectile": {
				skill = new ItemProjectile(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "modifyarrows": {
				skill=new ModifyArrowsMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "mythiceffectprojectile": {
				skill = new EffectProjectile(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "mythicfloating": {
				skill=new MStatueMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "mythicorbitalprojectile": {
				skill = new MythicOrbitalProjectile(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "mythicprojectile": {
				skill = new MythicProjectile(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "oxygen": {
				skill = new OxygenMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "parseddisguise": {
				skill=new ParsedDisguiseMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "nodamageticks": {
				skill=new NoDamageTicksMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "parsedstance":
		  	case "pstance": {
				skill = new ParsedStance(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "playcredits": {
				skill=new PlayCreditsMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "playergoggle":
			case "playergoggleat": {
				skill=new PlayerGoggleMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "playerspin": {
				skill=new PlayerSpinMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "playerweather": {
				skill=new PlayerWeatherMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "playerzoom": {
				skill=new PlayerZoomMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "playloading": {
				skill=new PlayLoadingMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "renameentity": {
				skill = new RenameEntityMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "setfaction": {
				skill=new SetFactionMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "setnbt": {
				skill=new SetNbt(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "setitemcooldown": {
				skill=new SetItemCooldown(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			}
			case "setrandomlevel": {
				skill = new SetLevelMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "setmeta": {
				skill = new SetMetatagMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "setmobhealth": {
				skill=new SetMobHealthMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "setrotation": {
				skill=new SetRotationMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "setspeed":
			case "randomspeed": {
				skill = new SetSpeedMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "setthreattarget": {
				skill = new SetThreatTableTargetMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "steal": {
				skill = new StealMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "storecooldown": {
				skill=new StoreCooldownMechanic(e.getContainer().getConfigLine(),e.getConfig());
				e.register(skill);
				break;
			} case "stun": {
				skill = new StunMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "swap": {
				skill = new SwapMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			} case "unequip": {
				skill = new UnequipMechanic(e.getContainer().getConfigLine(), e.getConfig());
				e.register(skill);
				break;
			}
		}
	}
}
