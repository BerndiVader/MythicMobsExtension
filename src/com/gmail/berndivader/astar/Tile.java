package com.gmail.berndivader.astar;

import org.bukkit.Location;

public class Tile {
	// as offset from starting point
	private final short x, y, z;
	private double g = -1, h = -1;
	private Tile parent = null;
	private final String uid;

	public Tile(short x, short y, short z, Tile parent) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.parent = parent;
		uid = String.valueOf(x) + y + z;
	}

	public boolean isInRange(int range) {
		return ((range - Math.abs(x) >= 0) && (range - Math.abs(y) >= 0) && (range - Math.abs(z) >= 0));
	}

	public void setParent(Tile parent) {
		this.parent = parent;
	}

	public Location getLocation(Location start) {
		return new Location(start.getWorld(), start.getBlockX() + x, start.getBlockY() + y, start.getBlockZ() + z);
	}

	public Tile getParent() {
		return this.parent;
	}

	public short getX() {
		return x;
	}

	public int getX(Location i) {
		return (i.getBlockX() + x);
	}

	public short getY() {
		return y;
	}

	public int getY(Location i) {
		return (i.getBlockY() + y);
	}

	public short getZ() {
		return z;
	}

	public int getZ(Location i) {
		return (i.getBlockZ() + z);
	}

	public String getUID() {
		return this.uid;
	}

	public boolean equals(Tile t) {
		return (t.getX() == x && t.getY() == y && t.getZ() == z);
	}

	public void calculateBoth(int sx, int sy, int sz, int ex, int ey, int ez, boolean update) {
		this.calculateG(update);
		this.calculateH(sx, sy, sz, ex, ey, ez, update);
	}

	public void calculateH(int sx, int sy, int sz, int ex, int ey, int ez, boolean update) {
		// only update if h hasn't been calculated or if forced
		if ((!update && h == -1) || update) {
			if (AStar.getAlgorithm() == PathfindAlgorithm.DIJKSTRA) {
				this.h = 0;
			} else {
				int hx = sx + x, hy = sy + y, hz = sz + z;
				this.h = this.getEuclideanDistance(hx, hy, hz, ex, ey, ez) * (AStar.getAlgorithm() == PathfindAlgorithm.BEST_FIRST ? 1_000_000 : 1);
			}
		}
	}

	// G = the movement cost to move from the starting point A to a given square
	// on the grid, following the path generated to get there.
	public void calculateG(boolean update) {

		if (update || g == -1) {

			// only update if g hasn't been calculated or if forced
			Tile currentParent, currentTile = this;
			int gCost = 0;
			// follow path back to start
			while ((currentParent = currentTile.getParent()) != null) {

				int dx = currentTile.getX() - currentParent.getX(), dy = currentTile.getY() - currentParent.getY(), dz =
					currentTile.getZ() - currentParent.getZ();

				dx = Math.abs(dx);
				dy = Math.abs(dy);
				dz = Math.abs(dz);
				if (dx == 1 && dy == 1 && dz == 1) {
					gCost += 1.7;
				} else if ((dx == 1 || dz == 1) && (dy == 1 || dy == 0)) {
					gCost += 1.4;
				} else {
					gCost += 1.0;
				}
				// move backwards a tile
				currentTile = currentParent;
			}
			this.g = gCost;
		}

	}

	public double getG() {
		return g;
	}

	public double getH() {
		return h;
	}

	public double getF() {
		return h+g;
	}

	private double getEuclideanDistance(int sx,int sy,int sz,int ex,int ey,int ez) {
		double dx=sx-ex,dy=sy-ey,dz=sz-ez;
		return Math.sqrt((dx*dx)+(dy*dy)+(dz*dz));
	}

}