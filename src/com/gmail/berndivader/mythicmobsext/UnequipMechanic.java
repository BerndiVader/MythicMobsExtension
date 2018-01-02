package com.gmail.berndivader.mythicmobsext;

import io.lumine.xikage.mythicmobs.io.MythicLineConfig;

public class UnequipMechanic extends DamageArmorMechanic {

	public UnequipMechanic(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.rndMin=9999;
		this.rndMax=9999;
	}
}
