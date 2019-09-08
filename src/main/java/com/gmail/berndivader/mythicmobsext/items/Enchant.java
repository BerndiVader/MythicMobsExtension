package com.gmail.berndivader.mythicmobsext.items;

import org.bukkit.enchantments.Enchantment;

import com.gmail.berndivader.mythicmobsext.utils.RandomDouble;

public 
class 
Enchant
{
	public Enchantment enchantment;
	public RandomDouble level;
		
	public Enchant(Enchantment ench,String level) {
		this.enchantment=ench;
		this.level=new RandomDouble(level);
	}

}
