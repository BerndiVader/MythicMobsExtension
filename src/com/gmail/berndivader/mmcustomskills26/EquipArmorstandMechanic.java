package com.gmail.berndivader.mmcustomskills26;

import java.util.Optional;

import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.items.MythicItem;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class EquipArmorstandMechanic extends SkillMechanic
implements
INoTargetSkill {

	protected MythicMobs mythicmobs = Main.getPlugin().getMythicMobs();
	private String itemString;
	private int slot=0;

	public EquipArmorstandMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		String[]parse=mlc.getString(new String[]{"item","i"}).split(":");
		this.itemString=parse[0];
		if (parse.length==2&&CustomSkillStuff.isNumeric(parse[1])) this.slot=Integer.parseInt(parse[1]);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (data.getCaster().getEntity().getBukkitEntity() instanceof ArmorStand) {
			ArmorStand as=(ArmorStand)data.getCaster().getEntity().getBukkitEntity();
			Optional<MythicItem>maybeItem;
			if ((maybeItem=this.mythicmobs.getItemManager().getItem(itemString)).isPresent()) {
				ItemStack is=BukkitAdapter.adapt(maybeItem.get().generateItemStack(1));
				System.out.println("mat."+is.getType().toString());
				as.setHelmet(is);
//				Main.getPlugin().getVolatileHandler().sendArmorstandEquipPacket(as);
				return true;
			}
		}
		return true;
	}
}
