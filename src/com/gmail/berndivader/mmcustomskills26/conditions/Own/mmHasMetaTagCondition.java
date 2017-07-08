package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.HashMap;
import java.util.Map;

import com.gmail.berndivader.mmcustomskills26.metaTagValue;
import com.gmail.berndivader.mmcustomskills26.metaTagValue.ValueTypes;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class mmHasMetaTagCondition extends SkillCondition 
implements 
IEntityCondition,
ILocationCondition,
IEntityComparisonCondition {
	protected String metaStrings[];
	protected HashMap<String, metaTagValue>metatags = new HashMap<>();
	protected boolean isBlock;
	
	public mmHasMetaTagCondition(String line, MythicLineConfig mlc) {
		super(line);
		String ms =  mlc.getString(new String[]{"metalist","list","l"});
		ms = ms.substring(1, ms.length()-1);
		ms = SkillString.parseMessageSpecialChars(ms);
		this.metaStrings = ms.split("\\|\\|");
		for (String metaString : metaStrings) {
			String parse[] = metaString.split(";");
			String t=null, v=null, vt=null;
			for (String p : parse) {
				if (p.startsWith("tag=")) {
					t = p.substring(4);
					continue;
				} else if (p.startsWith("value=")) {
					v = p.substring(6);
					continue;
				} else if (p.startsWith("type=")) {
					vt = p.substring(5);
					continue;
				}
			}
			if (t!=null) {
				metaTagValue mtv = new metaTagValue(v, vt);
				this.metatags.put(t, mtv);
			}
		}
	}

	@Override
	public boolean check(AbstractLocation location) {
		return false;
	}

	@Override
	public boolean check(AbstractEntity entity) {
		return false;
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity target) {
		ActiveMob am=MythicMobs.inst().getMobManager().getMythicMobInstance(caster);
		for (Map.Entry<String, metaTagValue>e:metatags.entrySet()) {
			String t = e.getKey();
			metaTagValue v = e.getValue();
			String vs;
			if (v.getType().equals(ValueTypes.BOOLEAN)) {
				vs = ((Boolean)v.getValue()).toString();
			} else if (v.getType().equals(ValueTypes.STRING)) {
				vs = SkillString.parseMobVariables((String)v.getValue(), am, target, null);
			} else if (v.getType().equals(ValueTypes.NUMERIC)) {
				vs = ((Double)v.getValue()).toString();
			}
		}
		return false;
	}

}
