package com.gmail.berndivader.mythicmobsext.compatibility.protocollib;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.ListeningWhitelist;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;
import com.comphenix.protocol.injector.GamePhase;
import com.comphenix.protocol.injector.server.TemporaryPlayer;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.compatibility.protocollib.wrapper.WrapperPlayServerUpdateHealth;
import com.gmail.berndivader.mythicmobsext.utils.Utils;

public
class
PacketReader
implements
PacketListener 
{
	static final String tag="mme_last_health";
	
	ListeningWhitelist outgoing;
	ListeningWhitelist incoming;
	
	public PacketReader() {
		
		this.incoming=ListeningWhitelist.EMPTY_WHITELIST;
		
		this.outgoing=ListeningWhitelist.newBuilder()
				.priority(ListenerPriority.LOWEST)
				.types(new PacketType[] {
						PacketType.Play.Server.UPDATE_HEALTH,
						PacketType.Play.Server.ENTITY_METADATA,
					})
				.gamePhase(GamePhase.BOTH)
				.options(new ListenerOptions[0])
				.build();
	}

	@Override
	public Plugin getPlugin() {
		return Main.getPlugin();
	}

	@Override
	public ListeningWhitelist getReceivingWhitelist() {
		return incoming;
	}

	@Override
	public ListeningWhitelist getSendingWhitelist() {
		return outgoing;
	}

	@Override
	public void onPacketReceiving(PacketEvent packet_event) {
	}

	@Override
	public void onPacketSending(PacketEvent packet_event) {
		if(packet_event.isCancelled()||packet_event.getPlayer() instanceof TemporaryPlayer) return;
		switch(packet_event.getPacketType().getCurrentId()) {
			case 72:
				WrapperPlayServerUpdateHealth health_packet=new WrapperPlayServerUpdateHealth(packet_event.getPacket());
				Player player=packet_event.getPlayer();
				float last_health=0f;
				if(player.hasMetadata(tag)) {
					last_health=player.getMetadata(tag).get(0).asFloat();
				} else {
					last_health=health_packet.getHealth();
					player.setMetadata(tag,new FixedMetadataValue(Main.getPlugin(),last_health));
				}
				player.setMetadata(Utils.meta_LASTHEALAMOUNT,new FixedMetadataValue(Main.getPlugin(),health_packet.getHealth()-last_health));
				player.setMetadata(tag,new FixedMetadataValue(Main.getPlugin(),health_packet.getHealth()));
				break;
		}
	}

}
