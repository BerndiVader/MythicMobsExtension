package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantRecipe;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "trade", author = "Seyarada")
public class Trade extends SkillMechanic implements ITargetedEntitySkill{
	String title;
	List<String> tradesRaw = new ArrayList<>();

	public Trade(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		title = mlc.getString(new String[] { "title", "t"}, "Trades");
		for (int i = 1; i <= 10; i++) {
			String x = mlc.getString(new String[] {String.valueOf(i)}, "none");
			
			if (!x.equals("none")) tradesRaw.add(x);
			}

	}
	
	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity p) {
		if (p.isPlayer()) {
			Player player = (Player) p.getBukkitEntity();
			List<MerchantRecipe> merchantRecipes = getRecipes();
			
			Entity v = data.getCaster().getEntity().getBukkitEntity();
			if(v instanceof Villager) {
				((Villager)v).setRecipes(merchantRecipes);
				player.openMerchant(((Villager)v), true);
			} else {
				Merchant merchant = Bukkit.createMerchant(title);
				merchant.setRecipes(merchantRecipes);
				player.openMerchant(merchant, true);
			}
			
			
		}
		return true;
	}
	
	public List<MerchantRecipe> getRecipes() {
		List<MerchantRecipe> merchantRecipes = new ArrayList<MerchantRecipe>();
		
		for (String trades : tradesRaw) {
			
			ItemStack r = null, p1 = null, p2 = null;
        	ItemStack f1 = null,f2 = null,f3 = null;
        	Integer uses = 9999;
        	Boolean xp = true;
        	
            for(String trade : trades.split(",")) {	
            	String rS = null, p1S = null, p2S = null;
            	
            	
            	String[] n = trade.split(":");
            	String k = trade.split(":")[0];
            	String l = trade.split(":")[1];
            	
            	Integer amount = 1;
            	if (n.length > 2)
            		amount = Integer.valueOf(n[2]);
            	
            	if (k.equals("result")) rS = l;
            	else if (k.equals("price")) p1S = l;
            	else if (k.equals("price1")) p1S = l;
            	else if (k.equals("price2")) p2S = l;
            	else if (k.equals("uses")) uses = Integer.valueOf(l);
            	else if (k.equals("xp")) xp = Boolean.valueOf(l);
            	
            	r = getItem(rS,amount);
            	p1 = getItem(p1S,amount);
            	p2 = getItem(p2S,amount);
            	
            	if (r!=null) f1 = r;
            	if (p1!=null) f2 = p1;
            	if (p2!=null) f3 = p2;
            	rS = null; p1S = null; p2S = null;
            	
            }
        	MerchantRecipe recipe = new MerchantRecipe(f1, uses);
        	recipe.setVillagerExperience(0);
			recipe.setExperienceReward(xp);
			recipe.addIngredient(f2);
			recipe.setVillagerExperience(5);
			if (f3!=null) recipe.addIngredient(f3);
			merchantRecipes.add(recipe);
        }
		return merchantRecipes;
	}
	
	public ItemStack getItem(String i, Integer amount) {
		if (i == null) return null;
		
		ItemStack item = null;
		try {
			Material baseMaterial = Material.valueOf(i);
			item = new ItemStack(baseMaterial, amount);
		} catch (Exception e) {
			Optional<MythicItem> t = MythicMobs.inst().getItemManager().getItem(i);
            item = BukkitAdapter.adapt(t.get().generateItemStack(amount));
		}
		return item;
	}
}
