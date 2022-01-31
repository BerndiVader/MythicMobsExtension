package com.gmail.berndivader.mythicmobsext.healthbar;

import java.util.UUID;

import com.gmail.berndivader.mythicmobsext.Main;
import me.filoghost.holographicdisplays.api.beta.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.beta.hologram.Hologram;
import me.filoghost.holographicdisplays.api.beta.hologram.HologramLines;
import me.filoghost.holographicdisplays.api.beta.hologram.VisibilitySettings;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

public class SpeechBubble {
	protected Hologram hologram;
	protected LivingEntity entity;
	protected UUID uuid;
	protected double offset;
	protected double sOffset;
	protected double fOffset;
	protected String[] template;
	protected int counter;
	protected int maxlines;
	protected int il1 = 0;
	protected int ll;
	protected boolean useOffset;
	protected boolean uc1;
	protected String id;

	public SpeechBubble(LivingEntity entity, String[] text) {
		this(entity, "bubble", entity.getLocation(), 0d, -1, text, 0d, 0d, false, 30, true);
	}

	public SpeechBubble(LivingEntity entity, String[] text, int ll) {
		this(entity, "bubble", entity.getLocation(), 0d, -1, text, 0d, 0d, false, ll, true);
	}

	public SpeechBubble(LivingEntity entity, String s1, Location l1, double offset, int showCounter, String[] text,
			double sOffset, double fOffset, boolean b1, int ll, boolean b2) {
		hologram = HolographicDisplaysAPI.get(Main.getPlugin()).createHologram(l1);
		this.id = s1;
		this.ll = ll;
		this.fOffset = fOffset;
		this.sOffset = sOffset;
		this.maxlines = -1;
		this.useOffset = fOffset != 0d || sOffset != 0d;
		if (this.useOffset && b1) {
			Vector soV = MathUtils.getSideOffsetVectorFixed(entity.getLocation().getYaw(), this.sOffset, false);
			Vector foV = MathUtils.getFrontBackOffsetVector(entity.getLocation().getDirection(), this.fOffset);
			hologram.getPosition().add(soV.getX(), soV.getY(), soV.getZ());
			hologram.getPosition().add(foV.getX(), foV.getY(), foV.getZ());
		}
		this.uc1 = b2;
		this.uuid = entity.getUniqueId();
		HealthbarHandler.speechbubbles.put(this.uuid.toString() + this.id, this);
		this.counter = showCounter < 1 ? 60 : showCounter * 20;
		hologram.getVisibilitySettings().setGlobalVisibility(VisibilitySettings.Visibility.VISIBLE);
		this.counter = showCounter;
		this.entity = entity;
		this.offset = offset;
		this.template = text;
		lines();
	}

	public boolean update() {
		if (hologram.isDeleted())
			return false;
		Location l = this.entity.getLocation();
		World w = l.getWorld();
		double dx = l.getX();
		double dy = l.getY();
		double dz = l.getZ();
		double do1 = (hologram.getLines().size() * 0.25) + (il1 * 0.5) + this.offset;
		if (this.useOffset) {
			Vector soV = MathUtils.getSideOffsetVectorFixed(entity.getLocation().getYaw(), this.sOffset, false);
			Vector foV = MathUtils.getFrontBackOffsetVector(entity.getLocation().getDirection(), this.fOffset);
			dx += soV.getX() + foV.getX();
			dz += soV.getZ() + foV.getZ();
		}
		hologram.setPosition(w, dx, dy + do1, dz);
		if (uc1) {
			this.counter--;
			if (this.counter < 0)
				this.remove();
		}
		return true;
	}

	public void remove() {
		HealthbarHandler.speechbubbles.remove(this.uuid.toString() + this.id);
		hologram.delete();
	}

	public void lines() {
		HologramLines lines = hologram.getLines();
		lines.clear();
		this.il1 = 0;
		for (String l : this.template) {
			if (l.contains("<additem.")) {
				String a1, a3;
				a1 = "<additem." + (a3 = l.split("<additem.")[1].split(">")[0]) + ">";
				String[] a2 = (l.replace(a1, "<split>")).split("<split>");
				if (a2.length > 0)
					lines.appendText(a2[0]);
				Material m1;
				if ((m1 = Material.getMaterial(a3.toUpperCase())) != null) {
					lines.appendItem(new ItemStack(m1));
					il1++;
				}
				if (a2.length > 1)
					lines.appendText(a2[1]);
			} else {
				lines.appendText(l);
			}
		}
	}
}
