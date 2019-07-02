package com.gmail.berndivader.mythicmobsext.bossbars.mechanics;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.bossbars.BossBars;
import com.gmail.berndivader.mythicmobsext.bossbars.SegmentedEnum;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public 
class
CreateBossBar 
extends
SkillMechanic
implements
ITargetedEntitySkill
{
	String title;
	BarStyle style;
	BarColor color;
	List<BarFlag>flags;
	int flags_size;
	String expr;
	
	public CreateBossBar(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		title=mlc.getString("title","Bar");
		style=BarStyle.valueOf(SegmentedEnum.real(mlc.getInteger("segment",6)).name());
		expr=mlc.getString("value","0.05d");
		try {
			color=BarColor.valueOf(mlc.getString("color","RED").toUpperCase());
		} catch (Exception ex) {
			Main.logger.info(mlc.getString("color")+" is not valid for BarColor in "+skill);
			color=BarColor.RED;
		}
		flags=new ArrayList<>();
		String[]arr=mlc.getString("flags","").toUpperCase().split(",");
		int size=arr.length;
		for(int i1=0;i1<size;i1++) {
			String parse=arr[i1];
			if(!parse.isEmpty()) {
				BarFlag flag=null;
				try {
					flag=BarFlag.valueOf(parse);
				} catch (Exception ex) {
					Main.logger.info(parse+" is no valid BarFlag in "+skill);
				}
				if(flag!=null) flags.add(flag);
			}
		}
		flags_size=flags.size();
	}

	@Override
	public boolean castAtEntity(SkillMetadata data, AbstractEntity abstract_entity) {
		BossBar bar=Bukkit.createBossBar(title,color,style);
		if(bar!=null) {
			double default_value=1d;
			String parsed_expr=Utils.parseMobVariables(expr,data,data.getCaster().getEntity(),abstract_entity,null);
			try {
				default_value=Double.parseDouble(parsed_expr);
			}catch (Exception e) {
				Main.logger.info(parsed_expr+" is not valid for double in "+this.line);
				default_value=0d;
			}				
			bar.setProgress(default_value);
			for(int i1=0;i1<flags_size;i1++) {
				bar.addFlag(flags.get(i1));
			}
			BossBars.addBar(abstract_entity.getUniqueId(),bar);
			return true;
		}
		return false;
	}
	
}
