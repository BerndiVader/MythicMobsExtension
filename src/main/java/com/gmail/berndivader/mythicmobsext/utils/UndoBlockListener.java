package com.gmail.berndivader.mythicmobsext.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.Main;

public class UndoBlockListener implements Listener {

	private static Collection<Material>dList;
	public static Double getRandomVel(Double min, Double max) {
		return Main.random.nextDouble()*(max-min)+min;
	}
	
	static {
		dList=new ArrayList<Material>(Arrays.asList(
				Material.CHEST,
				Material.TRAPPED_CHEST,
				Material.ENDER_CHEST,
				Material.ANVIL,
				Material.DISPENSER,
				Material.BREWING_STAND,
				Material.FURNACE));
	}

	@EventHandler
	public void removeFallingBlock(EntityChangeBlockEvent e) {
		if (!e.getEntity().hasMetadata("removeit")) return;
		e.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	private void GrenadeBlockDestroy(EntityExplodeEvent e) {
		if (e.getEntityType()==EntityType.PRIMED_TNT&&!e.isCancelled()&&!e.blockList().isEmpty()) {
			final Entity e1=e.getEntity();
			if (!e1.hasMetadata("customgrenade")) return;
			if (e1.hasMetadata("noblkdmg")) {
				if (!e1.getMetadata("noblkdmg").get(0).asBoolean()) {
					e.setCancelled(true);
					e.getLocation().getWorld().createExplosion(e.getLocation().getX(), e.getLocation().getY(),
							e.getLocation().getZ(), 2, false, false);
					return;
				}
			}
			if (e1.hasMetadata("undotnt")&&!e1.getMetadata("undotnt").get(0).asBoolean()) return;
			final boolean ueffect=e1.hasMetadata("ueffect")?e1.getMetadata("ueffect").get(0).asBoolean():false;
			final int utime=e1.hasMetadata("utime")?e1.getMetadata("utime").get(0).asInt():0;
			e.setYield(0);
			final List<BlockState> blocks=new ArrayList<BlockState>();
			ListIterator<Block> lblocks=e.blockList().listIterator();
			while (lblocks.hasNext()) {
				Block b = lblocks.next();
				if (dList.contains(b.getType())) {
					lblocks.remove();
				} else {
					b.getDrops().clear();
					if (b.getType() != Material.AIR && !blocks.contains(b.getState())) {
						blocks.add(b.getState());
						@SuppressWarnings("deprecation")
						FallingBlock fb = b.getWorld().spawnFallingBlock(b.getLocation(), Material.FIRE, (byte) 0);
						fb.setVelocity(
								new Vector(getRandomVel(-0.5, 0.5), getRandomVel(0.1, 0.5), getRandomVel(-0.5, 0.5)));
						fb.setMetadata("removeit", new FixedMetadataValue(Main.getPlugin(), true));
					}
				}
			}
			Collections.shuffle(blocks);
			new BukkitRunnable() {
				int i=-1;
				@Override
				@SuppressWarnings("deprecation")
				public void run() {
					if (i<blocks.size()) {
						i++;
						BlockState bs = blocks.get(i);
						bs.getBlock().setType(bs.getType());
						bs.getBlock().setData(bs.getBlock().getData());
						bs.update();
						if (ueffect)
							bs.getBlock().getWorld().playEffect(bs.getLocation(), Effect.STEP_SOUND,bs.getBlock().getType());
					} else {
						blocks.clear();
						this.cancel();
					}
				}
			}.runTaskTimer(Main.getPlugin(),utime,1);
		}
	}
}
