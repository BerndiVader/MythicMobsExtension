package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.Optional;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.items.HoldingItem;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.ItemManager;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderInt;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name = "giveitem", author = "BerndiVader")
public class CreateItem extends SkillMechanic implements ITargetedEntitySkill, INoTargetSkill {

	static ItemManager itemmanager = Utils.mythicmobs.getItemManager();
	public final static String str_viewonly = "view_only";

	PlaceholderString bag_name;
	PlaceholderString item_name;
	String click_skill;
	HoldingItem holding;
	boolean override;
	Optional<Boolean> view_only = Optional.empty();
	PlaceholderInt amount;

	public CreateItem(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE = false;

		holding = new HoldingItem();
		this.holding.setWhere(mlc.getString("to", "inventory"));
		this.holding.setSlot(mlc.getString("slot", "-1"));
		this.holding.setBagName(mlc.getString("bagname"));

		this.item_name = PlaceholderString.of(mlc.getString("item"));
		this.amount = PlaceholderInt.of(mlc.getString("amount", "1"));
		this.override = mlc.getBoolean("override", true);
		this.click_skill = mlc.getString("clickskill");
		if (mlc.getLine().contains("viewonly")) {
			this.view_only = Optional.ofNullable(mlc.getBoolean("viewonly"));
		}
	}

	@Override
	public boolean cast(SkillMetadata data) {
		castAtEntity(data, data.getCaster().getEntity());
		return false;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		HoldingItem holding = this.holding.clone();
		if (holding != null) {
			if (item_name == null || !abstract_entity.isLiving())
				return false;
			holding.parseSlot(data, abstract_entity);
			if (bag_name != null)
				holding.setBagName(this.bag_name.get(data, abstract_entity));
			ItemStack item_stack = itemmanager.getItemStack(this.item_name.get(data, abstract_entity));
			if (item_stack != null) {
				item_stack.setAmount(amount.get(data, abstract_entity));
				item_stack = NMSUtils.makeReal(item_stack);
				if (this.click_skill != null)
					NMSUtils.setMeta(item_stack, Utils.meta_CLICKEDSKILL, this.click_skill);
				if (this.view_only.isPresent())
					NMSUtils.setMetaBoolean(item_stack, str_viewonly, view_only.get());
				HoldingItem.giveItem((LivingEntity) abstract_entity.getBukkitEntity(), holding, item_stack, override);
				return true;
			}
		}
		return false;
	}

}
