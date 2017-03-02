package com.gmail.berndivader.mmcustomskills26;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import com.sk89q.worldedit.Vector;

public class Pyramid extends forms {

	public Pyramid () {
		blocks = new ArrayList<BlockState>();
		rand = new Random();
		return;
	}
	
    public void drawPyramid(Location l, Material material, int size, int undo, boolean mask) {
    	this.undo = undo;
        Vector pos = new Vector(l.getX(), l.getY(), l.getZ());
		if (!mask) {Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());}
        makePyramid(l.getWorld(), pos, material, size, false, mask);
    }

    public void makePyramid(World w, Vector pos, Material material, int size, boolean filled, boolean mask) {
        int height = size;
        for (int y = 0; y <= height; ++y) {
            size--;
            for (int x = 0; x <= size; ++x) {
                for (int z = 0; z <= size; ++z) {
                    if ((filled && z <= size && x <= size) || z == size || x == size) {
                        blkset.add(pos.add(x, y, z));
                        blkset.add(pos.add(-x, y, z));
                        blkset.add(pos.add(x, y, -z));
                        blkset.add(pos.add(-x, y, -z));
                    }
                }
            }
        }
        if (mask) {
        	setBlocksMask(w, material);
        } else {
        	setBlocks(w, material);
        }
		return;
    }
}
