package com.gmail.berndivader.mythicmobsext.volatilecode.v1_15_R1;

import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PacketPlayInArmAnimation;
import net.minecraft.server.v1_15_R1.PacketPlayInBlockDig;
import net.minecraft.server.v1_15_R1.PacketPlayInFlying;
import net.minecraft.server.v1_15_R1.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_15_R1.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_15_R1.PacketPlayInFlying.PacketPlayInPosition;

public
interface
IPacketReceivingHandler
{
    Packet<?> handle(PacketPlayInArmAnimation packet);
    Packet<?> handle(PacketPlayInResourcePackStatus packet);
    Packet<?> handle(PacketPlayInPosition packet);
	Packet<?> handle(PacketPlayInFlying packet);
	Packet<?> handle(PacketPlayInSteerVehicle packet);
	Packet<?> handle(PacketPlayInBlockDig packet);
}
