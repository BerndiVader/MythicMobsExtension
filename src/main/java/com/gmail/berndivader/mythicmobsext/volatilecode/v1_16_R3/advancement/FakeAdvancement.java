package com.gmail.berndivader.mythicmobsext.volatilecode.v1_16_R3.advancement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.server.v1_16_R3.Advancement;
import net.minecraft.server.v1_16_R3.AdvancementDisplay;
import net.minecraft.server.v1_16_R3.AdvancementProgress;
import net.minecraft.server.v1_16_R3.AdvancementRewards;
import net.minecraft.server.v1_16_R3.Criterion;
import net.minecraft.server.v1_16_R3.CriterionInstance;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.LootSerializationContext;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.PacketPlayOutAdvancements;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;

public class FakeAdvancement {
	private FakeDisplay display;

	public FakeAdvancement(FakeDisplay display) {
		this.display = display;
	}

	public FakeDisplay getDisplay() {
		return this.display;
	}

	public void displayToast(Player player) {
		MinecraftKey key = new MinecraftKey("mme", "notification");
		FakeDisplay display = this.getDisplay();
		MinecraftKey backgroundTexture = null;
		boolean useBackground = display.getBackgroundTexture() != null;
		if (useBackground)
			backgroundTexture = new MinecraftKey(display.getBackgroundTexture());
		HashMap<String, Criterion> criterias = new HashMap<String, Criterion>();
		String[][] requirements = new String[][] {};
		criterias.put("for_free", new Criterion(new CriterionInstance() {
			public MinecraftKey a() {
				return new MinecraftKey("minecraft", "impossible");
			}
			// MODIFIED
			@Override
			public JsonObject a(LootSerializationContext arg0) {
				return null;
			}
			// MODIFIED
		}));
		ArrayList<String[]> fixed = new ArrayList<String[]>();
		fixed.add(new String[] { "for_free" });
		requirements = (String[][]) Arrays.stream(fixed.toArray()).toArray(n -> new String[n][]);
		AdvancementDisplay nmsDisplay = new AdvancementDisplay(
				CraftItemStack.asNMSCopy((org.bukkit.inventory.ItemStack) display.getIcon()),
				(IChatBaseComponent) display.getTitle().getBaseComponent(),
				(IChatBaseComponent) display.getDescription().getBaseComponent(), backgroundTexture,
				display.getFrame().getNMS(), true, false, true);
		Advancement nmsAdvancement = new Advancement(key, null, nmsDisplay,
				new AdvancementRewards(0, new MinecraftKey[0], new MinecraftKey[0], null), criterias, requirements);

		HashMap<MinecraftKey, AdvancementProgress> progresses = new HashMap<MinecraftKey, AdvancementProgress>();
		AdvancementProgress progress = new AdvancementProgress();
		progress.a(criterias, requirements);
		progress.getCriterionProgress("for_free").b();
		progresses.put(key, progress);
		PacketPlayOutAdvancements packet = new PacketPlayOutAdvancements(false,
				Arrays.asList(new Advancement[] { nmsAdvancement }), new HashSet<MinecraftKey>(), progresses);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		HashSet<MinecraftKey> rm = new HashSet<MinecraftKey>();
		rm.add(key);
		progresses.clear();
		packet = new PacketPlayOutAdvancements(false, new ArrayList<Advancement>(), rm, progresses);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}

}
