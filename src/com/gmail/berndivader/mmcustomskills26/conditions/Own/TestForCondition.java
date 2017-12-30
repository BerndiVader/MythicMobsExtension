package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import com.gmail.berndivader.mmcustomskills26.Main;
import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import net.minecraft.server.v1_12_R1.CommandException;

public class TestForCondition
extends
mmCustomCondition
implements
IEntityCondition {
	private String c;
	private char m=0;

	public TestForCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.c=mlc.getString(new String[]{"vc","c"},"");
		if (c.startsWith("\"")&&c.endsWith("\"")) {
			this.c=SkillString.parseMessageSpecialChars(this.c.substring(1,this.c.length()-1));
		}
	}

	@Override
	public boolean check(AbstractEntity e) {
		boolean b=true;
		try {
			b=Main.getPlugin().getVolatileHandler().testForCondition(e.getBukkitEntity(),this.c,m);
		} catch (CommandException e1) {
			e1.printStackTrace();
		}
		return b;
	}

}
