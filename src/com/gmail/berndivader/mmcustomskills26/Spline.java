package com.gmail.berndivader.mmcustomskills26;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockState;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.math.interpolation.Interpolation;
import com.sk89q.worldedit.math.interpolation.KochanekBartelsInterpolation;
import com.sk89q.worldedit.math.interpolation.Node;

public class Spline extends forms {

	public Spline () {
		blocks = new ArrayList<BlockState>();
		rand = new Random();
		return;
	}
	
    public void drawSpline(Location s, Location d, Material material, double range, 
    		double tension, double bias, double continuity, double quality, double radius, int undo, boolean mask) {
    	this.undo = undo;
    	double tx = (d.getX()-s.getX())/4;
    	double tz = (d.getZ()-s.getZ())/4;
    	double ty = (d.getY()-s.getY())/4;
    	double vx=0,vz=0,vy=0;
    	double y=range;
    	nodevectors.add(new Vector(s.getX(), s.getY(), s.getZ()));
    	for (int a=1;a<=4;a++) {
    		vx = s.getX()+((tx*a)+y);
        	vz = s.getZ()+((tz*a)+y);
        	vy = s.getY()+((ty*a)+y);
        	y=-y;
            nodevectors.add(new Vector(vx, vy, vz));
    	}
        nodevectors.add(new Vector(d.getX(), d.getY(), d.getZ()));
		if (!mask) {Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());}
        makeSpline(s.getWorld(), material, d, s, nodevectors, tension, bias, continuity, quality, radius, undo, mask);
    }
    
    public void makeSpline(World w, Material material, Location s, Location d, List<Vector> nodevectors, 
    		double tension, double bias, double continuity, double quality, double radius, int undo, boolean mask) {
        List<Node> nodes = new ArrayList<Node>(nodevectors.size());
        Interpolation interpol = new KochanekBartelsInterpolation();
        for (Vector nodevector : nodevectors) {
            Node n = new Node(nodevector);
            n.setTension(tension); n.setBias(bias); n.setContinuity(continuity); nodes.add(n);
        }
        interpol.setNodes(nodes);
        double splinelength = interpol.arcLength(0, 1);
        for (double loop = 0; loop <= 1; loop += 1D / splinelength / quality) {
            Vector tipv = interpol.getPosition(loop);
            int tipx = (int) Math.round(tipv.getX()); int tipy = (int) Math.round(tipv.getY());
            int tipz = (int) Math.round(tipv.getZ()); blkset.add(new Vector(tipx, tipy, tipz));
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
