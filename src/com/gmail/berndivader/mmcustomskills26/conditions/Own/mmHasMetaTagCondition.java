package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;

import com.gmail.berndivader.mmcustomskills26.metaTagValue;
import com.gmail.berndivader.mmcustomskills26.metaTagValue.ValueTypes;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.ConditionAction;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;
import net.minecraft.server.v1_12_R1.Material;

public class mmHasMetaTagCondition extends SkillCondition 
implements 
ILocationCondition,
IEntityComparisonCondition {
	protected String metaStrings[];
	protected HashMap<String, metaTagValue>metatags = new HashMap<>();
	protected boolean compareToSelf;
	
	public mmHasMetaTagCondition(String line, MythicLineConfig mlc) {
		super(line);
		try {
			this.ACTION = ConditionAction.valueOf(mlc.getString(new String[]{"action","a"}, "TRUE").toUpperCase());
		} catch (Exception ex) {
			this.ACTION = ConditionAction.TRUE;
		}
		this.compareToSelf = mlc.getBoolean(new String[]{"compareself","cs"},false);
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
		Location l = BukkitAdapter.adapt(location);
		Block block = l.getBlock();
		if (block.getType().equals(Material.AIR)) return false;
		for (Map.Entry<String, metaTagValue>e:metatags.entrySet()) {
			String t = SkillString.parseMobVariables(e.getKey(), null, null, null);
			String vs = getMetaValString(e.getValue(), null, null);
			if (block.hasMetadata(t)) {
				if (e.getValue().getType().equals(ValueTypes.DEFAULT)) return true;
				for (MetadataValue mv : block.getMetadata(t)) {
					if (mv.asString().equals(vs)) return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity ae) {
		ActiveMob am=MythicMobs.inst().getMobManager().getMythicMobInstance(caster);
		for (Map.Entry<String, metaTagValue>e:metatags.entrySet()) {
			String t = SkillString.parseMobVariables(e.getKey(), am, ae, null);
			String vs = getMetaValString(e.getValue(), am, ae);
			Entity target = this.compareToSelf?caster.getBukkitEntity():ae.getBukkitEntity();
			if (target.hasMetadata(t)) {
				if (e.getValue().getType().equals(ValueTypes.DEFAULT)) return true;
				for (MetadataValue mv : target.getMetadata(t)) {
					if (mv.asString().equals(vs)) return true;
				}
			}
		}
		return false;
	}
	
	private static String getMetaValString(metaTagValue v, ActiveMob am, AbstractEntity ae) {
		String vs="";
		if (v.getType().equals(ValueTypes.BOOLEAN)) {
			vs = ((Boolean)v.getValue()).toString();
		} else if (v.getType().equals(ValueTypes.STRING)) {
			vs = SkillString.parseMobVariables((String)v.getValue(), am, ae, null);
		} else if (v.getType().equals(ValueTypes.NUMERIC)) {
			vs = ((Double)v.getValue()).toString();
		}
		return vs;
	}

}
