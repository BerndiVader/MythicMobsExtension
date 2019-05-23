package com.gmail.berndivader.mythicmobsext.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble.Operation;

public 
class
HoldingItem
{
	Enchantment enchantment;
	Material material;
	String lore,name;
	RangedDouble amount;
	int slot;
	boolean material_any;
	public WhereEnum where;

	public HoldingItem() {
		this(null,"1","ANY","ANY",-1,"ANY","ANY");
	}
	
	public HoldingItem(String material,String amount,String name,String lore,int slot,String where,String enchant) {
		setMaterial(material);
		this.setAmount(amount);
		this.setLore(lore);
		this.setSlot(slot);
		this.setWhere(where);
		this.setName(name);
		this.setEnchantment(enchant);
	}
	
	boolean materialMatch(Material material) {
		return this.isAnyMaterial()||this.material.equals(material);
	}
	boolean amountMatch(String amount) {
		return this.amount.equals(amount);
	}
	boolean amountMatch(int amount) {
		boolean bool=this.amount.getOperation()==Operation.EQUALS?this.amount.getMin()<=amount:this.amount.equals(amount);
		return bool;
	}
	boolean nameMatch(ItemMeta meta) {
		if(this.name.equals("ANY")) return true;
		if(meta!=null&&meta.hasDisplayName()) return this.name.equals(meta.getDisplayName());
		return false;
	}
	boolean loreMatch(ItemMeta meta) {
		if(this.lore.equals("ANY")) return true;
		if(meta!=null&&meta.hasLore()) {
			for(Iterator<String>it=meta.getLore().iterator();it.hasNext();) {
				if (it.next().contains(this.lore)) return true; 					
			}
		}
		return false;
	}
	boolean enchantMatch(ItemMeta meta) {
		return this.enchantment==null||meta.hasEnchant(this.enchantment);
	}
	
	public boolean stackMatch(ItemStack item_stack,boolean ignore_amount) {
		boolean match=item_stack!=null&&this.materialMatch(item_stack.getType());
		if(match) match=this.nameMatch(item_stack.getItemMeta());
		if(match) match=this.loreMatch(item_stack.getItemMeta());
		if(match) match=this.enchantMatch(item_stack.getItemMeta());
		if(match&&!ignore_amount) match=this.amountMatch(item_stack.getAmount());
		return match;
	}
	
	public void setMaterial(String m) {
		if(m==null) m="ANY";
		if (m.toUpperCase().equals("ANY")) {
			this.material=null;
			this.material_any=true;
			return;
		}
		Material material;
		try {
			material=Material.valueOf(m.toUpperCase());
		} catch (Exception ex) {
			this.material_any=true;
			return;
		}
		this.material_any=false;
		this.material=material;
	}
	public Material getMaterial() {
		return material;
	}
	
	public void setEnchantment(String enchant) {
		if(enchant==null) enchant="ANY";
		if (enchant.toUpperCase().equals("ANY")) {
			this.enchantment=null;
			return;
		}
		Enchantment enchantment;
		try {
			enchantment=Enchantment.getByName(enchant.toUpperCase());
		} catch (Exception ex) {
			return;
		}
		System.err.println(enchantment.getName());
		this.enchantment=enchantment;
	}
	public Enchantment getEnchantment() {
		return enchantment;
	}
	
	public void setLore(String l) {
		this.lore=(l==null || l.isEmpty() || l.toUpperCase().equals("ANY")) ? "ANY":l;
	}
	public String getLore() {
		return lore;
	}
	
	public void setName(String l) {
		this.name=(l==null||l.isEmpty()||l.toUpperCase().equals("ANY"))?"ANY":l;
	}
	public String getName() {
		return name;
	}
	
	public void setSlot(int slot) {
		this.slot=slot;
	}
	public int getSlot() {
		return this.slot;
	}
	
	public void setWhere(WhereEnum where) {
		this.where=where;
	}
	public void setWhere(String w) { 
		this.where=WhereEnum.get(w); 
	}
	public WhereEnum getWhere() {
		return where;
	}
	
	public RangedDouble getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount=new RangedDouble(amount);
	}
	
	public Boolean isAnyMaterial(){
		return this.material_any;
	}
	
	public static List<ItemStack> getContents(HoldingItem holding, LivingEntity entity) {
		boolean is_player=entity.getType()==EntityType.PLAYER;
		List<ItemStack>contents=new ArrayList<ItemStack>();
		if (holding.getWhere().equals(WhereEnum.ANY)) {
			if (is_player) {
				contents=Arrays.asList(((Player)entity).getInventory().getContents());
			} else {
				contents=Arrays.asList(entity.getEquipment().getArmorContents());
				contents.add(entity.getEquipment().getItemInMainHand());
				contents.add(entity.getEquipment().getItemInOffHand());
			}
		} else if(holding.getWhere().equals(WhereEnum.SLOT)) {
			if(is_player) {
				ItemStack itemstack=((Player)entity).getInventory().getItem(holding.getSlot());
				contents.add(itemstack);
			}
		} else if(holding.getWhere().equals(WhereEnum.BACKBAG)) {
			if(BackBagHelper.hasBackBag(entity.getUniqueId())) {
				contents=Arrays.asList(BackBagHelper.getInventory(entity.getUniqueId()).getContents());
			}
		} else {
			if (is_player&&holding.getWhere().equals(WhereEnum.INVENTORY)) {
				contents=Arrays.asList(((Player)entity).getInventory().getStorageContents());
				contents.remove(((Player)entity).getEquipment().getItemInMainHand());
			} else if (holding.getWhere().equals(WhereEnum.HAND)) {
				contents.add(entity.getEquipment().getItemInMainHand());
			} else if (holding.getWhere().equals(WhereEnum.OFFHAND)) {
				contents.add(entity.getEquipment().getItemInOffHand());
			} else if (holding.getWhere().equals(WhereEnum.ARMOR)) {
				contents=Arrays.asList(entity.getEquipment().getArmorContents());
			}
		}
		return contents;
	}
	
	public static boolean spawnItem(ItemStack item_stack,HoldingItem holding,Location location,int pickup_delay,boolean no_drop) {
		ItemStack drop_stack=item_stack.clone();
		int amount=(int)(item_stack.getAmount()<holding.amount.getMin()?item_stack.getAmount():holding.getAmount().getMin());
		drop_stack.setAmount(amount);
		if (!no_drop) {
			Item dropped_item=location.getWorld().dropItem(location,drop_stack);
			dropped_item.setPickupDelay(pickup_delay);
		}
		if (item_stack.getAmount()<=holding.getAmount().getMin()) {
			item_stack.setAmount(0);
			item_stack.setType(Material.AIR);
		} else {
			item_stack.setAmount(item_stack.getAmount()-amount);
		}
		return true;
	}
		
}
