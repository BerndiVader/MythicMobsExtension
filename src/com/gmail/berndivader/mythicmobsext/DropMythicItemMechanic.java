package com.gmail.berndivader.mythicmobsext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.events.MythicMobsExtItemDropEvent;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.DropManager;
import io.lumine.xikage.mythicmobs.drops.MythicDropTable;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class DropMythicItemMechanic
extends
SkillMechanic 
implements 
ITargetedEntitySkill, 
ITargetedLocationSkill {
	private String itemtype;
	private String dropname;
	private Integer amount;
	private Boolean stackable;
	private Boolean shuffle;

	public DropMythicItemMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.itemtype=mlc.getString(new String[] { "mythicitem", "item", "itemtype", "type", "t", "i" },null);
		this.dropname=mlc.getString(new String[] { "dropname", "customname", "name", "n" },null);
		this.amount=mlc.getInteger(new String[] { "amount", "a" }, 1);
		this.stackable=mlc.getBoolean(new String[] { "stackable", "sa" },true);
		this.shuffle=mlc.getBoolean(new String[] { "shuffle", "s" },false);
	}
	
	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation ltarget) {
		return a(data,null,ltarget);
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity etarget) {
		return a(data,etarget,null);
	}
	
	private boolean a(SkillMetadata data,AbstractEntity e1,AbstractLocation l1) {
		ActiveMob am=(ActiveMob)data.getCaster();
		if (this.itemtype==null||am==null) return false;
		ArrayList<ItemStack>drops=createItemStack(this.itemtype,this.dropname,this.amount,this.stackable,this.shuffle,am,e1==null?data.getTrigger():e1);
		LivingEntity trigger=data.getTrigger()==null?null:(LivingEntity)data.getTrigger().getBukkitEntity();
		MythicMobsExtItemDropEvent e=new MythicMobsExtItemDropEvent(am,trigger,drops);
        Bukkit.getServer().getPluginManager().callEvent(e);
		if (e.isCancelled()) return false;
		drops=e.getDrops();
		dropItems(drops,e1!=null?e1.getBukkitEntity().getLocation():BukkitAdapter.adapt(l1));
		return true;
	}

	private static ArrayList<ItemStack> createItemStack(String itemtype, String dropname, int amount, boolean stackable, boolean bl1,
			ActiveMob dropper, AbstractEntity trigger) {
		DropManager dropmanager=Main.getPlugin().getMythicMobs().getDropManager();
		Optional<MythicDropTable>maybeDropTable=dropmanager.getDropTable(itemtype);
		ArrayList<ItemStack>loot=new ArrayList<>();
		MythicDropTable dt;
		if (maybeDropTable.isPresent()) {
			dt=maybeDropTable.get();
		} else {
			List<String>droplist=new ArrayList<>();
			droplist.add(itemtype);
			dt=new MythicDropTable(droplist,null,null,null,null);
		}
		if (bl1) Collections.shuffle(dt.strDropItems);
		for (int a=0;a<amount;a++) {
			dt.parseTable(dropper,trigger);
			for (ItemStack is:dt.getDrops()) {
				if (dropname!=null) is.getItemMeta().setDisplayName(dropname);
				loot.add(is);
			}
		}
		return loot;
	}

	private static void dropItems(ArrayList<ItemStack> drops, Location l) {
		World w=l.getWorld();
		for (ItemStack is:drops) {
			if (is!=null&&is.getType()!=Material.AIR) w.dropItem(l,is);
		}
	}
}
