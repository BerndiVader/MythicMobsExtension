package com.gmail.berndivader.mythicmobsext.compatibility.disguise;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.LibsDisguises;

public class LibsDisguisesSupport implements Listener {

	private static LibsDisguisesSupport core;
	private Plugin plugin;
	private static Optional<LibsDisguises> libsDisguise;

	static {
		libsDisguise = Optional
				.ofNullable((LibsDisguises) Bukkit.getServer().getPluginManager().getPlugin("LibsDisguises"));
	}

	public LibsDisguisesSupport() {
		Main.pluginmanager.registerEvents(this, Main.getPlugin());
		Main.logger.info("using LibsDisguise");
	}

	public static LibsDisguisesSupport inst() {
		return core;
	}

	public Plugin plugin() {
		return this.plugin;
	}

	public static boolean isPresent() {
		return libsDisguise.isPresent();
	}

	@EventHandler
	public void onMythicMechanicLoad(MythicMechanicLoadEvent e) {
		switch (e.getMechanicName().toLowerCase()) {
		case "parseddisguise":
		case "parseddisguise_ext": {
			e.register(new ParsedDisguiseMechanic(e.getContainer().getConfigLine(), e.getConfig()));
		}
		}
	}

	public static boolean isDisguised(Entity e) {
		return DisguiseAPI.isDisguised(e);
	}

	public static void setArmor(Entity e, ItemStack[] armorStack) {
		DisguiseAPI.getDisguise(e).getWatcher().setArmor(armorStack);
		;
	}

	public static void setMainHand(Entity e, ItemStack itemStack) {
		DisguiseAPI.getDisguise(e).getWatcher().setItemInMainHand(itemStack);
	}

	public static void setOffHand(Entity e, ItemStack itemStack) {
		DisguiseAPI.getDisguise(e).getWatcher().setItemInOffHand(itemStack);
	}

	public static ItemStack[] getArmor(Entity e) {
		return DisguiseAPI.getDisguise(e).getWatcher().getArmor();
	}

	public static ItemStack getMainHand(Entity e) {
		return DisguiseAPI.getDisguise(e).getWatcher().getItemInMainHand();
	}

	public static ItemStack getOffHand(Entity e) {
		return DisguiseAPI.getDisguise(e).getWatcher().getItemInOffHand();
	}

}
