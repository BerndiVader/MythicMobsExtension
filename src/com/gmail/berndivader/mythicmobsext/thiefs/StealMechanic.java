package com.gmail.berndivader.mythicmobsext.thiefs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ListIterator;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class StealMechanic 
extends 
SkillMechanic 
implements
ITargetedEntitySkill {
	private ArrayList<String> items;
	private String signal_fail;
	private String signal_ok;
	private int[]iarr;

	public StealMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		this.items=new ArrayList<>(Arrays.asList(mlc.getString(new String[] { "items", "i" }, "ANY").toUpperCase().split(",")));
		this.signal_fail = mlc.getString(new String[] { "failsignal", "fail" }, "steal_fail");
		this.signal_ok = mlc.getString(new String[] { "oksignal", "ok" }, "steal_ok");
		this.iarr=i();
	}
	
	private static int[]i() {
		int[]arr1=new int[41];
		for(int j1=0;j1<41;j1++) {
			arr1[j1]=j1;
		}
		return arr1;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!target.isPlayer()||target.isDead()||!(data.getCaster()instanceof ActiveMob)) return false;
		ActiveMob am=(ActiveMob)data.getCaster();
		Player pl=(Player)BukkitAdapter.adapt(target);
		Collections.shuffle(this.items,Main.random);
		boolean stolen=false;
		ListIterator<String>iter1=this.items.listIterator();
		int a,ra;
		String it;
		ItemStack item,item1;
		while(iter1.hasNext()) {
			String[]temp=iter1.next().split(":");
			String ri=temp[0];
			try {
				ra=Integer.parseInt(temp[1]);
			} catch (NumberFormatException e) {
				ra=1;
			}
			PlayerInventory pi1=pl.getInventory();
			ListIterator<ItemStack>iter2=pi1.iterator();
			if (ri.equals("ANY")) {
				int[]arr1=Utils.shuffleArray(this.iarr.clone());
				boolean c=false;
				int i=0;
				while(!c&&i<41) {
					Material m1=pi1.getItem(arr1[i])!=null?pi1.getItem(arr1[i]).getType():Material.AIR;
					if(m1!=Material.AIR) {
						c=true;
						ri=m1.name();
						break;
					}
					i++;
				}
			}
			while(iter2.hasNext()&&!stolen) {
				item=iter2.next();
				if (item==null||item.getType()==Material.AIR) continue;
				it=item.getType().toString();
				a=item.getAmount();
				if (ri.equals(it)) {
					if (ra<a) {
						item1=new ItemStack(item);
						item1.setAmount(a-ra);
						iter2.set(item1.clone());
					} else {
						ra=a;
						iter2.set(new ItemStack(Material.AIR));
					}
					am.signalMob(am.getEntity(),this.signal_ok);
					item1=new ItemStack(item);
					item1.setAmount(ra);
					Thiefs.thiefhandler.addThief(am.getUniqueId(),item1.clone());
					stolen=true;
					break;
				}
				if (stolen) break;
			}
		}
		if (!stolen) am.signalMob(am.getEntity(),this.signal_fail);
		return true;
	}
}
