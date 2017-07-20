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

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.TaskManager;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager.QueuedMobCleanup;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;

public class PlayerManager implements Listener {
	private MythicPlayers mythicplayers;
    private ConcurrentHashMap<UUID, ActivePlayer> activePlayers = new ConcurrentHashMap<UUID, ActivePlayer>();
	
	public PlayerManager(MythicPlayers mythicplayers) {
		this.mythicplayers = mythicplayers;
		Bukkit.getServer().getPluginManager().registerEvents(this, mythicplayers.plugin());
	}
	
    public ActivePlayer registerActiveMob(ActivePlayer ap) {
        this.activePlayers.put(ap.getUniqueId(), ap);
        MythicMobs.inst().getMobManager().registerActiveMob(ap);
        return ap;
    }
    
    public void unregisterActivePlayer(UUID uuid) {
    	this.activePlayers.remove(uuid);
		MythicMobs.inst().getMobManager().unregisterActiveMob(uuid);
    }
    
    public boolean isActivePlayer(UUID uuid) {
        return this.activePlayers.containsKey(uuid);
    }
    
    public Optional<ActivePlayer> getActivePlayer(UUID uuid) {
        return Optional.ofNullable(this.activePlayers.getOrDefault(uuid, null));
    }
	
	public void removeAllEffectsFromPlayer(AbstractEntity e) {
		if (e.hasPotionEffect()) {
			LivingEntity le = (LivingEntity) e.getBukkitEntity();
			Iterator<PotionEffect> i = le.getActivePotionEffects().iterator();
			while (i.hasNext()) {
				le.removePotionEffect(i.next().getType());
			}
		}
	}
	
	public void addMythicPlayerToFaction(MythicMob mm, ActivePlayer ap) {
        if (mm.hasFaction()) {
            ap.setFaction(mm.getFaction());
            ap.getLivingEntity().setMetadata("Faction", new FixedMetadataValue(MythicMobs.inst(),mm.getFaction()));
        }
	}
	
	public void makeNormalPlayer(ActivePlayer ap) {
		Entity e = ap.getEntity().getBukkitEntity();
		ap.signalMob(ap.getEntity(), "NOACTIVEMOB");
		this.removeAllEffectsFromPlayer(ap.getEntity());
		this.removeActivePlayer(ap);
		e.removeMetadata("MythicPlayer", mythicplayers.plugin());
		e.removeMetadata("Faction", mythicplayers.plugin());
	}
	
	public void removeActivePlayer(ActivePlayer ap) {
		Location l = ap.getEntity().getBukkitEntity().getLocation().clone();
		l.setY(0);
		AbstractEntity d = BukkitAdapter.adapt(l.getWorld().spawnEntity(l, EntityType.BAT));
		ap.setEntity(d);
		this.unregisterActivePlayer(ap.getUniqueId());
		d.remove();
	}
	
	@SuppressWarnings("unchecked")
	public boolean attachActivePlayer(LivingEntity l, boolean dotrigger) {
		MythicMob mm = MythicMobs.inst().getMobManager().getMythicMob(l.getMetadata("MythicPlayer").get(0).asString());
		if (mm==null) {
			l.removeMetadata("MythicPlayer", mythicplayers.plugin());
			return false;
		};
        ActivePlayer ap = new ActivePlayer(l.getUniqueId(), BukkitAdapter.adapt((Entity)l), mm, 1);
        this.addMythicPlayerToFaction(mm, ap);
        this.registerActiveMob(ap);
        if (dotrigger) new TriggeredSkill(SkillTrigger.SPAWN, ap, null);
        return true;
	}
	
    @SuppressWarnings("unchecked")
	public boolean createActivePlayer(LivingEntity l, MythicMob mm) {
        ActivePlayer ap = new ActivePlayer(l.getUniqueId(), BukkitAdapter.adapt((Entity)l), mm, 1);
        this.addMythicPlayerToFaction(mm, ap);
        this.registerActiveMob(ap);
        new TriggeredSkill(SkillTrigger.SPAWN, ap, null);
        l.setMetadata("MythicPlayer", new FixedMetadataValue(mythicplayers.plugin(),mm.getInternalName()));
        return true;
    }

	@EventHandler(priority=EventPriority.MONITOR)
	public void onMythicPlayerDeath(PlayerDeathEvent e) {
		if (!this.isActivePlayer(e.getEntity().getUniqueId())) return;
		ActivePlayer ap = this.getActivePlayer(e.getEntity().getUniqueId()).get();
		ap.signalMob(ap.getEntity(), "DEATH");
		this.removeAllEffectsFromPlayer(ap.getEntity());
		this.removeActivePlayer(ap);
	}
	
	@EventHandler
	public void onMythicPlayerRespawn(PlayerRespawnEvent e) {
		if (e.getPlayer().hasMetadata("MythicPlayer")) {
	    	new BukkitRunnable() {
	            public void run() {
	            	MythicPlayers.inst().getPlayerManager().attachActivePlayer(e.getPlayer(),true);
	            }
	        }.runTaskLater(mythicplayers.plugin(), 1);
		}
	}

	@EventHandler
	public void onMythicPlayerJoin(PlayerJoinEvent e) {
		if (e.getPlayer().hasMetadata("MythicPlayer")) this.attachActivePlayer(e.getPlayer(),true);
	}
	
	@EventHandler
	public void onMythicPlayerQuit(PlayerQuitEvent e) {
		if (this.isActivePlayer(e.getPlayer().getUniqueId())) {
			ActivePlayer ap = this.getActivePlayer(e.getPlayer().getUniqueId()).get();
			this.removeAllEffectsFromPlayer(ap.getEntity());
			ap.signalMob(ap.getEntity(), "QUIT");
			this.removeActivePlayer(ap);
            TaskManager.get().runAsyncLater(new QueuedMobCleanup(ap), 0);
		}
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onMythicPlayerDamage(EntityDamageEvent e) {
		if (!MythicMobs.inst().getAPIHelper().isMythicMob(e.getEntity()) || e.isCancelled()) return;
		DamageCause cause = e.getCause();
        e.getEntity().setMetadata("LDC", new FixedMetadataValue(mythicplayers.plugin(),cause.toString()));
        if (!this.isActivePlayer(e.getEntity().getUniqueId())) return;
		if (!cause.equals(DamageCause.ENTITY_ATTACK) &&
			!cause.equals(DamageCause.ENTITY_EXPLOSION) &&
			!cause.equals(DamageCause.CUSTOM) &&
			!cause.equals(DamageCause.PROJECTILE)) {
			ActivePlayer ap = this.getActivePlayer(e.getEntity().getUniqueId()).get();
			new TriggeredSkill(SkillTrigger.DAMAGED, ap, null);
		}
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onMythicPlayerToggleSneak(PlayerToggleSneakEvent e) {
		if (e.isCancelled() || !this.isActivePlayer(e.getPlayer().getUniqueId())) return;
		SkillTrigger st = e.getPlayer().isSneaking()?SkillTrigger.UNCROUCH:SkillTrigger.CROUCH;
		new TriggeredSkill(st, this.getActivePlayer(e.getPlayer().getUniqueId()).get(), null);
	}
	
    @EventHandler
    public void onUseTrigger(PlayerInteractEvent e) {
    	if (!this.isActivePlayer(e.getPlayer().getUniqueId())) return;
    	ActivePlayer ap = this.getActivePlayer(e.getPlayer().getUniqueId()).get();
    	TriggeredSkillAP ts = null;
    	if (e.getHand().equals(EquipmentSlot.HAND)) {
    		if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
    			ts = new TriggeredSkillAP(SkillTrigger.USE, ap, null, null, true);
    		} else if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
    	    	ts = new TriggeredSkillAP(SkillTrigger.USE, ap, null, BukkitAdapter.adapt(e.getClickedBlock().getLocation()), true);
    		}
    	}
    	/**
    	Bukkit.getLogger().info("Hand: " + e.getHand().toString());
    	Bukkit.getLogger().info("Action: " + e.getAction().toString());
    	 */
        if (ts!=null && ts.getCancelled()) e.setCancelled(true);
    }
   
	@EventHandler
    public void onPlayerWorldChangeAtPortal(PlayerPortalEvent e) {
    	if (e.isCancelled() 
    			|| !this.isActivePlayer(e.getPlayer().getUniqueId()) 
    			|| e.getFrom().getWorld().equals(e.getTo().getWorld())) return;
    	Optional<ActivePlayer>maybeActivePlayer = this.getActivePlayer(e.getPlayer().getUniqueId());
    	if (maybeActivePlayer.isPresent()) {
        	ActivePlayer ap = maybeActivePlayer.get();
        	this.removeActivePlayer(ap);
    	}
    }
    
	@EventHandler
    public void onMythicPlayerWorldChangeAtTeleport(PlayerTeleportEvent e) {
    	if (e.isCancelled() 
    			|| !this.isActivePlayer(e.getPlayer().getUniqueId()) 
    			|| e.getFrom().getWorld().equals(e.getTo().getWorld())) return;
    	Optional<ActivePlayer>maybeActivePlayer = this.getActivePlayer(e.getPlayer().getUniqueId());
    	if (maybeActivePlayer.isPresent()) {
        	ActivePlayer ap = maybeActivePlayer.get();
        	this.removeActivePlayer(ap);
    	}
    }
	
	@EventHandler
    public void onMythicPlayerWorldChanged(PlayerChangedWorldEvent e) {
		if (e.getPlayer().hasMetadata("MythicPlayer")) {
	    	new BukkitRunnable() {
	    		private Player p = e.getPlayer();
	            public void run() {
	            	MythicPlayers.inst().getPlayerManager().attachActivePlayer(this.p,false);
	            }
	        }.runTaskLater(mythicplayers.plugin(),50L);
		}
    }
	
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onMythicPlayerChangeSlot(PlayerItemHeldEvent e) {
		if (e.isCancelled() || !e.getPlayer().hasMetadata("MythicPlayer")) return;
		Optional<ActivePlayer>maybeActivePlayer = this.getActivePlayer(e.getPlayer().getUniqueId());
		if (maybeActivePlayer.isPresent()) {
			ActivePlayer ap = maybeActivePlayer.get();
	        e.getPlayer().setMetadata("READYREASON", new FixedMetadataValue(mythicplayers.plugin(),"ITEMCHANGE"));
			new TriggeredSkill(SkillTrigger.READY, ap, ap.getEntity(), false);
		}
	}
    
}
