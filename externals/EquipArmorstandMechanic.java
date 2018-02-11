package externalmechanics;

import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;

import com.gmail.berndivader.mythicmobsext.externals.SkillAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@SkillAnnotation(name="asequip",author="BerndiVader")
public class EquipArmorstandMechanic extends SkillMechanic
implements
INoTargetSkill {
	Material material;
	int slot;

	public EquipArmorstandMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE=false;
		String[]parse=mlc.getString(new String[] { "item", "i" } ).split(":");
		try {
			this.material=Material.valueOf(parse[0]);
		} catch (Exception e) {
			this.material=Material.DIRT;
		}
		if (parse.length==2 && Utils.isNumeric(parse[1])) this.slot=Integer.parseInt(parse[1]);
	}

	@Override
	public boolean cast(SkillMetadata data) {
		if (data.getCaster().getEntity().getBukkitEntity() instanceof ArmorStand) {
			ArmorStand as=(ArmorStand)data.getCaster().getEntity().getBukkitEntity();
			ItemStack is=new ItemStack(this.material,1);
			switch(slot){
				case 0: {
					as.setItemInHand(is);
					break;
				} case 1: {
					as.setBoots(is);
					break;
				} case 2: {
					as.setLeggings(is);
					break;
				} case 3: {
					as.setChestplate(is);
					break;
				} case 4: {
					as.setHelmet(is);
					break;
				}
			}
			return true;
		}
		return false;
	}
}
