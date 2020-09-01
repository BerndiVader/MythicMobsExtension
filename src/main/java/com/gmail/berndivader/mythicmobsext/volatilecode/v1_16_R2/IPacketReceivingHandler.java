package com.gmail.berndivader.mythicmobsext.volatilecode.v1_16_R2;

import net.minecraft.server.v1_16_R2.Packet;
import net.minecraft.server.v1_16_R2.PacketPlayInArmAnimation;
import net.minecraft.server.v1_16_R2.PacketPlayInBlockDig;
import net.minecraft.server.v1_16_R2.PacketPlayInFlying;
import net.minecraft.server.v1_16_R2.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_16_R2.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_16_R2.PacketPlayInFlying.PacketPlayInPosition;

public interface IPacketReceivingHandler {
	Packet<?> handle(PacketPlayInArmAnimation packet);

	Packet<?> handle(PacketPlayInResourcePackStatus packet);

	Packet<?> handle(PacketPlayInPosition packet);

	Packet<?> handle(PacketPlayInFlying packet);

	Packet<?> handle(PacketPlayInSteerVehicle packet);

	Packet<?> handle(PacketPlayInBlockDig packet);
}
