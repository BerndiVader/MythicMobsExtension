package com.gmail.berndivader.healthbar;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;
import com.gmail.filoghost.holographicdisplays.object.CraftHologram;

public class Healthbar extends CraftHologram {
	protected LivingEntity entity;
	protected UUID uuid;
	protected double offset;
	protected String template;
	protected TextLine textline;
	
	public Healthbar(LivingEntity entity) {
		this(entity, 0.0d, "$h");
	}
	public Healthbar(LivingEntity entity, double offset) {
		this(entity,offset,"$h");
	}
	public Healthbar(LivingEntity entity, double offset, String l) {
		super(entity.getLocation().add(0, offset, 0));
		this.uuid = entity.getUniqueId();
		if (!l.contains("$h")) l = "$h";
		this.template = l;
		this.entity = entity;
		this.offset = offset;
		HealthbarHandler.healthbars.put(this.uuid, this);
		this.textline = this.appendTextLine(this.composeHealthLine());
	}

	public void updateHealth() {
		this.textline.setText(this.composeHealthLine());
	}

	@Override
	public boolean update() {
		if (this.isDeleted()) return false;
		Location l = this.entity.getLocation();
		World w = l.getWorld();
		double x = l.getX();
		double y = l.getY();
		double z = l.getZ();
		this.teleport(w, x, y+this.offset, z);
		return true;
	}
	
	public double getHealth() {
		double d = 0;
		d = CustomSkillStuff.round(this.entity.getHealth(), 0);
		return d;
	}
	
	public void remove() {
		HealthbarHandler.healthbars.remove(this.uuid);
		this.delete();
	}
	
	public void changeDisplay(String display) {
		if (display.contains("$h")) {
			this.template=display;
			this.updateHealth();
		}
	}
	
    private String composeHealthLine() {
        int hp = (int)this.getHealth();
        if (this.template.equals("$h")) {
            double percent = hp / this.entity.getMaxHealth();
            String sHP = String.valueOf(hp);
            int hplength = sHP.length();
            int length = 10 + hplength;
            int gray = (int)Math.floor(percent * (double)length);
            StringBuilder line = new StringBuilder().append((Object)ChatColor.DARK_RED).append("[");
            boolean passed = false;
            for (int i = 0; i < length; ++i) {
                if (!passed && i > gray) {
                    passed = true;
                }
                if (i < 5) {
                    line.append((Object)(passed ? ChatColor.DARK_GRAY : ChatColor.RED));
                    line.append("|");
                    continue;
                }
                if (i < 5 + hplength) {
                    line.append((Object)(passed ? ChatColor.GRAY : ChatColor.DARK_RED));
                    try {
                        line.append(sHP.substring(i - 5, i - 4));
                    }
                    catch (Exception exception) {}
                    continue;
                }
                if (i == hplength + 2 && !passed) {
                    line.append((Object)ChatColor.RED);
                }
                line.append((Object)(passed ? ChatColor.DARK_GRAY : ChatColor.RED));
                line.append("|");
            }
            return line.append((Object)ChatColor.DARK_RED).append("]").toString();
        } else {
        	String line=this.template;
        	line=line.replace("$h", Integer.toString(hp));
        	return line;
        }
    }	
}
