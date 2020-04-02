package com.gmail.berndivader.mythicmobsext.placeholders;

import java.io.IOException;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.compatibilitylib.BukkitSerialization;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

public class PlaceHolderUtils {

	public static String parse_serialized_item(String result, String tag_name) {
		ItemStack item_stack = new ItemStack(Material.AIR);

		try {
			item_stack = BukkitSerialization.itemStackFromBase64(result.substring(Utils.SERIALIZED_ITEM.length()));
		} catch (IOException e) {
			Main.logger.warning(e.getMessage());
		}

		switch (tag_name.toUpperCase()) {
		case "TYPE":
			return item_stack.getType().name();
		case "LORE":
			String lore = "";
			if (item_stack.hasItemMeta()) {
				ItemMeta item_meta = item_stack.getItemMeta();
				if (item_meta.hasLore()) {
					List<String> lores = item_meta.getLore();
					for (int i1 = 0; i1 < lores.size(); i1++) {
						lore = lore + lores.get(i1);
					}
				}
			}
			return lore;
		case "NAME":
			String name = "";
			if (item_stack.hasItemMeta()) {
				ItemMeta item_meta = item_stack.getItemMeta();
				if (item_meta.hasDisplayName()) {
					name = item_meta.getDisplayName();
				}
			}
			return name;
		case "AMOUNT":
			return Integer.toString(item_stack.getAmount());
		}
		return result;
	}

}
