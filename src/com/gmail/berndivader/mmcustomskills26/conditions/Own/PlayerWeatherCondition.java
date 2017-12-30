package com.gmail.berndivader.mmcustomskills26.conditions.Own;

import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mmcustomskills26.conditions.mmCustomCondition;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class PlayerWeatherCondition extends mmCustomCondition
implements
IEntityCondition {
	private WeatherType type;
	
	public PlayerWeatherCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		String s=mlc.getString(new String[] {"weather","w"},"CLEAR").toUpperCase();
		try {
			this.type=WeatherType.valueOf(s);
		} catch (Exception e) {
			this.type=WeatherType.CLEAR;
		}
	}

	@Override
	public boolean check(AbstractEntity target) {
		if (target.isPlayer()) {
			Player p=(Player)target.getBukkitEntity();
			return p.getPlayerWeather().equals(this.type);
		}
		return false;
	}

}
