package com.gmail.berndivader.mmcustomskills26;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitUtil;

public class Cylinder extends forms {

	public Cylinder () {
		blocks = new ArrayList<BlockState>();
		rand = new Random();
		return;
	}
	
    public void drawCylinder(Location l, Material material, String radiusString, int height, int undo, boolean mask) {
    	this.undo = undo;
    	String[] radii = radiusString.split(",");
        final double radiusX, radiusZ;
        switch (radii.length) {
        case 1:
            radiusX = radiusZ = Math.max(1, Double.parseDouble(radii[0]));
            break;
        case 2:
            radiusX = Math.max(1, Double.parseDouble(radii[0]));
            radiusZ = Math.max(1, Double.parseDouble(radii[1]));
            break;
        default:
            Bukkit.getServer().getConsoleSender().sendMessage("You must either specify 1 or 2 radius values.");
            return;
        }
        Vector pos = new Vector(l.getX(), l.getY()-1, l.getZ());
		if (!mask) {Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());}
        makeCylinder(l.getWorld(), pos, material, radiusX, radiusZ, height, mask);
    }

    private void makeCylinder(World w, Vector pos, Material material, double radiusX, double radiusZ, int height, boolean mask) {
    	radiusX += 0.5;
        radiusZ += 0.5;
        com.sk89q.worldedit.world.World world;
        world = BukkitUtil.getLocalWorld(w);
        if (height == 0) {return;}
        else if (height < 0) {
            height = -height; pos = pos.subtract(0, height, 0);
        }
        if (pos.getBlockY() < 0) {
            pos = pos.setY(0);
        } else if (pos.getBlockY() + height - 1 > world.getMaxY()) {
            height = world.getMaxY() - pos.getBlockY() + 1;
        }
        final double invRadiusX = 1 / radiusX;
        final double invRadiusZ = 1 / radiusZ;
        final int ceilRadiusX = (int) Math.ceil(radiusX);
        final int ceilRadiusZ = (int) Math.ceil(radiusZ);
        double nextXn = 0;
        forX: for (int x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextZn = 0;
            forZ: for (int z = 0; z <= ceilRadiusZ; ++z) {
                final double zn = nextZn;
                nextZn = (z + 1) * invRadiusZ;
                double distanceSq = lengthSq(xn, zn);
                if (distanceSq > 1) {
                    if (z == 0) {
                        break forX;
                    }
                    break forZ;
                }
                if (lengthSq(nextXn, zn) <= 1 && lengthSq(xn, nextZn) <= 1) {
                   continue;
                }
                for (int y = 0; y < height; ++y) {
                    blkset.add(pos.add(x, y, z));
                    blkset.add(pos.add(-x, y, z));
                    blkset.add(pos.add(x, y, -z));
                    blkset.add(pos.add(-x, y, -z));
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
