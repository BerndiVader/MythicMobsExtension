package com.gmail.berndivader.mmcustomskills26;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class EquipArmorstandMechanic extends SkillMechanic
implements
INoTargetSkill {

	protected MythicMobs mythicmobs = Main.getPlugin().getMythicMobs();
	private Material material;
	private int slot;

	public EquipArmorstandMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		String[]parse=mlc.getString(new String[]{"item","i"}).split(":");
		try {
			this.material=Material.valueOf(parse[0]);
		} catch (Exception e) {
			this.material=Material.DIRT;
		}
		if (parse.length==2&&CustomSkillStuff.isNumeric(parse[1])) this.slot=Integer.parseInt(parse[1]);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (data.getCaster().getEntity().getBukkitEntity() instanceof ArmorStand) {
			ArmorStand as=(ArmorStand)data.getCaster().getEntity().getBukkitEntity();
			ItemStack is=new ItemStack(this.material,1);
			switch(slot){
			case 0:{
				as.setItemInHand(is);
				break;
			}case 1:{
				as.setBoots(is);
				break;
			}case 2:{
				as.setLeggings(is);
				break;
			}case 3:{
				as.setChestplate(is);
				break;
			}case 4:{
				as.setHelmet(is);
				break;
			}}
			return true;
		}
		return false;
	}
}
