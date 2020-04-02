package com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "eyedirection", author = "BerndiVader")
public class EyeDirectionTargeter extends ISelectorLocation {

	public EyeDirectionTargeter(MythicLineConfig mlc) {
		super(mlc);
	}

	@Override
	public HashSet<AbstractLocation> getLocations(SkillMetadata data) {
		HashSet<AbstractLocation> targets = new HashSet<>();
		if (data.getCaster().getEntity().isLiving()) {
			targets.add(data.getCaster().getEntity().getEyeLocation());
		}
		return applyOffsets(targets);
	}

}
