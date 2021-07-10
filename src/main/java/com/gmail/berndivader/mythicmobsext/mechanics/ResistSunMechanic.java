package com.gmail.berndivader.mythicmobsext.mechanics;

import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "sunresist", author = "BerndiVader")
public class ResistSunMechanic extends SkillMechanic implements INoTargetSkill {
	public ResistSunMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		data.getCaster().getEntity().getBukkitEntity().setMetadata(Utils.meta_NOSUNBURN,
				new FixedMetadataValue(Main.getPlugin(), true));
		return true;
	}

}
