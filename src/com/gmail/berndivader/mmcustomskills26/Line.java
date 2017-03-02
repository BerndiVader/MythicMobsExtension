package com.gmail.berndivader.mmcustomskills26;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import com.sk89q.worldedit.Vector;

public class Line extends forms {

	public Line () {
		blocks = new ArrayList<BlockState>();
		rand = new Random();
		return;
	}
	
    public void drawLine(Location s, Location d, Material material, double radius, int undo, boolean mask) {
    	this.undo = undo;
		if (!mask) {Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());}
        makeLine(s.getWorld(), material, s, d, radius, undo, mask);
    }
    
    public void makeLine(World w, Material material, Location s, Location d, double radius, int undo, boolean mask) {
        boolean notdrawn = true;
        Vector pos1 = new Vector(s.getX(), s.getY(), s.getZ());
        Vector pos2 = new Vector(d.getX(), d.getY(), d.getZ());

        int x1 = pos1.getBlockX(), y1 = pos1.getBlockY(), z1 = pos1.getBlockZ();
        int x2 = pos2.getBlockX(), y2 = pos2.getBlockY(), z2 = pos2.getBlockZ();
        int tipx = x1, tipy = y1, tipz = z1;
        int dx = Math.abs(x2 - x1), dy = Math.abs(y2 - y1), dz = Math.abs(z2 - z1);
        if (dx + dy + dz == 0) {
            blkset.add(new Vector(tipx, tipy, tipz)); notdrawn = false;
        }
        if (Math.max(Math.max(dx, dy), dz) == dx && notdrawn) {
            for (int domstep = 2; domstep <= dx-2; domstep++) {
                tipx = x1 + domstep * (x2 - x1 > 0 ? 1 : -1);
                tipy = (int) Math.round(y1 + domstep * ((double) dy) / ((double) dx) * (y2 - y1 > 0 ? 1 : -1));
                tipz = (int) Math.round(z1 + domstep * ((double) dz) / ((double) dx) * (z2 - z1 > 0 ? 1 : -1));
                blkset.add(new Vector(tipx, tipy, tipz));
            }
            notdrawn = false;
        }
        if (Math.max(Math.max(dx, dy), dz) == dy && notdrawn) {
            for (int domstep = 0; domstep <= dy; domstep++) {
                tipy = y1 + domstep * (y2 - y1 > 0 ? 1 : -1);
                tipx = (int) Math.round(x1 + domstep * ((double) dx) / ((double) dy) * (x2 - x1 > 0 ? 1 : -1));
                tipz = (int) Math.round(z1 + domstep * ((double) dz) / ((double) dy) * (z2 - z1 > 0 ? 1 : -1));
                blkset.add(new Vector(tipx, tipy, tipz));
            }
            notdrawn = false;
        }
        if (Math.max(Math.max(dx, dy), dz) == dz && notdrawn) {
            for (int domstep = 0; domstep <= dz; domstep++) {
                tipz = z1 + domstep * (z2 - z1 > 0 ? 1 : -1);
                tipy = (int) Math.round(y1 + domstep * ((double) dy) / ((double) dz) * (y2-y1>0 ? 1 : -1));
                tipx = (int) Math.round(x1 + domstep * ((double) dx) / ((double) dz) * (x2-x1>0 ? 1 : -1));
                blkset.add(new Vector(tipx, tipy, tipz));
            }
            notdrawn = false;
        }
        blkset = getBallooned(blkset, radius); blkset = getHollowed(blkset);
        if (mask) {
        	setBlocksMask(w, material);
        } else {
        	setBlocks(w, material);
        }
		return;
    }
}
