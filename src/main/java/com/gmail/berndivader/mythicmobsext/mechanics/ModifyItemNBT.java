package com.gmail.berndivader.mythicmobsext.mechanics;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.externals.ExternalAnnotation;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.logging.MythicLogger;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.variables.Variable;
import io.lumine.xikage.mythicmobs.skills.variables.VariableMechanic;
import io.lumine.xikage.mythicmobs.skills.variables.VariableRegistry;
import io.lumine.xikage.mythicmobs.skills.variables.VariableType;
import io.lumine.xikage.mythicmobs.util.jnbt.CompoundTag;

@ExternalAnnotation(name = "modifyitemnbt,setitemnbt,getnbt,getitemnbt", author = "Seyarada")
public class ModifyItemNBT extends VariableMechanic implements ITargetedEntitySkill {

	private final String where;
	private final String NBTkey;
	private final String NBTvalue;
	private final char mode;
	private VariableType type;
	
	public ModifyItemNBT(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		mode = skill.toUpperCase().charAt(0);
		this.ASYNC_SAFE = false;
		this.where = mlc.getString(new String[] { "where", "w" }, "HAND");
		this.NBTkey = mlc.getString(new String[] { "key", "k" }, "Hello");
		this.NBTvalue = mlc.getString(new String[] { "value", "v" }, "World");
		
		String strType = mlc.getString(new String[]{"type", "t"}, VariableType.INTEGER.toString());
	    try {
	       this.type = VariableType.valueOf(strType.toUpperCase());
	    } catch (Exception e) {
	       MythicLogger.errorMechanicConfig(this, mlc, "'" + strType + "' is not a valid variable type.");
	    }
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (!target.isLiving())
			return false;
		LivingEntity entity = (LivingEntity) target.getBukkitEntity();
		EntityEquipment equipment = entity.getEquipment();
		
		switch (mode) {
			case 'G':
				ItemStack iS = null;
				switch (where) {
					case "HAND":
						iS = equipment.getItemInMainHand().clone();
						break;
					case "OFFHAND":
						iS = equipment.getItemInOffHand().clone();
						break;
					case "HELMET":
						iS = equipment.getHelmet().clone();
						break;
					case "CHESTPLATE":
						iS = equipment.getChestplate().clone();
						break;
					case "LEGGINGS":
						iS = equipment.getLeggings().clone();
						break;
					case "BOOTS":
						iS = equipment.getBoots().clone();
						break;
				}
				String value = readNBT(iS);
				VariableRegistry variables = getVariableManager().getRegistry(this.scope, data, target);
			    if (variables == null)
			    	MythicLogger.errorMechanicConfig(this, this.config, "Failed to get variable registry (MME)");
			    else {
			    	Variable var = null;
			        if (this.type != VariableType.INTEGER && this.type != VariableType.FLOAT)
			            var = Variable.ofType(this.type, value, this.duration);
			        else if (this.type == VariableType.INTEGER)
			            var = Variable.ofType(this.type, Integer.valueOf(value), this.duration);
			        else if (this.type == VariableType.FLOAT)
			             var = Variable.ofType(this.type, Float.valueOf(value), this.duration);
			        variables.put(this.key, var);
			      }
			   break;
			default:
				switch (where) {
					case "HAND":
						equipment.setItemInMainHand(setItemNBT(equipment.getItemInMainHand().clone()));
						break;
					case "OFFHAND":
						equipment.setItemInOffHand(setItemNBT(equipment.getItemInOffHand().clone()));
						break;
					case "HELMET":
						equipment.setHelmet(setItemNBT(equipment.getHelmet().clone()));
						break;
					case "CHESTPLATE":
						equipment.setChestplate(setItemNBT(equipment.getChestplate().clone()));
						break;
					case "LEGGINGS":
						equipment.setLeggings(setItemNBT(equipment.getLeggings().clone()));
						break;
					case "BOOTS":
						equipment.setBoots(setItemNBT(equipment.getBoots().clone()));
						break;
				}
				break;
		}
		return true;
	}
	
	
	public ItemStack setItemNBT(ItemStack iS) {
	    Map<String, String> tags = new HashMap<String, String>();
	    tags.put(NBTkey, NBTvalue);
	    iS = MythicItem.addItemNBT(iS, "Base", tags);
		return iS;
	}
	
	public String readNBT(ItemStack iS) {
		if(iS==null) return "null";
		CompoundTag a = MythicMobs.inst().getVolatileCodeHandler().getItemHandler().getNBTData(iS);
		if(a.containsKey(NBTkey)) return a.getString(NBTkey);
		return "null";
	}
}
