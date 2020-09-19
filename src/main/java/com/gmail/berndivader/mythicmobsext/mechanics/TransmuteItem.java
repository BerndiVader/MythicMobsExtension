package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "transmuteitem", author = "Seyarada")
public class TransmuteItem extends SkillMechanic implements ITargetedEntitySkill {

	int amount;
	ItemStack baseItem;
	ItemStack resultItem;
	Material resultMaterial;
	String resultMLC;
	
	public TransmuteItem(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		amount = mlc.getInteger(new String[] {"amount", "a"}, -1);
		
		String baseMLC = mlc.getString(new String[] {"item", "i"}, "STONE");
		try {
			Material baseMaterial = Material.valueOf(baseMLC);
			baseItem = new ItemStack(baseMaterial);
		} catch (Exception e) {
			Optional<MythicItem> t = MythicMobs.inst().getItemManager().getItem(baseMLC);
            ItemStack item = BukkitAdapter.adapt(t.get().generateItemStack(1));
            baseItem = item;
		}
				
		resultMLC = mlc.getString(new String[] {"result", "r"}, "DIRT");
	}
	
	public ItemStack generateItemStack(int quantity) {
		
		try {
			Material resultMaterial = Material.valueOf(resultMLC);
			resultItem = new ItemStack(resultMaterial, quantity);
		} catch (Exception e) {
			Optional<MythicItem> t = MythicMobs.inst().getItemManager().getItem(resultMLC);
            ItemStack mythicItem = BukkitAdapter.adapt(t.get().generateItemStack(quantity));
            resultItem = mythicItem;
		}
		
		return resultItem;
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity p) {
		
		int altAmount = Integer.valueOf(amount);
		Player player = (Player) p.getBukkitEntity();
		if (p.isPlayer()) {
			
			ItemStack[] baseContent = player.getInventory().getContents();
			for(int i = 0; i < player.getInventory().getSize(); i++) {
				if(baseContent[i] != null && baseContent[i].isSimilar(baseItem)) {
		
					int quantity = baseContent[i].getAmount();
					if(altAmount<0) {
						System.out.println("Infinite...");
						resultItem = new ItemStack(generateItemStack(quantity));
						player.getInventory().setItem(i, resultItem);
					}
					// stack 50
					// amount 33
					// stack = 50-33
					// -> 27
					
					
					// Stack is larger than amount
					else if(quantity > altAmount && altAmount != 0) {
						baseContent[i].setAmount(quantity-altAmount);
						player.getInventory().addItem(generateItemStack(altAmount));
						altAmount = 0;
					}
					
					// Stack is smaller than amount
					else if (altAmount != 0) {
						resultItem = new ItemStack(generateItemStack(quantity));
						player.getInventory().setItem(i, resultItem);
						altAmount = altAmount - quantity;
					} else {
						System.out.println("None?");
					}
				}
			}
		}
		
		return false;
	}

}

/*
var TransmuteItem=function(data,target,mlc) {
var baseContent = target.getInventory();
var invContent = baseContent.getContents();
var transmuteAmount = mlc.getString("amount");
//var baseMaterial = mythicmobs.getItemManager().getItemStack(mlc.getString("base"));
var baseMaterial = mythicmobs.getItemManager().getItem(mlc.getString("base"));
baseMaterial = baseMaterial.get();
var outputMaterial = baseMaterial.generateItemStack(1);
Bukkit.getServer().broadcastMessage(outputMaterial);
var transmuteMaterial = mythicmobs.getItemManager().getItemStack(mlc.getString("result"));
transmuteMaterial.setAmount(transmuteAmount);
for(i = 0; i < invContent.length; i++) {
    if(invContent[i] !== null && invContent[i].isSimilar(baseMaterial)) {
        var matchAmount = invContent[i].getAmount();
        if(matchAmount - transmuteAmount >= 0) {
            invContent[i].setAmount(matchAmount - transmuteAmount);
            baseContent.setItem(i, invContent[i]);
            baseContent.addItem(transmuteMaterial);
            return true;
*/