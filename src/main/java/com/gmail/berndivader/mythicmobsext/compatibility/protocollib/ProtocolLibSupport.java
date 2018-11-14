package com.gmail.berndivader.mythicmobsext.compatibility.protocollib;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.ProtocolLib;
import com.comphenix.protocol.ProtocolLibrary;
import com.gmail.berndivader.mythicmobsext.Main;

public
class 
ProtocolLibSupport
{
	static String pluginName="ProtocolLib";
	private static ProtocolLibSupport core;
	private Plugin plugin;
	private static Optional<ProtocolLib>protocolLib;
	
	static {
		protocolLib=Optional.ofNullable((ProtocolLib)Bukkit.getServer().getPluginManager().getPlugin(pluginName));
	}
	
	public static ProtocolLibSupport inst() {
		return core;
	}
	
	public ProtocolLibSupport(Plugin plugin) {
		core=this;
		this.plugin=plugin;
		Main.logger.info("using "+pluginName);
		ProtocolLibrary.getProtocolManager().addPacketListener(new PacketReader());
	}

	public Plugin plugin() {
		return this.plugin;
	}
	public static boolean isPresent() {
		return protocolLib.isPresent();
	}
	public ProtocolLib protocolLib() {
		return ProtocolLibSupport.protocolLib.get();
	}
	
	
	
}
