package com.gmail.berndivader.mythicmobsext.conditions;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.externals.*;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.ILocationCondition;

@ExternalAnnotation(name = "behindnearestplayer", author = "BerndiVader")
public class LocationBehindNearestPlayerCondition extends AbstractCustomCondition implements ILocationCondition {
	double viewAngle;
	double range;

	public LocationBehindNearestPlayerCondition(String line, MythicLineConfig mlc) {
		super(line, mlc);
		this.viewAngle = MathUtils.round(mlc.getDouble(new String[] { "view", "angle", "v" }, 45.0D), 3);
		this.range = Math.pow(mlc.getDouble("range", 32), 2d);
	}

	@Override
	public boolean check(AbstractLocation arg0) {
		Location source = BukkitAdapter.adapt(arg0);
		List<Player> players = Utils.getPlayersInRange(source, range);
		int size = players.size();
		Double nearest_distance = null;
		Location nearest_location = null;
		for (int i1 = 0; i1 < size; i1++) {
			Location l = players.get(i1).getLocation().clone();
			if (nearest_distance == null) {
				nearest_distance = Math.abs(l.distanceSquared(source));
				nearest_location = l.clone();
			} else {
				double check_distance = Math.abs(l.distanceSquared(source));
				if (check_distance < nearest_distance) {
					nearest_distance = check_distance;
					nearest_location = l.clone();
				}
			}
		}
		if (nearest_location != null) {
			return check(nearest_location, source, this.viewAngle);
		}
		return false;
	}

	private static boolean check(Location s, Location t, double viewAngle) {
		double dT = Math.cos(viewAngle);
		Vector f = s.getDirection();
		Vector r = s.subtract(t).toVector().normalize();
		return Math.toDegrees(Math.asin(f.dot(r))) >= dT;
	}

}
