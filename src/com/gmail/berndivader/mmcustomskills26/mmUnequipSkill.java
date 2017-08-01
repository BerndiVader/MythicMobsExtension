package com.gmail.berndivader.mmcustomskills26;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;

public class mmUnequipSkill extends mmDamageArmorSkill {

	public mmUnequipSkill(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.rndMin = 9999;
		this.rndMax = 9999;
	}
}
