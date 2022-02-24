package com.gmail.berndivader.mythicmobsext.healthbar;

import java.util.UUID;

import com.gmail.berndivader.mythicmobsext.Main;
import me.filoghost.holographicdisplays.api.beta.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.beta.hologram.Hologram;
import me.filoghost.holographicdisplays.api.beta.hologram.VisibilitySettings;
import me.filoghost.holographicdisplays.api.beta.hologram.line.TextHologramLine;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

public class Healthbar {

	protected Hologram hologram;
	protected LivingEntity entity;
	protected UUID uuid;
	protected double offset, sOffset, fOffset;
	protected String template;
	protected TextHologramLine textline;
	protected int showCounter, showCounterDefault;
	protected boolean useOffset, iYaw;

	public Healthbar(LivingEntity entity) {
		this(entity, 0d, -1, "$h", 0d, 0d, false);
	}

	public Healthbar(LivingEntity entity, double offset) {
		this(entity, offset, -1, "$h", 0d, 0d, false);
	}

	public Healthbar(LivingEntity entity, double offset, int showCounter, String l, double sOffset, double fOffset,
			boolean ignoreYaw) {
		hologram = HolographicDisplaysAPI.get(Main.getPlugin()).createHologram(entity.getLocation().add(0, offset, 0));

		this.fOffset = fOffset;
		this.sOffset = sOffset;
		this.iYaw = ignoreYaw;
		this.useOffset = fOffset != 0d || sOffset != 0d;
		if (this.useOffset) {
			Vector soV = MathUtils.getSideOffsetVectorFixed(entity.getLocation().getYaw(), this.sOffset, this.iYaw);
			Vector foV = MathUtils.getFrontBackOffsetVector(entity.getLocation().getDirection(), this.fOffset);
			hologram.getPosition().add(soV.getX(), soV.getY(), soV.getZ());
			hologram.getPosition().add(foV.getX(), foV.getY(), foV.getZ());
		}
		this.uuid = entity.getUniqueId();
		HealthbarHandler.healthbars.put(this.uuid, this);
		if (showCounter == -1) {
			this.showCounterDefault = -1;
			hologram.getVisibilitySettings().setGlobalVisibility(VisibilitySettings.Visibility.VISIBLE);
		} else {
			this.showCounterDefault = showCounter;
			hologram.getVisibilitySettings().setGlobalVisibility(VisibilitySettings.Visibility.HIDDEN);
		}
		if (!l.contains("$h"))
			l = "$h";
		this.template = l;
		this.entity = entity;
		this.offset = offset;
		this.showCounter = 0;
		this.textline = hologram.getLines().appendText(this.composeHealthLine());
	}

	public void updateHealth() {
		this.textline.setText(this.composeHealthLine());
		if (this.showCounterDefault > -1) {
			this.showCounter = this.showCounterDefault;
			hologram.getVisibilitySettings().setGlobalVisibility(VisibilitySettings.Visibility.VISIBLE);
		}
	}

	public boolean update() {
		if (hologram.isDeleted())
			return false;
		Location l = this.entity.getLocation();
		World w = l.getWorld();
		double x = l.getX();
		double y = l.getY();
		double z = l.getZ();
		if (this.showCounterDefault > -1) {
			if (this.showCounter == 0) {
				hologram.getVisibilitySettings().setGlobalVisibility(VisibilitySettings.Visibility.HIDDEN);
				this.showCounter = -1;
			} else {
				this.showCounter--;
			}
		}
		if (this.useOffset) {
			Vector soV = MathUtils.getSideOffsetVectorFixed(entity.getLocation().getYaw(), this.sOffset, this.iYaw);
			Vector foV = MathUtils.getFrontBackOffsetVector(entity.getLocation().getDirection(), this.fOffset);
			x += soV.getX() + foV.getX();
			z += soV.getZ() + foV.getZ();
		}
		hologram.setPosition(w, x, y + this.offset, z);
		return true;
	}

	public double getHealth() {
		double d = 0;
		d = MathUtils.round(this.entity.getHealth(), 0);
		return d;
	}

	public void remove() {
		HealthbarHandler.healthbars.remove(this.uuid);
		hologram.delete();
	}

	public void changeDisplay(String display) {
		if (display.contains("$h")) {
			this.template = display;
			this.updateHealth();
		}
	}

	private String composeHealthLine() {
		int hp = (int) this.getHealth();
		if (this.template.equals("$h")) {
			double percent = hp / this.entity.getMaxHealth();
			String sHP = String.valueOf(hp);
			int hplength = sHP.length();
			int length = 10 + hplength;
			int gray = (int) Math.floor(percent * (double) length);
			StringBuilder line = new StringBuilder().append((Object) ChatColor.DARK_RED).append("[");
			boolean passed = false;
			for (int i = 0; i < length; ++i) {
				if (!passed && i > gray) {
					passed = true;
				}
				if (i < 5) {
					line.append((Object) (passed ? ChatColor.DARK_GRAY : ChatColor.RED));
					line.append("|");
					continue;
				}
				if (i < 5 + hplength) {
					line.append((Object) (passed ? ChatColor.GRAY : ChatColor.DARK_RED));
					try {
						line.append(sHP.substring(i - 5, i - 4));
					} catch (Exception exception) {
					}
					continue;
				}
				if (i == hplength + 2 && !passed) {
					line.append((Object) ChatColor.RED);
				}
				line.append((Object) (passed ? ChatColor.DARK_GRAY : ChatColor.RED));
				line.append("|");
			}
			return line.append((Object) ChatColor.DARK_RED).append("]").toString();
		} else {
			String line = this.template;
			line = line.replace("$h", Integer.toString(hp));
			return line;
		}
	}
}
