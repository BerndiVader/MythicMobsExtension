package com.gmail.berndivader.mythicmobsext.compatibility.worldguard;

import java.util.Comparator;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

public class MemberRegion extends AbstractCustomCondition implements IEntityCondition {

	enum TestForEnum {
		MEMBER, OWNER;
	}

	boolean debug, use_priority;
	TestForEnum test_for;

	public MemberRegion(String line, MythicLineConfig mlc) {
		super(line, mlc);

		use_priority = mlc.getBoolean("usepriority", false);
		try {
			test_for = TestForEnum.valueOf(mlc.getString("member", TestForEnum.MEMBER.name()).toUpperCase());
		} catch (Exception ex) {
			Main.logger.warning("line has invalid member type using default");
			test_for = TestForEnum.MEMBER;
		}

	}

	@Override
	public boolean check(AbstractEntity abstract_entity) {
		Entity entity = abstract_entity.getBukkitEntity();
		Location location = entity.getLocation();
		Set<ProtectedRegion> regions = WorldGuardUtils.getRegionsByLocation(location);
		if (regions != null) {
			if (use_priority) {
				ProtectedRegion region = regions.stream().max(Comparator.comparing(ProtectedRegion::getPriority)).get();
				return test_for == TestForEnum.OWNER ? checkForOwners(region, entity) : checkForMembers(region, entity);
			} else {
				for (ProtectedRegion region : regions) {
					if ((test_for == TestForEnum.OWNER ? checkForOwners(region, entity)
							: checkForMembers(region, entity)))
						return true;
				}
			}
		}
		return false;
	}

	boolean checkForOwners(ProtectedRegion region, Entity entity) {
		return region.getOwners().getUniqueIds().contains(entity.getUniqueId());
	}

	boolean checkForMembers(ProtectedRegion region, Entity entity) {
		return region.getMembers().getUniqueIds().contains(entity.getUniqueId());
	}

}
