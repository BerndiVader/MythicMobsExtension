package com.gmail.berndivader.mythicmobsext.volatilecode.v1_13_R2;

import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.conditions.GetLastDamageIndicatorCondition;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.Packet;
import net.minecraft.server.v1_13_R2.PacketPlayInArmAnimation;
import net.minecraft.server.v1_13_R2.PacketPlayInBlockDig;
import net.minecraft.server.v1_13_R2.PacketPlayInFlying;
import net.minecraft.server.v1_13_R2.PacketPlayInFlying.PacketPlayInPosition;
import net.minecraft.server.v1_13_R2.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_13_R2.PacketPlayInSteerVehicle;

public class PacketReceivingHandler implements IPacketReceivingHandler {

	Player player;
	EntityPlayer entity_player;

	public PacketReceivingHandler(Player player) {
		this.player = player;
		entity_player = ((CraftPlayer) this.player).getHandle();
	}

	@Override
	public Packet<?> handle(PacketPlayInArmAnimation packet) {
		float f1 = Volatile.handler.getIndicatorPercentage(player);
		player.setMetadata(GetLastDamageIndicatorCondition.meta_LASTDAMAGEINDICATOR,
				new FixedMetadataValue(Main.getPlugin(), f1));
		;
		return packet;
	}

	@Override
	public Packet<?> handle(PacketPlayInResourcePackStatus packet) {
		player.setMetadata(Utils.meta_RESOURCEPACKSTATUS,
				new FixedMetadataValue(Main.getPlugin(), packet.status.name()));
		return packet;
	}

	@Override
	public Packet<?> handle(PacketPlayInPosition packet) {
		// TODO Auto-generated method stub
		return packet;
	}

	@Override
	public Packet<?> handle(PacketPlayInFlying packet) {
		com.gmail.berndivader.mythicmobsext.utils.Vec3D v3 = new com.gmail.berndivader.mythicmobsext.utils.Vec3D(
				entity_player.locX, entity_player.locY, entity_player.locZ);
		double dx = packet.a(entity_player.locX), dy = packet.b(entity_player.locY), dz = packet.c(entity_player.locZ);
		v3 = (v3.getX() != dx || v3.getY() != dy || v3.getZ() != dz)
				? v3.length(new com.gmail.berndivader.mythicmobsext.utils.Vec3D(dx, dy, dz))
				: new com.gmail.berndivader.mythicmobsext.utils.Vec3D(0, 0, 0);
		Utils.players.put(player.getUniqueId(), v3);
		return packet;
	}

	@Override
	public Packet<?> handle(PacketPlayInSteerVehicle packet) {
		// TODO Auto-generated method stub
		return packet;
	}

	@Override
	public Packet<?> handle(PacketPlayInBlockDig packet) {
		player.setMetadata(Utils.meta_MMEDIGGING, new FixedMetadataValue(Main.getPlugin(), packet.d().name()));
		return packet;
	}

}
