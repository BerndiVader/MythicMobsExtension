package com.gmail.berndivader.mmcustomskills26;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldedit.Vector;

public class forms implements Listener {
	List<BlockState> blocks;
	int undo;
	Random rand;
	List<Vector> nodevectors = new ArrayList<Vector>();
	Set<Vector> blkset = new HashSet<Vector>();
	
    public void regen(final List<BlockState> blocks, boolean effect, int speed) {
        Collections.shuffle(blocks);
        forms c = this;
    	new BukkitRunnable() {
            int i = -1;
            public void run() {
                if (i != blocks.size() - 1) {
                    i++; BlockState bs = blocks.get(i);
                    if (effect) bs.getBlock().getWorld().playEffect(bs.getLocation(), Effect.STEP_SOUND, 1);
                    bs.getBlock().setType(Material.AIR);
                } else {
                    blocks.clear(); this.cancel();
                    HandlerList.unregisterAll(c);
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 0);
    }
    public void regenMask(final List<BlockState> blocks, boolean effect, int speed) {
        Collections.shuffle(blocks);
    	new BukkitRunnable() {
            int i = -1;
            @SuppressWarnings("deprecation")
			public void run() {
                if (i != blocks.size() - 1) {
                    i++; BlockState bs = blocks.get(i);
                    if (effect) {
                    	if (bs.getType()==Material.AIR) {
                        	bs.getBlock().getWorld().playEffect(bs.getLocation(), Effect.STEP_SOUND, 1);
                    	} else {
                    		bs.getBlock().getWorld().playEffect(bs.getLocation(), Effect.STEP_SOUND, bs.getBlock().getType());
                    	}
                    }
                    for (Player p : bs.getWorld().getPlayers()) {
                        if (p.getLocation().distanceSquared(bs.getLocation()) >= 65536.0) continue;
                        p.sendBlockChange(bs.getLocation(), bs.getLocation().getBlock().getType(), (byte) 0);
                    }
                } else {
                    blocks.clear(); this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.getPlugin(), 0, 0);
    }
    
    public void setBlocksMask(World w, Material material) {
        new BukkitRunnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
		        Block blk = null;
		        for (Vector vec : blkset) {
		            blk = w.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
		            if (blk.getType()==Material.AIR) {
                        for (Player p : blk.getWorld().getPlayers()) {
                            if (p.getLocation().distanceSquared(blk.getLocation()) >= 65536.0) continue;
                            p.sendBlockChange(blk.getLocation(), material, (byte) 0);
                        }
		            	blocks.add(blk.getState());
		            	w.playEffect(blk.getLocation(), Effect.SMOKE, rand.nextInt(9));
		            }
		        }
				new BukkitRunnable() {
					public void run() {regenMask(blocks, true, 1);}
				}.runTaskLaterAsynchronously(Main.getPlugin(), undo);
			}
		}.runTaskAsynchronously(Main.getPlugin());
    }
    
    public void setBlocks(World w, Material material) {
        new BukkitRunnable() {
			@Override
			public void run() {
		        Block blk = null;
		        for (Vector vec : blkset) {
		            blk = w.getBlockAt(vec.getBlockX(), vec.getBlockY(), vec.getBlockZ());
		            if (blk.getType()==Material.AIR) {
                    	blk.setType(material); blocks.add(blk.getState());
		            	w.playEffect(blk.getLocation(), Effect.SMOKE, rand.nextInt(9));
		            }
		        }
				new BukkitRunnable() {
					public void run() {regen(blocks, true, 1);}
				}.runTaskLater(Main.getPlugin(), undo);
			}
		}.runTask(Main.getPlugin());
    }
    
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
    	if (blocks.contains(e.getBlock().getState())) {
    		e.setCancelled(true);
    		e.getBlock().setType(Material.AIR);
			blocks.remove(e.getBlock().getState());
    	}
    	if (blocks.isEmpty()) HandlerList.unregisterAll(this);
    }

    public static double lengthSq(double x, double z) {
        return (x * x) + (z * z);
    }

    public static Set<Vector> getBallooned(Set<Vector> vset, double radius) {
        Set<Vector> returnset = new HashSet<Vector>();
        int ceilrad = (int) Math.ceil(radius);
        for (Vector v : vset) {
            int tipx = v.getBlockX(), tipy = v.getBlockY(), tipz = v.getBlockZ();
            for (int loopx = tipx - ceilrad; loopx <= tipx + ceilrad; loopx++) {
                for (int loopy = tipy - ceilrad; loopy <= tipy + ceilrad; loopy++) {
                    for (int loopz = tipz - ceilrad; loopz <= tipz + ceilrad; loopz++) {
                        if (hypot(loopx - tipx, loopy - tipy, loopz - tipz) <= radius) {
                            returnset.add(new Vector(loopx, loopy, loopz));
                        }
                    }
                }
            }
        }
        return returnset;
    }

    public static Set<Vector> getHollowed(Set<Vector> vset) {
        Set<Vector> returnset = new HashSet<Vector>();
        for (Vector v : vset) {
            double x = v.getX(), y = v.getY(), z = v.getZ();
            if (!(vset.contains(new Vector(x + 1, y, z)) &&
            vset.contains(new Vector(x - 1, y, z)) &&
            vset.contains(new Vector(x, y + 1, z)) &&
            vset.contains(new Vector(x, y - 1, z)) &&
            vset.contains(new Vector(x, y, z + 1)) &&
            vset.contains(new Vector(x, y, z - 1)))) {
                returnset.add(v);
            }
        }
        return returnset;
    }

    public static double hypot(double... pars) {
        double sum = 0;
        for (double d : pars) {
            sum += Math.pow(d, 2);
        }
        return Math.sqrt(sum);
    }    

    public static double lengthSq(double x, double y, double z) {
        return (x * x) + (y * y) + (z * z);
    }
}
