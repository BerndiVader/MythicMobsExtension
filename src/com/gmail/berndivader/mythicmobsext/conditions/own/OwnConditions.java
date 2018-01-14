package com.gmail.berndivader.mythicmobsext.conditions.own;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;

public class OwnConditions implements Listener {

	public OwnConditions() {
		Bukkit.getServer().getPluginManager().registerEvents(this, Main.getPlugin());
		Bukkit.getLogger().info("Register CustomConditions");
	}

	@EventHandler
	public void onMythicMobsConditionsLoadEvent(MythicConditionLoadEvent e) {
		String conditionName = e.getConditionName().toLowerCase();
		switch (conditionName) {
			case "arrowcount": {
				SkillCondition c=new ArrowOnEntityCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "behind": {
				SkillCondition c = new IsBehindCondition(e.getConfig().getLine(), e.getConfig());
				e.register(c);
				break;
			} case "biomefix": {
				SkillCondition c = new BiomeFixCondition(e.getConfig().getLine(), e.getConfig());
				e.register(c);
				break;
			} case "cmpnbt": {
				SkillCondition c=new CompareNBTCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "crouching": 
			case "running":
			case "disguised":
			case "sleeping":
			case "jumping": {
				SkillCondition c=new PlayerBooleanConditions(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "damageable":
			case "attackable": {
				SkillCondition c = new IsAttackableCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "entitiesinradius":
			case "eir":
			case "leir":
			case "livingentitiesinradius":
			case "pir":
			case "playersinradius":{
				SkillCondition c=new EntitiesInRadiusCondition(e.getConfig().getLine(), e.getConfig());
				e.register(c);
				break;
			} case "facingdirection": {
				SkillCondition c = new FacingDirectionCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "getbowtension":
			case "bowtension":
			case "lastbowtension":
			case "tension": {
				SkillCondition c=new GetBowtensionCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "getindicator":
			case "damageindicator":
			case "indicator": {
				SkillCondition c=new GetDamageIndicator(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "lastindicator":
			case "lastdamageindicator": {
				SkillCondition c=new GetLastDamageIndicator(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "hasmeta":
			case "hasmetasimple": {
				SkillCondition c = new HasMetaTagCondition(e.getConfig().getLine(), e.getConfig());
				e.register(c);
				break;
			} case "hasspawner": {
				SkillCondition c=new HasMythicSpawnerCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "hastarget": {
				SkillCondition c = new HasTargetCondition(e.getConfig().getLine(), e.getConfig());
				e.register(c);
				break;
			} case "health": {
				SkillCondition c = new Healthcondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "infaction": {
				SkillCondition c = new InFactionCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "infront": {
				SkillCondition c = new InFrontCondition(e.getConfig().getLine(), e.getConfig());
				e.register(c);
				break;
			} case "inmotion": {
				SkillCondition c = new InMotionCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "isburning": {
				SkillCondition c=new IsBurningCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "isgoggling": {
				SkillCondition c=new IsGogglingCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "ispresent": {
				SkillCondition c=new IsTargetPresentCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "isspinning": {
				SkillCondition c=new IsSpinningCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "isstunned":
			case "stunned": {
				SkillCondition c = new IsStunnedCondition(e.getConfig().getLine(), e.getConfig());
				e.register(c);
				break;
			} case "isvehicle": {
				SkillCondition c=new IsVehicleCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "hasvehicle": {
				SkillCondition c=new HasVehicleCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "lastdamagecause": {
				SkillCondition c = new LastDamageCauseCondition(e.getConfig().getLine(), e.getConfig());
				e.register(c);
				break;
			} case "lookatme":
			case "looksatme": {
				SkillCondition c = new LookingAtMeCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "mir":
			case "mobsinradius": {
				SkillCondition c = new MobsInRadiusCondition(e.getConfig().getLine(), e.getConfig());
				e.register(c);
				break;
			} case "movespeed": {
				SkillCondition c=new MovementSpeedCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "oncooldown": {
				SkillCondition c=new OnCooldownCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "onsolidblock":
			case "insolidblock": {
				SkillCondition c=new SolidBlockConditions(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "owneralive": {
				SkillCondition c=new OwnerAliveCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "ownsitem":
			case "ownsitemsimple":
			case "iteminhand": {
				SkillCondition c = new HasItemCondition(e.getConfig().getLine(), e.getConfig());
				e.register(c);
				break;
			} case "parsedstance":
			case "pstance": {
				SkillCondition c = new ParsedStanceCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "playertime": {
				SkillCondition c=new PlayerTimeCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "playerweather": {
				SkillCondition c=new PlayerWeatherCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "relativedirection": {
				SkillCondition c = new DirectionalDamageCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "samefaction": {
				SkillCondition c = new SameFactionCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "samespawner": {
				SkillCondition c=new SameMythicSpawnerCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "sameworld": {
				SkillCondition c=new SameWorldCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "testfor": {
				SkillCondition c=new TestForCondition(e.getConfig().getLine(),e.getConfig());
				e.register(c);
				break;
			} case "vdistance": {
				SkillCondition c = new VerticalDistanceCondition(e.getConfig().getLine(), e.getConfig());
				e.register(c);
				break;
			}
		}
	}
}
