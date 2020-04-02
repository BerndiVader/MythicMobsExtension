package com.gmail.berndivader.mythicmobsext.guardianbeam;
/*
 *  The MIT License (MIT)
 *
 *  Copyright (c) 2016 Jaxon A Brown
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 *  documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 *  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 *  persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all copies or portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 *  WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 *  COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 *  OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import com.google.common.base.Preconditions;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Creates a guardian beam between two locations. This uses ProtocolLib to send
 * two entities: A guardian and a squid. The guardian is then set to target the
 * squid. Be sure to run #cleanup for any players you #start.
 * 
 * @author Jaxon A Brown
 */
public class LocationTargetBeam {
	private final WrappedBeamPacket packetSquidSpawn;
	private final WrappedBeamPacket packetSquidMove;
	private final WrappedBeamPacket packetGuardianSpawn;
	private final WrappedBeamPacket packetGuardianMove;
	private final WrappedBeamPacket packetRemoveEntities;

	/**
	 * Create a guardian beam. This sets up the packets.
	 * 
	 * @param startingPosition Position to start the beam, or the position which the
	 *                         effect 'moves towards'.
	 * @param endingPosition   Position to stop the beam, or the position which the
	 *                         effect 'moves away from'.
	 */
	public LocationTargetBeam(Location startingPosition, Location endingPosition) {
		Preconditions.checkNotNull(startingPosition, "startingPosition cannot be null");
		Preconditions.checkNotNull(endingPosition, "endingPosition cannot be null");
		Preconditions.checkState(startingPosition.getWorld().equals(endingPosition.getWorld()),
				"startingPosition and endingPosition must be in the same world");

		this.packetSquidSpawn = PacketFactory.createPacketSquidSpawn(endingPosition);
		this.packetSquidMove = PacketFactory.createPacketEntityMove(this.packetSquidSpawn);
		this.packetGuardianSpawn = PacketFactory.createPacketGuardianSpawn(startingPosition, this.packetSquidSpawn);
		this.packetGuardianMove = PacketFactory.createPacketEntityMove(this.packetGuardianSpawn);
		this.packetRemoveEntities = PacketFactory.createPacketRemoveEntities(this.packetSquidSpawn,
				this.packetGuardianSpawn);
	}

	/**
	 * Send the packets to create the beam to the player.
	 * 
	 * @param player player to whom the beam will be sent.
	 */
	public void start(Player player) {
		this.packetSquidSpawn.send(player);
		this.packetGuardianSpawn.send(player);
	}

	/**
	 * Sets the position of the beam which the effect 'moves away from'.
	 * 
	 * @param player   player who should receive the update. They MUST have been
	 *                 showed the beam already.
	 * @param location location of the new position.
	 */
	public void setStartingPosition(Player player, Location location) {
		Location l = new Location(location.getWorld(), 0, 0, 0);
		PacketFactory.modifyPacketEntitySpawn(this.packetSquidSpawn, l).send(player);
//        PacketFactory.modifyPacketEntitySpawn(this.packetGuardianSpawn, location).send(player);
		PacketFactory.modifyPacketEntityMove(this.packetGuardianMove, location).send(player);
	}

	/**
	 * Sets the position of the beam which the effect 'moves towards'.
	 * 
	 * @param player   player who should receive the update. They MUST have been
	 *                 showed the beam already.
	 * @param location location of the new position.
	 */
	public void setEndingPosition(Player player, Location location) {
		PacketFactory.modifyPacketEntitySpawn(this.packetSquidSpawn, location).send(player);
//        PacketFactory.modifyPacketEntityMove(this.packetSquidMove, location).send(player);
	}

	/**
	 * Cleans up the entities on the player's side.
	 * 
	 * @param player player who needs the cleanup.
	 */
	public void cleanup(Player player) {
		this.packetRemoveEntities.send(player);
	}
}
