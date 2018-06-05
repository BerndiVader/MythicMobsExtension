package com.gmail.berndivader.mythicmobsext.compatibility.nocheatplus;

import fr.neatmonster.nocheatplus.checks.CheckType;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.INoTargetSkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public 
class 
ExemptPlayerMechanic 
extends 
SkillMechanic 
implements 
INoTargetSkill,
ITargetedEntitySkill {
	CheckType[]types;
	boolean resistent;

	public ExemptPlayerMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		ASYNC_SAFE=true;
		types=new CheckType[0];
		String[]arr1=mlc.getString(new String[] {"types","type","t"},"ALL").toUpperCase().split(",");
		for(int i1=0;i1<arr1.length;i1++) {
			CheckType c1=null;
			try {
				c1=CheckType.valueOf(arr1[i1]);
			} catch (Exception ex) {
				MythicMobs.error("Unable to add NCP CheckType: "+arr1[i1]+" because the tpye is invalid!");
				continue;
			}
			if (c1==CheckType.ALL) {
				types=new CheckType[]{c1};
				break;
			}
			types=NoCheatPlusSupport.inc(types,new CheckType[]{c1});
			resistent=mlc.getBoolean(new String[] {"worldchangeresist","wcr","w"},false);
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity e) {
		if(e.isPlayer()) {
			NoCheatPlusSupport.inc(e.getUniqueId(),types,resistent);
			return true;
		}
		return false;
	}

	@Override
	public boolean cast(SkillMetadata data) {
		return castAtEntity(data,data.getCaster().getEntity());
	}
	
}
