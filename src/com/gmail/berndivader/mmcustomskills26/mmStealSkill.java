package com.gmail.berndivader.mmcustomskills26;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.mechanics.CustomMechanic;

public class mmStealSkill extends SkillMechanic implements ITargetedEntitySkill {
	protected ThiefHandler thiefhandler;
	private ArrayList<String> items;
	private String[] temp;
	private ActiveMob am;
	private String signal_fail;
	private String signal_ok;

	public mmStealSkill(CustomMechanic skill, MythicLineConfig mlc) {
		super(skill.getConfigLine(), mlc);
		this.ASYNC_SAFE = false;
		this.thiefhandler = Main.getPlugin().getThiefHandler();
		String[] i = mlc.getString(new String[] { "items", "i" }, "").split(",");
		this.items = new ArrayList<>(Arrays.asList(i));
		this.signal_fail = mlc.getString(new String[] { "failsignal", "fail" }, "steal_fail");
		this.signal_ok = mlc.getString(new String[] { "oksignal", "ok" }, "steal_ok");
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!target.isPlayer() || target.isDead())
			return false;
		SkillCaster caster = data.getCaster();
		if (!(caster instanceof ActiveMob))
			return false;
		am = (ActiveMob) caster;
		Player pl = (Player) BukkitAdapter.adapt(target);
		long seed = System.nanoTime();
		Collections.shuffle(this.items, new Random(seed));
		Inventory pit = pl.getInventory();
		String ri, it;
		ItemStack item;
		int a, ra;
		boolean stolen = false;
		for (String ll : items) {
			ListIterator<ItemStack> ii = pit.iterator();
			temp = ll.split(":");
			ri = temp[0];
			try {
				ra = Integer.parseInt(temp[1]);
			} catch (NumberFormatException e) {
				ra = 1;
			}
			while (ii.hasNext() && !stolen) {
				item = ii.next();
				if (item != null) {
					it = item.getData().getItemType().toString();
					a = item.getAmount();
					if (ri.equalsIgnoreCase(it)) {
						if (ra < a) {
							ii.set(new ItemStack(item.getData().getItemType(), a - ra));
						} else {
							ra = a;
							ii.set(new ItemStack(Material.AIR));
						}
						am.signalMob(am.getEntity(), this.signal_ok);
						this.thiefhandler.addThief(am.getUniqueId(), new ItemStack(item.getData().getItemType(), ra));
						stolen = true;
						break;
					}
				}
				if (stolen) {
					break;
				}
			}
		}
		;
		if (!stolen) {
			am.signalMob(am.getEntity(), this.signal_fail);
		}
		return true;
	}
}
