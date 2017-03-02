package com.gmail.berndivader.mmcustomskills26;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import com.sk89q.worldedit.Vector;

public class Sphere extends forms {

	public Sphere () {
		blocks = new ArrayList<BlockState>();
		rand = new Random();
		return;
	}
	
    public void drawsphere(Location l, Material material, String radiusString, int undo, boolean mask) {
    	this.undo = undo;
    	String[] radii = radiusString.split(",");
        final double radiusX, radiusY, radiusZ;
        switch (radii.length) {
        case 1:
            radiusX = radiusY = radiusZ = Math.max(1, Double.parseDouble(radii[0]));
            break;
        case 3:
            radiusX = Math.max(1, Double.parseDouble(radii[0]));
            radiusY = Math.max(1, Double.parseDouble(radii[1]));
            radiusZ = Math.max(1, Double.parseDouble(radii[2]));
            break;
        default:
            Bukkit.getServer().getConsoleSender().sendMessage("You must either specify 1 or 3 radius values.");
            return;
        }
        Vector pos = new Vector(l.getX(), l.getY(), l.getZ());
		if (!mask) {Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());}
        makeSphere(l.getWorld(), pos, material, radiusX, radiusY, radiusZ, false, mask);
    }

    private void makeSphere(World w, Vector pos, Material material, double radiusX, double radiusY, double radiusZ, boolean filled, boolean mask) {
        radiusX += 0.5;
        radiusY += 0.5;
        radiusZ += 0.5;
        final double invRadiusX = 1 / radiusX;
        final double invRadiusY = 1 / radiusY;
        final double invRadiusZ = 1 / radiusZ;
        final int ceilRadiusX = (int) Math.ceil(radiusX);
        final int ceilRadiusY = (int) Math.ceil(radiusY);
        final int ceilRadiusZ = (int) Math.ceil(radiusZ);
        double nextXn = 0;
        forX: for (int x = 0; x <= ceilRadiusX; ++x) {
            final double xn = nextXn;
            nextXn = (x + 1) * invRadiusX;
            double nextYn = 0;
            forY: for (int y = 0; y <= ceilRadiusY; ++y) {
                final double yn = nextYn;
                nextYn = (y + 1) * invRadiusY;
                double nextZn = 0;
                forZ: for (int z = 0; z <= ceilRadiusZ; ++z) {
                    final double zn = nextZn;
                    nextZn = (z + 1) * invRadiusZ;
                    double distanceSq = lengthSq(xn, yn, zn);
                    if (distanceSq > 1) {
                        if (z == 0) {
                            if (y == 0) {
                                break forX;
                            }
                            break forY;
                        }
                        break forZ;
                    }
                    if (!filled) {
                        if (lengthSq(nextXn, yn, zn) <= 1 && lengthSq(xn, nextYn, zn) <= 1 && lengthSq(xn, yn, nextZn) <= 1) {
                            continue;
                        }
                    }
                    blkset.add(pos.add(x, y, z));
                    blkset.add(pos.add(-x, y, z));
                    blkset.add(pos.add(x, -y, z));
                    blkset.add(pos.add(x, y, -z));
                    blkset.add(pos.add(-x, -y, z));
                    blkset.add(pos.add(-x, y, -z));
                    blkset.add(pos.add(x, -y, -z));
                    blkset.add(pos.add(-x, -y, -z));
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
