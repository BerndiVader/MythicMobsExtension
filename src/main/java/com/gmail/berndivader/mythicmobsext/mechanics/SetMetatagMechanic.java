package com.gmail.berndivader.mythicmobsext.mechanics;

import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.MetaTagValue;
import com.gmail.berndivader.mythicmobsext.utils.MetaTagValue.ValueTypes;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.placeholders.parsers.PlaceholderString;

@ExternalAnnotation(name="setmeta",author="BerndiVader")
public class SetMetatagMechanic 
extends 
SkillMechanic 
implements
ITargetedLocationSkill,
ITargetedEntitySkill {
	protected PlaceholderString tag;
	protected MetaTagValue mtv;
	protected boolean useCaster;

	public SetMetatagMechanic(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.ASYNC_SAFE = false;
		this.useCaster=mlc.getBoolean(new String[] { "usecaster", "uc" },false);
		String ms = mlc.getString(new String[] { "meta", "m" },"");
		if (ms.startsWith("\"") && ms.endsWith("\"")) ms = ms.substring(1, ms.length() - 1);
		ms = SkillString.parseMessageSpecialChars(ms);
		String parse[] = ms.split(";");
		String t = null, v = null, vt = null;
		for (int a=0;a<parse.length;a++) {
			String p=parse[a];
			if (p.startsWith("tag=")) {
				t = p.substring(4);
			} else if (p.startsWith("value=")) {
				v = p.substring(6);
			} else if (p.startsWith("type=")) {
				vt = p.substring(5);
			}
		}
		if (t != null) {
			this.tag = new PlaceholderString(t);
			mtv = new MetaTagValue(v, vt);
		}
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		String parsedTag=this.tag.get(data,target);
		if (parsedTag==null||parsedTag.isEmpty()) return false;
		Object vo = this.mtv.getType().equals(ValueTypes.STRING) ?
				new PlaceholderString((String)this.mtv.getValue()).get(data,target)
				:this.mtv.getValue();
		if (this.useCaster) {
			data.getCaster().getEntity().getBukkitEntity().setMetadata(parsedTag, new FixedMetadataValue(Main.getPlugin(), vo));
		} else {
			target.getBukkitEntity().setMetadata(parsedTag, new FixedMetadataValue(Main.getPlugin(), vo));
		}
		return true;
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation location) {
		Block target = BukkitAdapter.adapt(location).getBlock();
		String parsedTag = this.tag.get(data);
		if (parsedTag==null||parsedTag.isEmpty()) return false;
		Object vo = this.mtv.getType().equals(ValueTypes.STRING)
				? new PlaceholderString((String)this.mtv.getValue()).get(data)
				: this.mtv.getValue();
		target.setMetadata(parsedTag, new FixedMetadataValue(Main.getPlugin(),vo));
		return true;
	}

}
