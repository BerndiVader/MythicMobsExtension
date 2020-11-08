package com.gmail.berndivader.mythicmobsext.volatilecode.v1_16_R3;

import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_16_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_16_R3.PacketPlayInFlying;
import net.minecraft.server.v1_16_R3.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_16_R3.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_16_R3.PacketPlayInFlying.PacketPlayInPosition;

public interface IPacketReceivingHandler {
	Packet<?> handle(PacketPlayInArmAnimation packet);

	Packet<?> handle(PacketPlayInResourcePackStatus packet);

	Packet<?> handle(PacketPlayInPosition packet);

	Packet<?> handle(PacketPlayInFlying packet);

	Packet<?> handle(PacketPlayInSteerVehicle packet);

	Packet<?> handle(PacketPlayInBlockDig packet);
}
