package com.gmail.berndivader.mmcustomskills26;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.drops.MythicDropTable;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class mmDropItemSkill extends SkillMechanic implements ITargetedEntitySkill,ITargetedLocationSkill {
	protected String itemtype, dropname;
	protected int amount;
	protected boolean stackable;

	public mmDropItemSkill(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.itemtype=mlc.getString(new String[]{"mythicitem","item","itemtype","type","t","i"},null);
		this.dropname=mlc.getString(new String[]{"dropname","customname","name","n"},null);
		this.amount=mlc.getInteger(new String[]{"amount","a"},1);
		this.stackable=mlc.getBoolean(new String[]{"stackable","sa"},true);
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation ltarget) {
		if (this.itemtype==null) return false;
		ArrayList<ItemStack>drops = createItemStack(this.itemtype,this.dropname,this.amount,this.stackable,(ActiveMob)data.getCaster(),null);
		World w = BukkitAdapter.adapt(ltarget.getWorld());
		Location l = BukkitAdapter.adapt(ltarget);
		for (ItemStack is : drops) {
			if (is!=null) w.dropItem(l, is);
		}
		return true;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity etarget) {
		SkillCaster caster = data.getCaster();
		if (this.itemtype==null || !(caster instanceof ActiveMob)) return false;
		ArrayList<ItemStack>drops = createItemStack(this.itemtype,this.dropname,this.amount,this.stackable,(ActiveMob)data.getCaster(),etarget);
		World w = BukkitAdapter.adapt(etarget.getWorld());
		Location l = BukkitAdapter.adapt(etarget.getLocation());
		for (ItemStack is : drops) {
			if (is!=null) w.dropItem(l, is);
		}
		return true;
	}
	
	public static ArrayList<ItemStack> createItemStack(String itemtype, String dropname, int amount, boolean stackable, ActiveMob dropper, AbstractEntity trigger) {
        Optional<MythicDropTable> maybeDropTable = MythicMobs.inst().getDropManager().getDropTable(itemtype);
        ArrayList<ItemStack> loot = new ArrayList<ItemStack>();
        MythicDropTable dt;
        if (maybeDropTable.isPresent()) {
        	dt = maybeDropTable.get();
        } else {
        	List<String>droplist = new ArrayList<>();
        	droplist.add(itemtype);
        	dt = new MythicDropTable(droplist, null, null, null, null);
        }
        for (int a=0;a<amount;a++) {
            dt.parseTable(dropper, trigger);
            for (ItemStack is : dt.getDrops()) {
            	if (dropname!=null) is.getItemMeta().setDisplayName(dropname);
            	loot.add(is);
            }
        }
        return loot;
	}
	
}
