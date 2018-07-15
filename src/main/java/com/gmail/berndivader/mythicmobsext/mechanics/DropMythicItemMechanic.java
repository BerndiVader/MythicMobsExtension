package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.events.MythicMobsExtItemDropEvent;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

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

@ExternalAnnotation(name="dropmythicitem",author="BerndiVader")
public class DropMythicItemMechanic
extends
SkillMechanic 
implements 
ITargetedEntitySkill, 
ITargetedLocationSkill {
	private String itemtype;
	private String dropname;
	String[]tags;
	private String amount;
	private boolean stackable;
	private boolean shuffle,tag,give;
	private static DropManager dropmanager;
	
	static {
		dropmanager=Utils.mythicmobs.getDropManager();
	}

	public DropMythicItemMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		this.itemtype=mlc.getString(new String[] { "mythicitem", "item", "itemtype", "type", "t", "i" },null);
		this.dropname=mlc.getString(new String[] { "dropname", "customname", "name", "n" },null);
		this.tags=mlc.getString(new String[] {"tags","tag"},"NONE").split(",");
		if(tags[0].equals("NONE")) tags=null;
		this.tag=mlc.getBoolean("mark",true);
		this.give=mlc.getBoolean("give",false);
		this.amount=mlc.getString(new String[] { "amount", "a" },"1").toLowerCase();
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
		ActiveMob am=data.getCaster() instanceof ActiveMob?(ActiveMob)data.getCaster():null;
		if (this.itemtype==null) return false;
		if(this.itemtype.toLowerCase().equals("exporb")) {
			Location l=BukkitAdapter.adapt(l1);
			ExperienceOrb orb=l.getWorld().spawn(l,ExperienceOrb.class);
			orb.setExperience(Utils.randomRangeInt(amount));
			return true;
		}
		ArrayList<ItemStack>drops=createItemStack(this.itemtype,this.dropname,Utils.randomRangeInt(amount),this.stackable,this.shuffle,tag,tags,am,e1==null?data.getTrigger():e1);
		LivingEntity trigger=data.getTrigger()==null?null:(LivingEntity)data.getTrigger().getBukkitEntity();
		if (am!=null) {
			MythicMobsExtItemDropEvent e=new MythicMobsExtItemDropEvent(am,trigger,drops);
	        Bukkit.getServer().getPluginManager().callEvent(e);
			if (e.isCancelled()) return false;
			drops=e.getDrops();
		}
		dropItems(drops,e1!=null?e1.getBukkitEntity().getLocation():BukkitAdapter.adapt(l1),e1!=null?e1.isPlayer()?(Player)e1.getBukkitEntity():null:null,give);
		return true;
	}

	private static ArrayList<ItemStack> createItemStack(String itemtype, String dropname, int amount, boolean stackable, boolean bl1,
			boolean tag,String[]tags,ActiveMob dropper, AbstractEntity trigger) {
		Optional<MythicDropTable>maybeDropTable=dropmanager.getDropTable(itemtype);
		ArrayList<ItemStack>loot=new ArrayList<>();
		MythicDropTable dt;
		boolean mdt=false;
		if (maybeDropTable.isPresent()) {
			dt=maybeDropTable.get();
			mdt=true;
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
				if (tag&&!mdt) {
					NMSUtils.setMeta(is,"MythicQuestItem",itemtype);
				}
				if(tags!=null&&tags.length>0) {
					for(int i1=0;i1<tags.length;i1++) {
						String[]parse=tags[i1].split(":");
						NMSUtils.setMeta(is,parse[0],parse[1]);
					}
				}
				if (!stackable) {
					UUID uuid=UUID.randomUUID();
					String most=Long.toString(uuid.getMostSignificantBits()),least=Long.toString(uuid.getLeastSignificantBits());
					NMSUtils.setMeta(is,"RandomMost",most);
					NMSUtils.setMeta(is,"RandomLeast",least);
				}
				loot.add(is);
			}
		}
		return loot;
	}
	
	private static void dropItems(ArrayList<ItemStack> drops, Location l,Player player,boolean bl1) {
		World w=l.getWorld();
		for (ItemStack is:drops) {
			if (is==null||is.getType()==Material.AIR) continue;
			if (bl1&&player!=null&&player.getInventory().firstEmpty()>-1) {
				player.getInventory().addItem(is.clone());
				player.updateInventory();
			} else {
				w.dropItem(l,is.clone());
			}
		}
	}
}
