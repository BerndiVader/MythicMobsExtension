package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.items.ModdingItem;
import com.gmail.berndivader.mythicmobsext.items.ModdingItem.ACTION;
import com.gmail.berndivader.mythicmobsext.items.WhereEnum;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "modifyloreline", author = "nicochulo2001")
public class ModifyLoreLine extends SkillMechanic implements ITargetedEntitySkill {
	ModdingItem modding_item;
	String loreline;
	String loretext;
	String loreaction;

	public ModifyLoreLine(String skill, MythicLineConfig mlc) {
		super(skill, mlc);

		WhereEnum where = Utils.enum_lookup(WhereEnum.class, mlc.getString("what", "HAND").toUpperCase());
		ACTION action = Utils.enum_lookup(ACTION.class, "SET");
		String bagname = null;

		String slot = mlc.getString("slot", "-7331");
		String temp = mlc.getString("bagname");
		loreline = mlc.getString("loreline");
		loretext = mlc.getString("loretext");
		loreaction = mlc.getString("action", "SET").toUpperCase();
		
		if ((temp = mlc.getString("bagname")) != null)
			bagname = temp;
		
		modding_item = new ModdingItem(where, slot, action, null, null, null, null, null, null, bagname,
				null);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.isLiving()) {
			LivingEntity entity = (LivingEntity) target.getBukkitEntity();
			ItemStack item_stack = modding_item.getItemStackByWhere(data, target, entity);
			ItemMeta item_meta = item_stack.getItemMeta();
			int lorelinenum = Integer.parseInt(loreline);
			if (item_meta.hasLore()) {
				List<String> lore_array = item_meta.getLore();
				if(lore_array.size() > lorelinenum) {
					String base_line = lore_array.get(lorelinenum);
					if(loreaction.equals("SET"))
						base_line = loretext;
					else if(loreaction.equals("ADDBEGIN"))
						base_line = loretext + base_line;
					else if(loreaction.equals("ADDEND"))
						base_line = base_line + loretext;
					else if(loreaction.equals("DEL"))
						base_line = base_line.replace(loretext, "");
					lore_array.set(lorelinenum, base_line);
					item_meta.setLore(lore_array);
					item_stack.setItemMeta(item_meta); 
				}
			}
			if (item_stack != null)
				item_stack = modding_item.applyMods(data, target, item_stack);
			if(target.getBukkitEntity() instanceof Player) {
				((Player)target.getBukkitEntity()).updateInventory();
			}
			return true;
		}
		return false;
	}
}