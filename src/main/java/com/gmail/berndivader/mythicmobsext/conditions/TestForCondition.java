package main.java.com.gmail.berndivader.mythicmobsext.conditions;

import main.java.com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class TestForCondition
extends
AbstractCustomCondition
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
		b=Volatile.handler.testForCondition(e.getBukkitEntity(),this.c,m);
		return b;
	}

}
