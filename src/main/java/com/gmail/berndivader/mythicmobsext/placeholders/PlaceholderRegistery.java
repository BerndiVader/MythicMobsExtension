package com.gmail.berndivader.mythicmobsext.placeholders;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicReloadedEvent;
import io.lumine.xikage.mythicmobs.skills.placeholders.Placeholder;
import io.lumine.xikage.mythicmobs.skills.placeholders.PlaceholderManager;

public class PlaceholderRegistery implements Listener {

	static PlaceholderManager manager;

	static {
		manager = Utils.mythicmobs.getPlaceholderManager();
	}

	public PlaceholderRegistery() {
		Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
		register();
	}

	@EventHandler
	void mythicReload(MythicReloadedEvent e) {
		register();
	}

	void register() {

		new CasterMetatagPlaceholder();
		new TriggerMetatagPlaceholder();
		new TargetMetatagPlaceholder();

		manager.register("caster.l.dx", Placeholder.meta((meta, arg) -> {
			return Double.toString(meta.getCaster().getLocation().getX());
		}));
		manager.register("caster.l.dy", Placeholder.meta((meta, arg) -> {
			return Double.toString(meta.getCaster().getLocation().getY());
		}));
		manager.register("caster.l.dz", Placeholder.meta((meta, arg) -> {
			return Double.toString(meta.getCaster().getLocation().getZ());
		}));
		manager.register("caster.holding", Placeholder.meta((meta, arg) -> {
			if (meta.getCaster().getEntity().isLiving()) {
				LivingEntity entity = (LivingEntity) meta.getCaster().getEntity().getBukkitEntity();
				ItemStack item_stack = entity.getEquipment().getItemInMainHand();
				Material material = item_stack == null ? Material.AIR : item_stack.getType();
				return material.name();
			}
			return null;
		}));
		manager.register("caster.nbt", Placeholder.meta((meta, arg) -> {
			return null;
		}));
		manager.register("trigger.l.dx", Placeholder.meta((meta, arg) -> {
			return Double.toString(meta.getTrigger().getLocation().getX());
		}));
		manager.register("trigger.l.dy", Placeholder.meta((meta, arg) -> {
			return Double.toString(meta.getTrigger().getLocation().getY());
		}));
		manager.register("trigger.l.dz", Placeholder.meta((meta, arg) -> {
			return Double.toString(meta.getTrigger().getLocation().getZ());
		}));
		manager.register("trigger.holding", Placeholder.meta((meta, arg) -> {
			if (meta.getTrigger().isLiving()) {
				LivingEntity entity = (LivingEntity) meta.getTrigger().getBukkitEntity();
				ItemStack item_stack = entity.getEquipment().getItemInMainHand();
				Material material = item_stack == null ? Material.AIR : item_stack.getType();
				return material.name();
			}
			return null;
		}));
		manager.register("trigger.nbt", Placeholder.meta((meta, arg) -> {
			return null;
		}));
		manager.register("target.l.dx", Placeholder.entity((entity, arg) -> {
			return Double.toString(entity.getLocation().getX());
		}));
		manager.register("target.l.dy", Placeholder.entity((entity, arg) -> {
			return Double.toString(entity.getLocation().getY());
		}));
		manager.register("target.l.dz", Placeholder.entity((entity, arg) -> {
			return Double.toString(entity.getLocation().getZ());
		}));
		manager.register("target.holding", Placeholder.entity((target, arg) -> {
			if (target.isLiving()) {
				LivingEntity entity = (LivingEntity) target.getBukkitEntity();
				ItemStack item_stack = entity.getEquipment().getItemInMainHand();
				Material material = item_stack == null ? Material.AIR : item_stack.getType();
				return material.name();
			}
			return null;
		}));
		manager.register("target.nbt", Placeholder.entity((target, arg) -> {
			return null;
		}));
		manager.recheckForPlaceholders();

		Main.logger.info("registered mme placeholders");
	}

}
