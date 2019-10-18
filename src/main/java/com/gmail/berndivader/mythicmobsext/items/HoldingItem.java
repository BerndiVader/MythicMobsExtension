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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.backbags.BackBagHelper;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble.Operation;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

public 
class
HoldingItem
implements
Cloneable
{
	Enchantment enchantment;
	Material material;
	String lore,name,bag_name;
	RangedDouble amount;
	boolean material_any;
	public WhereEnum where;
	String slot;

	public HoldingItem() {
		this(null,"1","ANY","ANY","-7331","ANY","ANY",BackBagHelper.str_name);
	}
	
	public HoldingItem(String material,String amount,String name,String lore,String slot,String where,String enchant,String bag_name) {
		this.where=WhereEnum.ANY;
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
	
	public void parseSlot(SkillMetadata data,AbstractEntity target) {
		if(this.slot!=null) {
			this.slot=new PlaceholderString(this.slot).get(data,target);
		}
	}
	
	public void setSlot(String slot) {
		this.slot=slot;
	}
	public int getSlot() {
		return Integer.parseInt(this.slot);
	}
	
	public void setWhere(WhereEnum where) {
		this.where=where;
	}
	public void setWhere(String w) { 
		this.where=WhereEnum.getWhere(w); 
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
	
	public void setBagName(String bag_name) {
		this.bag_name=bag_name;
	}
	public String getBagName() {
		return this.bag_name;
	}
	
	public Boolean isAnyMaterial(){
		return this.material_any;
	}
	
	public static void parse(String parse, HoldingItem holding) {
		String[]p=parse.split(parse.contains(",")?",":";");
		for(String parse1:p) {
			if(parse1.startsWith("material=")) {
				parse1=parse1.substring(9, parse1.length());
				holding.setMaterial(parse1);
			} else if(parse1.startsWith("lore=")) {
				parse1=parse1.substring(5, parse1.length());
				holding.setLore(parse1);
			} else if(parse1.startsWith("name=")) {
				parse1=parse1.substring(5, parse1.length());
				holding.setName(parse1);
			} else if(parse1.startsWith("amount=")) {
				parse1=parse1.substring(7, parse1.length());
				holding.setAmount(parse1);
			} else if(parse1.startsWith("where=")) {
				parse1=parse1.substring(6,parse1.length());
				holding.setWhere(parse1);
			} else if(parse1.startsWith("slot=")) {
				parse1=parse1.substring(5,parse1.length());
				holding.setSlot(parse1);
			} else if(parse1.startsWith("bagname=")) {
				parse1=parse1.substring(8,parse1.length());
				holding.setBagName(parse1);
			} else if(parse1.startsWith("enchant=")) {
				parse1=parse1.substring(8,parse1.length());
				holding.setEnchantment(parse1);
			}
		}
	}
	
	public static List<ItemStack> getContents(HoldingItem holding, LivingEntity entity) {
		boolean is_player=entity.getType()==EntityType.PLAYER;
		List<ItemStack>contents=new ArrayList<ItemStack>();
		if (holding.getWhere().equals(WhereEnum.ANY)) {
			if (is_player) {
				contents.addAll(Arrays.asList(((Player)entity).getInventory().getContents()));
			} else {
				contents.addAll(Arrays.asList(entity.getEquipment().getArmorContents()));
				contents.add(entity.getEquipment().getItemInMainHand());
				contents.add(entity.getEquipment().getItemInOffHand());
			}
		} else if(holding.getWhere().equals(WhereEnum.SLOT)) {
			if(is_player) {
				ItemStack itemstack=new ItemStack(Material.AIR);
				if(holding.getSlot()>-1) itemstack=((Player)entity).getInventory().getItem(holding.getSlot());
				contents.add(itemstack);
			}
		} else if(holding.getWhere().equals(WhereEnum.BACKBAG)) {
			if(BackBagHelper.hasBackBag(entity.getUniqueId())) {
				Inventory inventroy=BackBagHelper.getInventory(entity.getUniqueId(),holding.getBagName());
				if(inventroy!=null) {
					if(holding.getSlot()>-1) {
						contents.add(inventroy.getItem(holding.getSlot()));
					} else {
						contents=Arrays.asList(inventroy.getContents());
					}
				}
			}
		} else {
			if (is_player&&holding.getWhere().equals(WhereEnum.INVENTORY)) {
				contents.addAll(Arrays.asList(((Player)entity).getInventory().getStorageContents()));
				contents.remove(((Player)entity).getEquipment().getItemInMainHand());
			} else if (holding.getWhere().equals(WhereEnum.HAND)) {
				contents.add(entity.getEquipment().getItemInMainHand());
			} else if (holding.getWhere().equals(WhereEnum.OFFHAND)) {
				contents.add(entity.getEquipment().getItemInOffHand());
			} else if (holding.getWhere().equals(WhereEnum.HELMET)) {
				contents.add(entity.getEquipment().getHelmet());
			} else if (holding.getWhere().equals(WhereEnum.CHESTPLATE)) {
				contents.add(entity.getEquipment().getChestplate());
			} else if (holding.getWhere().equals(WhereEnum.LEGGINGS)) {
				contents.add(entity.getEquipment().getLeggings());
			} else if (holding.getWhere().equals(WhereEnum.BOOTS)) {
				contents.add(entity.getEquipment().getBoots());
			} else if (holding.getWhere().equals(WhereEnum.ARMOR)) {
				contents.addAll(Arrays.asList(entity.getEquipment().getArmorContents()));
			}
		}
		for(int i1=0;i1<contents.size();i1++) {
			ItemStack stack=contents.get(i1);
		}
		return contents;
	}
	
	public static boolean giveItem(LivingEntity entity,HoldingItem holding,ItemStack item_stack,boolean override) {
		switch(holding.where) {
		case SLOT:
		case INVENTORY:
			if(entity instanceof Player) {
				Player player=(Player)entity;
				PlayerInventory inventory=player.getInventory();
				if(holding.getSlot()>-1) {
					if(override||(inventory.getItem(holding.getSlot())==null||inventory.getItem(holding.getSlot()).getType()==Material.AIR)) {
						inventory.setItem(holding.getSlot(),item_stack.clone());
					}
				} else if(inventory.firstEmpty()>-1){
					inventory.addItem(item_stack.clone());
				}
			}
			break;
			case BACKBAG:
				if(BackBagHelper.hasBackBag(entity.getUniqueId())) {
					Inventory inventory=BackBagHelper.getInventory(entity.getUniqueId(),holding.getBagName());
					if(inventory!=null) {
						if(holding.getSlot()>-1) {
							if(override||(inventory.getItem(holding.getSlot())==null||inventory.getItem(holding.getSlot()).getType()==Material.AIR)) {
								inventory.setItem(holding.getSlot(),item_stack.clone());
							}
						} else if(inventory.firstEmpty()>-1){
							inventory.addItem(item_stack.clone());
						}
					}
				}
				break;
			case HELMET:
				if(override||entity.getEquipment().getHelmet()==null) entity.getEquipment().setHelmet(item_stack.clone());
				break;
			case CHESTPLATE:
				if(override||entity.getEquipment().getChestplate()==null) entity.getEquipment().setChestplate(item_stack.clone());
				break;
			case LEGGINGS:
				if(override||entity.getEquipment().getLeggings()==null) entity.getEquipment().setLeggings(item_stack.clone());
				break;
			case BOOTS:
				if(override||entity.getEquipment().getBoots()==null) entity.getEquipment().setBoots(item_stack.clone());
				break;
			case HAND:
				if(override||entity.getEquipment().getItemInMainHand()==null) entity.getEquipment().setItemInMainHand(item_stack.clone());
				break;
			case OFFHAND:
				if(override||entity.getEquipment().getItemInOffHand()==null) entity.getEquipment().setItemInOffHand(item_stack.clone());
				break;
			default:
				break;
		}
		return true;
	}
	
	public static boolean spawnItem(ItemStack item_stack,HoldingItem holding,Location location,int pickup_delay,boolean no_drop) {
		ItemStack drop_stack=new ItemStack(item_stack);
		int amount=(int)(item_stack.getAmount()<holding.amount.getMin()?item_stack.getAmount():holding.getAmount().getMin());
		drop_stack.setAmount(amount);
		if(amount>0&&drop_stack.getType()!=Material.AIR) {
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
		}
		return true;
	}
	
	public static void tagWhere(HoldingItem holding,ItemStack new_item) {
		new_item=NMSUtils.makeReal(new_item);
		NMSUtils.setMeta(new_item,Utils.meta_BACKBACKTAG,holding.getWhere().name());
	}
	
	@Override
	public HoldingItem clone() {
		try {
			return (HoldingItem)super.clone();
		} catch (CloneNotSupportedException e) {
			Main.logger.warning(e.getMessage());
			return null;
		}
	}
}
