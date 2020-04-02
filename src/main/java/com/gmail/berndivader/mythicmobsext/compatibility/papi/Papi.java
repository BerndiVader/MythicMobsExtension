package com.gmail.berndivader.mythicmobsext.compatibility.papi;

import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.Main;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import me.clip.placeholderapi.PlaceholderAPI;

public class Papi {
	final public static String str_PLUGINNAME = "PlaceholderAPI";

	public Papi() {
		Main.logger.info("Using " + str_PLUGINNAME);
	}

	public static String setPlaceHolders(AbstractEntity entity, String text) {
		return PlaceholderAPI.setPlaceholders(entity.isPlayer() ? (Player) entity.getBukkitEntity() : null, text);
	}
}
