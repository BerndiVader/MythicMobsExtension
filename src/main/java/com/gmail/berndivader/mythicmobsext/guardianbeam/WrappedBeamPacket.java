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

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Wraps a packet container for convenience.
 * 
 * @author Jaxon A Brown
 */
public class WrappedBeamPacket {
	private final PacketContainer handle;

	/**
	 * Wraps the packet.
	 * 
	 * @param container packet to wrap.
	 */
	public WrappedBeamPacket(PacketContainer container) {
		this.handle = container;
	}

	/**
	 * Sends the packet to a lucky receiver!
	 * 
	 * @param receiver player to send the packet to.
	 */
	public void send(Player receiver) {
		try {
			ProtocolLibrary.getProtocolManager().sendServerPacket(receiver, this.handle);
		} catch (InvocationTargetException ex) {
			throw new RuntimeException("Failed to send beam packet to player.", ex);
		}
	}

	/**
	 * Get the packet container.
	 * 
	 * @return ProtocolLib packet container.
	 */
	public PacketContainer getHandle() {
		return this.handle;
	}
}
