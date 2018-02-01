package main.java.com.gmail.berndivader.mythicmobsext.targeters;

import java.util.HashSet;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.AbstractVector;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.targeters.ILocationSelector;

public class EyeDirectionTargeter
extends
ILocationSelector {
	private int length;
	
	public EyeDirectionTargeter(MythicLineConfig mlc) {
		this.length=mlc.getInteger(new String[] {"length","l"},10);
	}

	@Override
	public HashSet<AbstractLocation> getLocations(SkillMetadata data) {
		HashSet<AbstractLocation>targets=new HashSet<>();
		if (data.getCaster().getEntity().isLiving()) {
			AbstractLocation l=data.getCaster().getEntity().getEyeLocation();
			AbstractVector v=l.getDirection().clone().multiply(this.length);
			l.add(v);
			targets.add(l);
		}
		return targets;
	}

}
