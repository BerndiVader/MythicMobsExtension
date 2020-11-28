package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;
import com.gmail.berndivader.mythicmobsext.items.ModdingItem;
import com.gmail.berndivader.mythicmobsext.items.ModdingItem.ACTION;
import com.gmail.berndivader.mythicmobsext.items.WhereEnum;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name="modifyloreline", author="nicochulo2001")
public class ModifyLoreLine extends SkillMechanic implements ITargetedEntitySkill {
	ModdingItem moddingItem;
	String loreLine;
	PlaceholderString loreTextUnprocessed;
	String loreAction;

	public ModifyLoreLine(String skill, MythicLineConfig mlc) {
		super(skill, mlc);

		String slot = mlc.getString(new String[] { "slot", "s"}, "-1");
		String what = mlc.getString(new String[] { "what", "w"}, "HAND").toUpperCase();
		String bag = mlc.getString(new String[] { "bagname", "name", "bag", "b", "n"}, null);
		loreLine = mlc.getString(new String[] { "loreline", "line", "l"}, null);
		String loreTextInput = mlc.getString(new String[] { "loretext", "text", "t"}, null);
		loreAction = mlc.getString(new String[] { "loreaction", "action", "a"}, "SET").toUpperCase();
		WhereEnum where = WhereEnum.valueOf(what);
		ACTION action = ACTION.SET;
		
		if (loreTextInput==null||loreLine==null) return;
		
		loreTextUnprocessed = new PlaceholderString(loreTextInput);
		
		moddingItem = new ModdingItem(where, slot, action, null, null, null, null, null, null, bag, null);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isLiving()) {
			LivingEntity entity = (LivingEntity) target.getBukkitEntity();
			ItemStack itemStack = moddingItem.getItemStackByWhere(data, target, entity);
			ItemMeta itemMeta = itemStack.getItemMeta();
			int line = Integer.parseInt(loreLine);
			
			if (itemMeta.hasLore()) {
				List<String> loreArray = itemMeta.getLore();
				if(loreArray.size() > line) {
					String lore = loreArray.get(line);
					String loreText = loreTextUnprocessed.get(data, target);
					switch (loreAction) {
					case "SET":
						lore = loreText;
						break;
					case "INSERT":
						lore = loreText + lore;
						break;
					case "APPEND":
						lore = lore + loreText;
						break;
					case "DEL":
						lore = lore.replace(loreText, "");
						break;
					case "REMOVE":
						loreArray.remove(line);
						break;
					}
					
					if(!loreAction.equals("REMOVE"))
						loreArray.set(line, lore);
					itemMeta.setLore(loreArray);
					itemStack.setItemMeta(itemMeta); 
				}
			}
			
			if (itemStack!=null)
				itemStack = moddingItem.applyMods(data, target, itemStack);
			
			if(target.getBukkitEntity() instanceof Player)
				((Player)target.getBukkitEntity()).updateInventory();
			return true;
		}
		return false;
	}
}
