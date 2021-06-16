package com.gmail.berndivader.mythicmobsext.mechanics;

import io.lumine.xikage.mythicmobs.skills.AbstractSkill;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.externals.*;

import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedLocationSkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

@ExternalAnnotation(name = "breakblock_ext", author = "BerndiVader")
public class BreakBlock extends SkillMechanic implements ITargetedLocationSkill {
	int restore;
	BlockFace block_face;
	boolean play_effect;

	public BreakBlock(String skill, MythicLineConfig mlc) {
		super(skill, mlc);
		this.threadSafetyLevel = AbstractSkill.ThreadSafetyLevel.SYNC_ONLY;

		restore = mlc.getInteger("restore", 0);
		play_effect = mlc.getBoolean("effect", false);
		try {
			block_face = BlockFace.valueOf(mlc.getString("blockface", "down").toUpperCase());
		} catch (Exception e) {
			block_face = BlockFace.DOWN;
		}
	}

	@Override
	public boolean castAtLocation(SkillMetadata data, AbstractLocation al) {
		Block block = BukkitAdapter.adapt(al).getBlock().getRelative(block_face);
		if (block.getType() != Material.AIR) {
			final BlockState block_state = block.getState();
			block.breakNaturally();
			if (restore > 0) {
				new BukkitRunnable() {
					@Override
					public void run() {
						block_state.getBlock().setType(block_state.getType());
						block_state.update();
						block_state.getWorld().playEffect(block_state.getLocation(), Effect.STEP_SOUND,
								block_state.getType());
					}
				}.runTaskLater(Main.getPlugin(), this.restore);
			}
			return true;
		}
		return false;
	}
}
