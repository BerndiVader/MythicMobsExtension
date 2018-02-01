package main.java.com.gmail.berndivader.mythicmobsext.conditions;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.MetadataValue;

import main.java.com.gmail.berndivader.mythicmobsext.mechanics.MetaTagValue;
import main.java.com.gmail.berndivader.mythicmobsext.mechanics.MetaTagValue.ValueTypes;
import main.java.com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityComparisonCondition;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

public class HasMetaTagCondition
extends
AbstractCustomCondition
implements
ILocationCondition,
IEntityComparisonCondition {
	protected HashMap<String,MetaTagValue> metatags = new HashMap<>();
	protected boolean compareToSelf;

	public HasMetaTagCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.compareToSelf=mlc.getBoolean(new String[] { "compareself", "cs" }, false);
		if (!line.toLowerCase().startsWith("hasmetasimple")) {
			String ms = mlc.getString(new String[] { "metalist", "list", "l" });
			if (ms.startsWith("\"")&&ms.endsWith("\"")) ms=ms.substring(1,ms.length()-1);
			ms = SkillString.parseMessageSpecialChars(ms);
			String metaStrings[]=ms.split("\\|\\|");
			for (String metaString:metaStrings) {
				String parse[]=metaString.split(";");
				String t=null,v=null,vt=null;
				for (String p:parse) {
					if (p.startsWith("tag=")) {
						t=p.substring(4);
						continue;
					} else if (p.startsWith("value=")) {
						v=p.substring(6);
						continue;
					} else if (p.startsWith("type=")) {
						vt=p.substring(5);
						continue;
					}
				}
				if (t!=null) {
					MetaTagValue mtv=new MetaTagValue(v,vt);
					this.metatags.put(t,mtv);
				}
			}
		} else {
			String t,v,vt;
			t=SkillString.parseMessageSpecialChars(mlc.getString("tag"));
			v=SkillString.parseMessageSpecialChars(mlc.getString("value"));
			vt=SkillString.parseMessageSpecialChars(mlc.getString("type"));
			if (t!=null) {
				MetaTagValue mtv=new MetaTagValue(v,vt);
				this.metatags.put(t,mtv);
			}
		}
	}

	@Override
	public boolean check(AbstractLocation location) {
		Location l=BukkitAdapter.adapt(location);
		Block block=l.getBlock();
		for (Map.Entry<String,MetaTagValue>e:metatags.entrySet()) {
			String t=SkillString.parseMobVariables(e.getKey(),null,null,null);
			String vs=getMetaValString(e.getValue(),null,null);
			if (block.hasMetadata(t)) {
				if (e.getValue().getType().equals(ValueTypes.DEFAULT)) return true;
				for (MetadataValue mv:block.getMetadata(t)) {
					if (mv.asString().equals(vs)) return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean check(AbstractEntity caster, AbstractEntity ae) {
		ActiveMob am = Utils.mobmanager.getMythicMobInstance(caster);
		for (Map.Entry<String,MetaTagValue>e:metatags.entrySet()) {
			String t=SkillString.parseMobVariables(e.getKey(),am,ae,null);
			String vs=getMetaValString(e.getValue(),am,ae);
			Entity target=this.compareToSelf?caster.getBukkitEntity():ae.getBukkitEntity();
			if (target.hasMetadata(t)) {
				if (e.getValue().getType().equals(ValueTypes.DEFAULT)) return true;
				for (MetadataValue mv:target.getMetadata(t)) {
					if (mv.asString().equals(vs)) return true;
				}
			}
		}
		return false;
	}

	private static String getMetaValString(MetaTagValue v,ActiveMob am,AbstractEntity ae) {
		String vs="";
		if (v.getType().equals(ValueTypes.BOOLEAN)) {
			vs=((Boolean) v.getValue()).toString();
		} else if (v.getType().equals(ValueTypes.STRING)) {
			vs=SkillString.parseMobVariables((String)v.getValue(),am,ae,null);
		} else if (v.getType().equals(ValueTypes.NUMERIC)) {
			vs=((Double)v.getValue()).toString();
		}
		return vs;
	}

}
