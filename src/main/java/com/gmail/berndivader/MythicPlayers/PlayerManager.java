package com.gmail.berndivader.MythicPlayers;

import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.MythicPlayers.Mechanics.TriggeredSkillAP;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.TaskManager;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MobManager.QueuedMobCleanup;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;

public class PlayerManager implements Listener {
	public static String meta_MYTHICPLAYER = "MythicPlayer";
	public static String meta_NOACTIVEMOB = "NOACTIVEMOB";
	public static String meta_FACTION="Faction";
	public static String meta_READYREASON="READYREASON";
	public static String meta_ITEMCHANGE="ITEMCHANGE";
	public static String signal_QUIT="QUIT";
	public static String signal_DEATH = "DEATH";
	private MythicPlayers mythicplayers;
	private MobManager mobmanager = MythicPlayers.mythicmobs.getMobManager();
	private ConcurrentHashMap<UUID, ActivePlayer> activePlayers = new ConcurrentHashMap<UUID, ActivePlayer>();

	public PlayerManager(MythicPlayers mythicplayers) {
		this.mythicplayers = mythicplayers;
		Bukkit.getServer().getPluginManager().registerEvents(this, mythicplayers.plugin());
	}

	public ActivePlayer registerActiveMob(ActivePlayer ap) {
		this.activePlayers.put(ap.getUniqueId(), ap);
		mobmanager.registerActiveMob(ap);
		return ap;
	}

	public void unregisterActivePlayer(UUID uuid) {
		this.activePlayers.remove(uuid);
		mobmanager.unregisterActiveMob(uuid);
	}

	public boolean isActivePlayer(UUID uuid) {
		return this.activePlayers.containsKey(uuid);
	}

	public Optional<ActivePlayer> getActivePlayer(UUID uuid) {
		return Optional.ofNullable(this.activePlayers.getOrDefault(uuid, null));
	}

	public void removeAllEffectsFromPlayer(AbstractEntity e) {
		LivingEntity le = (LivingEntity) e.getBukkitEntity();
		if (e.hasPotionEffect()) {
			Iterator<PotionEffect> i = le.getActivePotionEffects().iterator();
			while (i.hasNext()) {
				le.removePotionEffect(i.next().getType());
			}
		}
	}

	public void addMythicPlayerToFaction(MythicMob mm, ActivePlayer ap) {
		if (mm.hasFaction()) {
			ap.setFaction(mm.getFaction());
			ap.getLivingEntity().setMetadata(meta_FACTION,
					new FixedMetadataValue(MythicPlayers.mythicmobs, mm.getFaction()));
		}
	}

	public void makeNormalPlayer(ActivePlayer ap) {
		Entity e = ap.getEntity().getBukkitEntity();
		ap.signalMob(ap.getEntity(), meta_NOACTIVEMOB);
		this.removeAllEffectsFromPlayer(ap.getEntity());
		this.removeActivePlayer(ap);
		e.removeMetadata(meta_MYTHICPLAYER, mythicplayers.plugin());
		e.removeMetadata(meta_FACTION, mythicplayers.plugin());
	}

	public void removeActivePlayer(ActivePlayer ap) {
		Location l = ap.getEntity().getBukkitEntity().getLocation().clone();
		l.setY(0);
		AbstractEntity d = BukkitAdapter.adapt(l.getWorld().spawnEntity(l, EntityType.BAT));
		ap.setEntity(d);
		this.unregisterActivePlayer(ap.getUniqueId());
		d.remove();
	}

	public boolean attachActivePlayer(LivingEntity l, boolean dotrigger) {
		MythicMob mm = mobmanager.getMythicMob(l.getMetadata(meta_MYTHICPLAYER).get(0).asString());
		if (mm == null) {
			l.removeMetadata(meta_MYTHICPLAYER, mythicplayers.plugin());
			return false;
		}
		ActivePlayer ap = new ActivePlayer(l.getUniqueId(), BukkitAdapter.adapt(l), mm, 1);
		this.addMythicPlayerToFaction(mm, ap);
		this.registerActiveMob(ap);
		if (dotrigger)
			new TriggeredSkillAP(SkillTrigger.SPAWN, ap, null);
		return true;
	}

	public boolean createActivePlayer(LivingEntity l, MythicMob mm) {
		l.setMetadata(meta_MYTHICPLAYER, new FixedMetadataValue(mythicplayers.plugin(), mm.getInternalName()));
		ActivePlayer ap = new ActivePlayer(l.getUniqueId(), BukkitAdapter.adapt(l), mm, 1);
		this.addMythicPlayerToFaction(mm, ap);
		this.registerActiveMob(ap);
		new TriggeredSkillAP(SkillTrigger.SPAWN, ap, null);
		return true;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onMythicPlayerDeath(PlayerDeathEvent e) {
		if (!this.isActivePlayer(e.getEntity().getUniqueId()))
			return;
		ActivePlayer ap = this.getActivePlayer(e.getEntity().getUniqueId()).get();
		ap.signalMob(ap.getEntity(), signal_DEATH);
		this.removeAllEffectsFromPlayer(ap.getEntity());
		this.removeActivePlayer(ap);
	}

	@EventHandler
	public void onMythicPlayerRespawn(PlayerRespawnEvent e) {
		if (e.getPlayer().hasMetadata(meta_MYTHICPLAYER)) {
			new BukkitRunnable() {
				@Override
				public void run() {
					PlayerManager.this.attachActivePlayer(e.getPlayer(), true);
				}
			}.runTaskLater(mythicplayers.plugin(), 1);
		}
	}

	@EventHandler
	public void onMythicPlayerJoin(PlayerJoinEvent e) {
		if (e.getPlayer().hasMetadata(meta_MYTHICPLAYER))
			this.attachActivePlayer(e.getPlayer(), true);
	}
	
	@EventHandler
	public void onMythicPlayerQuit(PlayerQuitEvent e) {
		if (this.isActivePlayer(e.getPlayer().getUniqueId())) {
			ActivePlayer ap = this.getActivePlayer(e.getPlayer().getUniqueId()).get();
			this.removeAllEffectsFromPlayer(ap.getEntity());
			ap.signalMob(ap.getEntity(), signal_QUIT);
			this.removeActivePlayer(ap);
			TaskManager.get().runAsyncLater(new QueuedMobCleanup(ap), 0);
		}
	}

	@EventHandler
	public void onMythicPlayerDamage(EntityDamageEvent e) {
		if (e.isCancelled()) return;
		if (mobmanager==null||mobmanager.isActiveMob(e.getEntity().getUniqueId())) return;
		Entity victim = e.getEntity();
		DamageCause cause = e.getCause();
		victim.setMetadata("LDC", new FixedMetadataValue(mythicplayers.plugin(), cause.toString()));
		if (!this.isActivePlayer(victim.getUniqueId()))
			return;
		if (!cause.equals(DamageCause.ENTITY_ATTACK) && !cause.equals(DamageCause.ENTITY_EXPLOSION)
				&& !cause.equals(DamageCause.CUSTOM) && !cause.equals(DamageCause.PROJECTILE)) {
			ActivePlayer ap = this.getActivePlayer(victim.getUniqueId()).get();
			AbstractEntity trigger = null;
			if (e instanceof EntityDamageByEntityEvent)
				trigger = BukkitAdapter.adapt(((EntityDamageByEntityEvent) e).getDamager());
			new TriggeredSkillAP(SkillTrigger.DAMAGED, ap, trigger);
		}
	}

	@EventHandler
	public void onMythicPlayerToggleSneak(PlayerToggleSneakEvent e) {
		if (e.isCancelled() || !this.isActivePlayer(e.getPlayer().getUniqueId()))
			return;
		SkillTrigger st = e.getPlayer().isSneaking() ? SkillTrigger.UNCROUCH : SkillTrigger.CROUCH;
		new TriggeredSkillAP(st, this.getActivePlayer(e.getPlayer().getUniqueId()).get(), null);
	}
	
	@EventHandler
	public void onUseTrigger(PlayerInteractEvent e) {
		if(e.getHand()==EquipmentSlot.HAND&&this.isActivePlayer(e.getPlayer().getUniqueId())) {
			ActivePlayer ap = this.getActivePlayer(e.getPlayer().getUniqueId()).get();
			TriggeredSkillAP ts=null;
			switch(e.getAction()) {
				case RIGHT_CLICK_AIR:
					ts=new TriggeredSkillAP(SkillTrigger.RIGHTCLICK,ap,null,null,true);
					break;
				case RIGHT_CLICK_BLOCK:
					ts=new TriggeredSkillAP(SkillTrigger.RIGHTCLICK,ap,null,BukkitAdapter.adapt(e.getClickedBlock().getLocation()),true);
					break;
				case LEFT_CLICK_AIR:
					ts=e.getPlayer().getInventory().getItemInMainHand()!=null?new TriggeredSkillAP(SkillTrigger.USE,ap,null,null,true):new TriggeredSkillAP(SkillTrigger.SWING,ap,null,null,true);
					break;
				case LEFT_CLICK_BLOCK:
					ts=e.getPlayer().getInventory().getItemInMainHand()!=null?new TriggeredSkillAP(SkillTrigger.USE,ap,null,BukkitAdapter.adapt(e.getClickedBlock().getLocation()),true):new TriggeredSkillAP(SkillTrigger.SWING,ap,null,BukkitAdapter.adapt(e.getClickedBlock().getLocation()),true);
					break;
				default:
					break;
			}
			if (ts!=null&&ts.getCancelled()) e.setCancelled(true);
		}
	}

	@EventHandler
	public void onPlayerWorldChangeAtPortal(PlayerPortalEvent e) {
		if (e.isCancelled() || !this.isActivePlayer(e.getPlayer().getUniqueId())
				|| e.getFrom().getWorld().equals(e.getTo().getWorld()))
			return;
		Optional<ActivePlayer> maybeActivePlayer = this.getActivePlayer(e.getPlayer().getUniqueId());
		if (maybeActivePlayer.isPresent()) {
			ActivePlayer ap = maybeActivePlayer.get();
			this.removeActivePlayer(ap);
		}
	}
	
	@EventHandler
	public void onMythicPlayerWorldChangeAtTeleport(PlayerTeleportEvent e) {
		if (e.isCancelled() || !this.isActivePlayer(e.getPlayer().getUniqueId())
				|| e.getFrom().getWorld().equals(e.getTo().getWorld()))
			return;
		Optional<ActivePlayer> maybeActivePlayer = this.getActivePlayer(e.getPlayer().getUniqueId());
		if (maybeActivePlayer.isPresent()) {
			ActivePlayer ap = maybeActivePlayer.get();
			this.removeActivePlayer(ap);
		}
	}
	
	@EventHandler
	public void onMythicPlayerWorldChanged(PlayerChangedWorldEvent e) {
		if (e.getPlayer().hasMetadata(meta_MYTHICPLAYER)) {
			final Player p=e.getPlayer();
			new BukkitRunnable() {
				@Override
				public void run() {
					PlayerManager.this.attachActivePlayer(p, true);
				}
			}.runTaskLater(mythicplayers.plugin(), 50L);
		}
	}

	@EventHandler
	public void onMythicPlayerTeleportChunkNotLoaded(PlayerTeleportEvent e) {
		if (!e.isCancelled()&&e.getPlayer().hasMetadata(meta_MYTHICPLAYER)&&e.getFrom().getWorld().equals(e.getTo().getWorld())) {
			final Player p=e.getPlayer();
			new BukkitRunnable() {
				@Override
				public void run() {
					if (p.isDead()) {
						Volatile.handler.setDeath(p,false);
						Optional<ActivePlayer> maybeActivePlayer = getActivePlayer(p.getUniqueId());
						if (maybeActivePlayer.isPresent()) {
							ActivePlayer ap = maybeActivePlayer.get();
							removeActivePlayer(ap);
							new BukkitRunnable() {
								@Override
								public void run() {
									PlayerManager.this.attachActivePlayer(p, true);
								}
							}.runTaskLater(Main.getPlugin(),20L);
						}
					}
				}
			}.runTaskLater(Main.getPlugin(),50L);
		}
	}
	
	@EventHandler
	public void onMythicPlayerChangeSlot(PlayerItemHeldEvent e) {
		if (e.isCancelled() || !e.getPlayer().hasMetadata(meta_MYTHICPLAYER))
			return;
		Optional<ActivePlayer> maybeActivePlayer = this.getActivePlayer(e.getPlayer().getUniqueId());
		if (maybeActivePlayer.isPresent()) {
			ActivePlayer ap = maybeActivePlayer.get();
			e.getPlayer().setMetadata(meta_READYREASON, new FixedMetadataValue(mythicplayers.plugin(), meta_ITEMCHANGE));
			new TriggeredSkillAP(SkillTrigger.READY, ap, ap.getEntity());
		}
	}

}
