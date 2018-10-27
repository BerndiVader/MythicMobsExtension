package com.gmail.berndivader.mythicmobsext.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.gmail.berndivader.MythicPlayers.Mechanics.TriggeredSkillAP;
import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.compatibility.nocheatplus.NoCheatPlusSupport;
import com.gmail.berndivader.mythicmobsext.config.Config;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.mechanics.NoDamageTicksMechanic;
import com.gmail.berndivader.mythicmobsext.mechanics.PlayerGoggleMechanic;
import com.gmail.berndivader.mythicmobsext.mechanics.PlayerSpinMechanic;
import com.gmail.berndivader.mythicmobsext.mechanics.StunMechanic;
import com.gmail.berndivader.mythicmobsext.volatilecode.Handler;
import com.gmail.berndivader.mythicmobsext.volatilecode.Volatile;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.AbstractLocation;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobSpawnEvent;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.MythicMob;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import io.lumine.xikage.mythicmobs.skills.SkillString;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.spawning.spawners.MythicSpawner;
import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;

import think.rpgitems.item.ItemManager;
import think.rpgitems.item.RPGItem;

@SuppressWarnings("deprecation")
public class Utils implements Listener {
	public static MythicMobs mythicmobs;
	public static MobManager mobmanager;
	public static int serverV;
	public static HashMap<UUID,Vec3D>pl;
	public static final String signal_AISHOOT="AISHOOT";
	public static final String signal_AIHIT="AIHIT";
	public static final String meta_WALKSPEED="MMEXTWALKSPEED";
	public static final String mpNameVar = "mythicprojectile";
	public static final String noTargetVar = "nottargetable";
	public static final String meta_BOWTICKSTART="mmibowtick";
	public static final String meta_BOWTENSIONLAST="mmibowtensionlast";
	public static final String meta_MYTHICDAMAGE="MythicDamage";
	public static final String meta_DAMAGECAUSE="DamageCause";
	public static final String meta_LASTDAMAGER="LastDamager";
	public static final String meta_LASTDAMAGECAUSE="LastDamageCause";
	public static final String meta_LASTDAMAGEAMOUNT="LastDamageAmount";
	public static final String meta_MMRPGITEMDMG="mmrpgitemdmg";
	public static final String meta_MMEDIGGING="MMEDIGGING";
	public static final String meta_LASTCOLLIDETYPE="MMELASTCOLLIDE";
	public static final String meta_NCP="NCP";
	public static final String meta_SPAWNREASON="SPAWNREASON";
	public static final String meta_CUSTOMSPAWNREASON="SETSPAWNREASON";
	public static final String meta_RESOURCEPACKSTATUS="MMERESPACKSTAT";
	public static String scripts;
	public static String str_PLUGINPATH;
	public static HashSet<Advancement>advancements;
	
	private static BlockFace[]axis={
			BlockFace.NORTH,
			BlockFace.EAST,
			BlockFace.SOUTH,
			BlockFace.WEST};
	private static BlockFace[]radial={
			BlockFace.NORTH,
			BlockFace.NORTH_EAST,
			BlockFace.EAST,
			BlockFace.SOUTH_EAST,
			BlockFace.SOUTH,
			BlockFace.SOUTH_WEST,
			BlockFace.WEST,
			BlockFace.NORTH_WEST}; 

	private static Handler handler;
	
	static {
		mythicmobs=MythicMobs.inst();
		mobmanager=mythicmobs.getMobManager();
		str_PLUGINPATH=Main.getPlugin().getDataFolder().toString();
	    try {
		    serverV=Integer.parseInt(Bukkit.getServer().getClass().getPackage().getName().substring(23).split("_")[1]);
	    } catch (Exception e) {
	    	serverV=11;
	    }
		if(Utils.serverV>11) {
			advancements=new HashSet<>();
			for(Iterator<Advancement>iter=Bukkit.getServer().advancementIterator();iter.hasNext();) {
				Advancement adv=iter.next();
				advancements.add(adv);
			}
		}
		pl=new HashMap<>();
	}
	
	public Utils() {
		handler=Volatile.handler;
		Main.pluginmanager.registerEvents(new UndoBlockListener(),Main.getPlugin());
		if (Utils.serverV>11) {
			Main.getPlugin().getServer().getPluginManager().registerEvents(this,Main.getPlugin());
			if (Config.m_parrot) Main.logger.info("patching entity parrot!");
		}
	}
	
	@EventHandler
	public void creeperExplode(EntityExplodeEvent e) {
		if(e.isCancelled()||(e.getEntityType()!=EntityType.CREEPER)) return;
		if(mobmanager.isActiveMob(e.getEntity().getUniqueId())) {
			ActiveMob am=mobmanager.getMythicMobInstance(e.getEntity());
			if(am.getType().getConfig().getBoolean("BlocksOnFire",false)) {
				int amount=am.getType().getConfig().getInt("BlocksOnFireAmount",10);
				World w=e.getLocation().getWorld();
				Location l=e.getLocation();
				for(int i=0;i<amount;i++) {
					FallingBlock fb=w.spawnFallingBlock(l,Material.FIRE,(byte)0);
					fb.setVelocity(new Vector(UndoBlockListener.getRandomVel(-0.5, 0.5),UndoBlockListener.getRandomVel(0.3, 0.8),UndoBlockListener.getRandomVel(-0.5, 0.5)));
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void tagAndChangeSpawnReason(CreatureSpawnEvent e) {
		if (e.isCancelled()) return;
		e.getEntity().setMetadata(meta_SPAWNREASON,new FixedMetadataValue(Main.getPlugin(),e.getSpawnReason()));
	}
	
	@EventHandler
	public void storeBowTensionEvent(PlayerInteractEvent e) {
		final Player p=e.getPlayer();
		if (p.getInventory().getItemInMainHand().getType()==Material.BOW) {
			p.setMetadata(meta_BOWTICKSTART, new FixedMetadataValue(Main.getPlugin(),NMSUtils.getCurrentTick(Bukkit.getServer())));
			new BukkitRunnable() {
				float f1;
				@Override
				public void run() {
					if (p!=null&&p.isOnline()&&(f1=com.gmail.berndivader.mythicmobsext.utils.Utils.getBowTension(p))>-1) {
						p.setMetadata(meta_BOWTENSIONLAST, new FixedMetadataValue(Main.getPlugin(),f1));
					} else {
						this.cancel();
					}
				}
			}.runTaskTimer(Main.getPlugin(),0l,0l);
		}
	}
	
	@EventHandler
	public void replaceParrotsEvent(MythicMobSpawnEvent e) {
		if (e.isCancelled()||!Config.m_parrot) return;
		if (e.getEntity() instanceof Parrot) {
			MythicMob mm=e.getMobType();
			LivingEntity p=handler.spawnCustomParrot(e.getLocation(),mm.getConfig().getBoolean("Options.CookieDie",true));
	        if (mm.getMythicEntity()!=null) p=(LivingEntity)mm.getMythicEntity().applyOptions(p);
	        final ActiveMob am = new ActiveMob(p.getUniqueId(), BukkitAdapter.adapt(p),mm,e.getMobLevel());
	        mythicmobs.getMobManager().registerActiveMob(am);
	        mm.applyMobOptions(am,am.getLevel());
	        mm.applyMobVolatileOptions(am);
	        new TriggeredSkillAP(SkillTrigger.SPAWN,am,null);
	        final Entity entity=e.getEntity();
	        new BukkitRunnable() {
				@Override
				public void run() {
					ActiveMob am1=null;
					if ((am1=mythicmobs.getMobManager().getMythicMobInstance(entity))!=null) {
						MythicSpawner ms=null;
						if ((ms=am1.getSpawner())!=null) {
							am.setSpawner(ms);
							if (!ms.getAssociatedMobs().contains(am.getUniqueId())) ms.trackMob(am);
						};
						am1.getEntity().remove();
					}
				}
			}.runTaskLater(Main.getPlugin(),1l);
		}
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		if (e.isCancelled()||!(e.getEntity().getShooter() instanceof Entity)) return;
		final Entity s=(Entity)e.getEntity().getShooter();
		final ActiveMob am=mobmanager.getMythicMobInstance(s);
		if (am!=null) {
			TriggeredSkillAP ts=new TriggeredSkillAP(SkillTrigger.SHOOT,am,am.getEntity().getTarget());
			e.setCancelled(ts.getCancelled());
		}
	}
	
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteractTrigger(PlayerInteractAtEntityEvent e) {
		if (e.isCancelled()&&e.getRightClicked().getType().equals(EntityType.VILLAGER)) {
			final Player p=e.getPlayer();
			new BukkitRunnable() {
				@Override
				public void run() {
					p.getOpenInventory().close();
				}
			}.runTaskLater(MythicMobs.inst(), 1L);
		}
	}
	
	@EventHandler
	public void RemoveFallingBlockProjectile(EntityChangeBlockEvent e) {
		if (e.getEntity().hasMetadata(Utils.mpNameVar)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority=EventPriority.LOWEST)
	public void removeScoreboardTagsFromEntity(EntityDeathEvent e) {
		if (e.getEntity() instanceof Player) return;
		final String[]arr1=e.getEntity().getScoreboardTags().toArray(new String[e.getEntity().getScoreboardTags().size()]);
		new BukkitRunnable() {
			@Override
			public void run() {
				for(int i1=0;i1<arr1.length;i1++) {
					e.getEntity().removeScoreboardTag(arr1[i1]);
				}
			}
		}.runTaskAsynchronously(Main.getPlugin());
	}
	
	@EventHandler
	public void mmTriggerOnKill(EntityDeathEvent e) {
		EntityDamageEvent entityDamageEvent = e.getEntity().getLastDamageCause();
		if (entityDamageEvent != null && !entityDamageEvent.isCancelled()
				&& entityDamageEvent instanceof EntityDamageByEntityEvent) {
			LivingEntity damager = getAttacker(((EntityDamageByEntityEvent) entityDamageEvent).getDamager());
			if (damager != null && mobmanager.isActiveMob(damager.getUniqueId())) {
				new TriggeredSkillAP(SkillTrigger.KILL, mobmanager.getMythicMobInstance(damager),
						BukkitAdapter.adapt(e.getEntity()));
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onMythicCustomRPGItemDamage(EntityDamageByEntityEvent e) {
		LivingEntity victim = null;
		if (e.getEntity() instanceof LivingEntity)
			victim = (LivingEntity) e.getEntity();
		if (victim == null || !victim.hasMetadata(meta_MYTHICDAMAGE))
			return;
		if (victim.getMetadata(meta_MMRPGITEMDMG).get(0).asBoolean()) {
			victim.removeMetadata(meta_MYTHICDAMAGE, Main.getPlugin());
			onEntityDamageTaken(e, victim);
		}
	}

	@EventHandler
	public void onMythicCustomDamage(EntityDamageByEntityEvent e) {
		LivingEntity victim = null;
		if (e.getEntity() instanceof LivingEntity)
			victim = (LivingEntity) e.getEntity();
		if (victim == null || !victim.hasMetadata(meta_MYTHICDAMAGE))
			return;
		if (!victim.getMetadata(meta_MMRPGITEMDMG).get(0).asBoolean()) {
			victim.removeMetadata(meta_MYTHICDAMAGE, Main.getPlugin());
			onEntityDamageTaken(e, victim);
		}
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void storeDamageCause(EntityDamageEvent e) {
		if(e.isCancelled()) return;
		Entity victim = e.getEntity();
		if (victim!=null&&victim.hasMetadata(meta_MYTHICDAMAGE)&&victim.hasMetadata(meta_DAMAGECAUSE)) {
			NMSUtils.setFinalField("cause",EntityDamageEvent.class,e,DamageCause.valueOf(victim.getMetadata(meta_DAMAGECAUSE).get(0).asString()));
			victim.removeMetadata(meta_DAMAGECAUSE,Main.getPlugin());
		}
		DamageCause cause = e.getCause();
		if (e instanceof EntityDamageByEntityEvent) {
			Entity damager = Utils.getAttacker(((EntityDamageByEntityEvent) e).getDamager());
			if (damager!=null) victim.setMetadata(meta_LASTDAMAGER, new FixedMetadataValue(Main.getPlugin(), damager.getType().toString()));
		} else if (victim.hasMetadata(meta_LASTDAMAGER)) {
			victim.removeMetadata(meta_LASTDAMAGER, Main.getPlugin());
		}
		victim.setMetadata(meta_LASTDAMAGECAUSE,new FixedMetadataValue(Main.getPlugin(), cause.toString()));
		victim.setMetadata(meta_LASTDAMAGEAMOUNT,new FixedMetadataValue(Main.getPlugin(),e.getDamage()));
	}
	
	@EventHandler
	public void triggerDamageForNoneEntity(EntityDamageEvent e) {
		TriggeredSkillAP ts;
		final Entity victim = e.getEntity();
		if (e instanceof EntityDamageByEntityEvent
				|| !(victim instanceof LivingEntity) 
				|| victim instanceof Player
				|| mobmanager.getVoidList().contains(victim.getUniqueId())) return;
		ActiveMob am = mobmanager.getMythicMobInstance(victim);
		if (am==null
				|| !am.getType().getConfig().getBoolean("onDamageForOtherCause")) return;
		ts = new TriggeredSkillAP(SkillTrigger.DAMAGED, am, null);
		if (ts.getCancelled()) e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p=e.getPlayer();
		UUID uuid;
		if(pl.containsKey(uuid=p.getUniqueId())) pl.remove(uuid);
		if (p.hasMetadata(NoDamageTicksMechanic.str)) e.getPlayer().removeMetadata(NoDamageTicksMechanic.str,Main.getPlugin());
		if (p.hasMetadata(StunMechanic.str)) {
			p.setGravity(true);
			p.removeMetadata(StunMechanic.str,Main.getPlugin());
		}
		if (p.hasMetadata(PlayerSpinMechanic.str)) {
			p.removeMetadata(PlayerSpinMechanic.str,Main.getPlugin());
		}
		if (p.hasMetadata(PlayerGoggleMechanic.str)) {
			p.removeMetadata(PlayerGoggleMechanic.str,Main.getPlugin());
		}
	}

	private static void onEntityDamageTaken(EntityDamageByEntityEvent e, LivingEntity victim) {
		boolean debug = victim.getMetadata("mmcdDebug").get(0).asBoolean();
		if (debug) Main.logger.info("CustomDamageMechanic cancelled? " + Boolean.toString(e.isCancelled()));
		if (e.isCancelled()) {
			if (e.getDamager().getType()==EntityType.PLAYER&&NoCheatPlusSupport.isPresent()) NCPExemptionManager.unexempt((Player)e.getDamager());
			return;
		}
		boolean ignoreArmor = victim.getMetadata("IgnoreArmor").get(0).asBoolean();
		boolean ignoreAbs = victim.getMetadata("IgnoreAbs").get(0).asBoolean();
		boolean strict=victim.getMetadata("DamageStrict").get(0).asBoolean();
		double md=strict?victim.getMetadata("DamageAmount").get(0).asDouble():e.getDamage();
		double df = e.getDamage(DamageModifier.BASE)!=0?round(md / e.getDamage(DamageModifier.BASE), 3):0.0d;
		if (debug) {
			Main.logger.info("Orignal BukkitDamage: " + Double.toString(e.getDamage(DamageModifier.BASE)));
			Main.logger.info("Custom MythicDamage.: " + Double.toString(md));
			Main.logger.info("DamageFactor: " + Double.toString(df));
			Main.logger.info("-----------------------------");
		}
		if (Double.isNaN(md)) md=0.001D;
		e.setDamage(DamageModifier.BASE,md);
		double damage=round(e.getDamage(DamageModifier.BASE),3);
		for (DamageModifier modifier:DamageModifier.values()) {
			if (!e.isApplicable(modifier)||modifier.equals(DamageModifier.BASE)) continue;
			double modF=df;
			if ((modifier.equals(DamageModifier.ARMOR)&&ignoreArmor)||(modifier.equals(DamageModifier.ABSORPTION)&&ignoreAbs)) modF=0D;
			modF=round(modF*e.getDamage(modifier),3);
			if (Double.isNaN(modF)) modF=0.001D;
			e.setDamage(modifier,modF);
			damage+=e.getDamage(modifier);
		}
		if (victim.getMetadata("PreventKnockback").get(0).asBoolean()) {
			e.setCancelled(true);
			victim.damage(damage);
		} else {
			e.setDamage(victim.hasMetadata("DamageStrict")&&victim.getMetadata("DamageStrict").get(0).asBoolean()?victim.getMetadata("DamageAmount").get(0).asDouble():damage);
		}
		if (debug) Main.logger.info("Finaldamage amount after modifiers: "+Double.toString(damage));
		if (e.getDamager().getType()==EntityType.PLAYER&&NoCheatPlusSupport.isPresent()&&victim.hasMetadata(meta_NCP)) {
			NCPExemptionManager.unexempt((Player)e.getDamager());
			victim.removeMetadata(meta_NCP,Main.getPlugin());
		}
	}

	public static void doDamage(SkillCaster am, AbstractEntity t, double damage, boolean ignorearmor,
			boolean preventKnockback, boolean preventImmunity, boolean ignoreabs, boolean debug, DamageCause cause,boolean ncp,boolean strict) {
		LivingEntity target;
		am.setUsingDamageSkill(true);
		if (am instanceof ActiveMob)
			((ActiveMob) am).setLastDamageSkillAmount(damage);
		LivingEntity source = (LivingEntity) BukkitAdapter.adapt(am.getEntity());
		target = (LivingEntity) BukkitAdapter.adapt(t);
		target.setMetadata("IgnoreArmor", new FixedMetadataValue(Main.getPlugin(), ignorearmor));
		target.setMetadata("PreventKnockback", new FixedMetadataValue(Main.getPlugin(), preventKnockback));
		target.setMetadata("IgnoreAbs", new FixedMetadataValue(Main.getPlugin(), ignoreabs));
		target.setMetadata(meta_MYTHICDAMAGE, new FixedMetadataValue(Main.getPlugin(), true));
		target.setMetadata("mmcdDebug", new FixedMetadataValue(Main.getPlugin(), debug));
		target.setMetadata(meta_DAMAGECAUSE, new FixedMetadataValue(Main.getPlugin(),cause.toString()));
		target.setMetadata("DamageStrict", new FixedMetadataValue(Main.getPlugin(),strict));
		target.setMetadata(meta_MMRPGITEMDMG, new FixedMetadataValue(Main.getPlugin(), false));
		if (!ignorearmor && Main.hasRpgItems && target instanceof Player) {
			damage = rpgItemPlayerHit((Player) target, damage);
		}
		if (am.getEntity().isPlayer()&&ncp&&NoCheatPlusSupport.isPresent()) {
			NCPExemptionManager.exemptPermanently((Player)am.getEntity().getBukkitEntity(),CheckType.FIGHT);
			target.setMetadata(meta_NCP,new FixedMetadataValue(Main.getPlugin(),true));
		}
		if (Double.isNaN(damage)) damage = 0.001D;
		round(damage,3);
		target.setMetadata("DamageAmount", new FixedMetadataValue(Main.getPlugin(), damage));
        target.damage(damage, source);
		if (preventImmunity)
			target.setNoDamageTicks(0);
		am.setUsingDamageSkill(false);
	}

	public static LivingEntity getAttacker(Entity damager) {
		LivingEntity shooter=null;
		if (damager instanceof Projectile) {
			if (((Projectile) damager).getShooter() instanceof LivingEntity) {
				shooter = (LivingEntity) ((Projectile) damager).getShooter();
			}
		} else if (damager instanceof LivingEntity) {
			shooter=(LivingEntity)damager;
		}
		return shooter;
	}

	public static Location getLocationInFront(Location start, double range) {
		return start.clone().add(start.getDirection().setY(0).normalize().multiply(range));
	}

	public static double rpgItemPlayerHit(Player p, double damage) {
		ItemStack[] armour = p.getInventory().getArmorContents();
		boolean useDamage = false;
		for (ItemStack pArmour : armour) {
			RPGItem pRItem = ItemManager.toRPGItem(pArmour);
			if (pRItem == null)
				continue;
			boolean can;
			if (!pRItem.hitCostByDamage) {
				can = pRItem.consumeDurability(pArmour, pRItem.hitCost);
			} else {
				can = pRItem.consumeDurability(pArmour, (int) (pRItem.hitCost * damage / 100d));
			}
			if (can && pRItem.getArmour() > 0) {
				useDamage = true;
				damage -= Math.round(damage * ((pRItem.getArmour()) / 100d));
			}
		}
		if (useDamage)
			p.setMetadata(meta_MMRPGITEMDMG, new FixedMetadataValue(Main.getPlugin(), true));
		return round(damage, 3);
	}

	public static LivingEntity getTargetedEntity(Player player) {
		int range = 32;
		BlockIterator bi;
		List<Entity> ne = player.getNearbyEntities(range, range, range);
		List<LivingEntity> entities = new ArrayList<>();
		for (Entity en:ne) {
			if ((en instanceof LivingEntity) && !en.hasMetadata(Utils.noTargetVar)) {
				entities.add((LivingEntity) en);
			}
		}
		LivingEntity target;
		bi = new BlockIterator(player, range);
		int bx;
		int by;
		int bz;
		while (bi.hasNext()) {
			Block b = bi.next();
			bx = b.getX();
			by = b.getY();
			bz = b.getZ();
			if (!b.getType().isTransparent())
				break;
			for (LivingEntity e : entities) {
				Location l = e.getLocation();
				double ex = l.getX();
				double ey = l.getY();
				double ez = l.getZ();
				if ((bx - 0.75D <= ex) && (ex <= bx + 1.75D) && (bz - 0.75D <= ez) && (ez <= bz + 1.75D)
						&& (by - 1 <= ey) && (ey <= by + 2.5D)) {
					target = e;
					if ((target != null) && ((target instanceof Player))
							&& (((Player) target).getGameMode() == org.bukkit.GameMode.CREATIVE)) {
						target = null;
					} else {
						return target;
					}
				}
			}
		}
		return null;
	}

	public static void applyInvisible(LivingEntity le, long runlater) {
		PotionEffect pe = new PotionEffect(PotionEffectType.INVISIBILITY, 2073600, 4, false, false);
		pe.apply(le);
		new BukkitRunnable() {
			@Override
			public void run() {
				le.getEquipment().clear();
			}
		}.runTaskLater(Main.getPlugin(), runlater);
	}

	public static final float DEGTORAD = 0.017453293F;
	public static final float RADTODEG = 57.29577951F;

	public static float getLookAtYaw(Entity loc, Entity lookat) {
		return getLookAtYaw(loc.getLocation(), lookat.getLocation());
	}

	public static float getLookAtYaw(Block loc, Block lookat) {
		return getLookAtYaw(loc.getLocation(), lookat.getLocation());
	}

	public static float getLookAtYaw(Location loc, Location lookat) {
		return getLookAtYaw(lookat.getX() - loc.getX(), lookat.getZ() - loc.getZ());
	}

	public static float getLookAtYaw(Vector motion) {
		return getLookAtYaw(motion.getX(), motion.getZ());
	}

	public static float getLookAtYaw(double dx, double dz) {
		float yaw = 0;
		if (dx != 0) {
			if (dx < 0) {
				yaw = 270;
			} else {
				yaw = 90;
			}
			yaw -= atan(dz / dx);
		} else if (dz < 0) {
			yaw = 180;
		}
		return -yaw - 90;
	}

	private static float atan(double value) {
		return RADTODEG * (float) Math.atan(value);
	}

	public static Location move(Location loc, Vector offset) {
		return move(loc, offset.getX(), offset.getY(), offset.getZ());
	}

	public static Location move(Location loc, double dx, double dy, double dz) {
		Vector off = rotate(loc.getYaw(), loc.getPitch(), dx, dy, dz);
		double x = loc.getX() + off.getX();
		double y = loc.getY() + off.getY();
		double z = loc.getZ() + off.getZ();
		return new Location(loc.getWorld(), x, y, z, loc.getYaw(), loc.getPitch());
	}

	public static Vector rotate(float yaw, float pitch, Vector value) {
		return rotate(yaw, pitch, value.getX(), value.getY(), value.getZ());
	}

	public static Vector rotate(float yaw, float pitch, double x, double y, double z) {
		float angle;
		angle = yaw * DEGTORAD;
		double sinyaw = Math.sin(angle);
		double cosyaw = Math.cos(angle);
		angle = pitch * DEGTORAD;
		double sinpitch = Math.sin(angle);
		double cospitch = Math.cos(angle);
		double newx = 0.0;
		double newy = 0.0;
		double newz = 0.0;
		newz -= x * cosyaw;
		newz -= y * sinyaw * sinpitch;
		newz -= z * sinyaw * cospitch;
		newx += x * sinyaw;
		newx -= y * cosyaw * sinpitch;
		newx -= z * cosyaw * cospitch;
		newy += y * cospitch;
		newy -= z * sinpitch;
		return new Vector(newx, newy, newz);
	}
	
	public static Vector getFrontBackOffsetVector(Vector v, double o) {
		Vector d=v.clone();
		d.normalize();
		d.multiply(o);
		return d;
	}
	
	@Deprecated
	public static Vector getSideOffsetVector(float vYa, double hO, boolean iy) {
		double y = 0d;
		if (!iy)
			y=Math.toRadians(vYa);
		double xo=Math.cos(y)*hO;
		double zo=Math.sin(y)*hO;
		return new Vector(xo,0d,zo);
	}
	
	public static Vector getSideOffsetVectorFixed(float vYa, double hO, boolean iy) {
		double y=0d;
		if (!iy) y=Math.toRadians(vYa);
		double xo=Math.cos(y)*hO;
		double zo=Math.sin(y)*hO;
		return new Vector(xo,y,zo);
	}
	

	public static float lookAtYaw(Location loc, Location lookat) {
		loc = loc.clone();
		lookat = lookat.clone();
		float yaw = 0.0F;
		double dx = lookat.getX() - loc.getX();
		double dz = lookat.getZ() - loc.getZ();
		if (dx != 0) {
			if (dx < 0) {
				yaw = (float) (1.5 * Math.PI);
			} else {
				yaw = (float) (0.5 * Math.PI);
			}
			yaw = yaw - (float) Math.atan(dz / dx);
		} else if (dz < 0) {
			yaw = (float) Math.PI;
		}
		yaw = -yaw * 180f / (float) Math.PI;
		return yaw;
	}
	public static Vec2D lookAtVec(Location loc, Location lookat) {
		loc=loc.clone();
		lookat=lookat.clone();
		float yaw=0.0F;
		double dx=lookat.getX()-loc.getX(),dz=lookat.getZ()-loc.getZ(),dy=lookat.getY()-loc.getY();
		double dxz=Math.sqrt(Math.pow(dx,2)+Math.pow(dz,2));
		if (dx!=0) {
			if(dx<0) {
				yaw=(float)(1.5*Math.PI);
			} else {
				yaw=(float)(0.5*Math.PI);
			}
			yaw=yaw-(float)Math.atan(dz/dx);
		} else if (dz<0) {
			yaw=(float)Math.PI;
		}
		float pitch=(float)-Math.atan(dy/dxz);
		return new Vec2D(-yaw*180f/Math.PI,pitch*180f/Math.PI);
	}

	public static Location moveTo(Location loc, Vector offset) {
		float ryaw = -loc.getYaw() / 180f * (float) Math.PI;
		float rpitch = loc.getPitch() / 180f * (float) Math.PI;
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		z -= offset.getX() * Math.sin(ryaw);
		z += offset.getY() * Math.cos(ryaw) * Math.sin(rpitch);
		z += offset.getZ() * Math.cos(ryaw) * Math.cos(rpitch);
		x += offset.getX() * Math.cos(ryaw);
		x += offset.getY() * Math.sin(rpitch) * Math.sin(ryaw);
		x += offset.getZ() * Math.sin(ryaw) * Math.cos(rpitch);
		y += offset.getY() * Math.cos(rpitch);
		y -= offset.getZ() * Math.sin(rpitch);
		return new Location(loc.getWorld(), x, y, z, loc.getYaw(), loc.getPitch());
	}

	public static AbstractLocation getCircleLoc(Location c, double rX, double rZ, double rY, double air) {
		double x=c.getX()+rX*Math.cos(air);
		double z=c.getZ()+rZ*Math.sin(air);
		double y=c.getY()+rY*Math.cos(air);
		Location loc=new Location(c.getWorld(),x,y,z);
		Vector difference=c.toVector().clone().subtract(loc.toVector());
		loc.setDirection(difference);
		return BukkitAdapter.adapt(loc);
	}

	public static double round(double value, int places) {
		return new BigDecimal(value).round(new MathContext(places, RoundingMode.HALF_UP)).doubleValue();
	}

	public static Vector calculateTrajectory(Vector from, Vector to, double heightGain, double gravity) {
		int endGain = to.getBlockY() - from.getBlockY();
		double horizDist = Math.sqrt(distance2D(from, to));
		double maxGain = heightGain > (endGain + heightGain) ? heightGain : (endGain + heightGain);
		double a = -horizDist * horizDist / (4 * maxGain);
		double b = horizDist;
		double c = -endGain;
		double slope = -b / (2 * a) - Math.sqrt(b * b - 4 * a * c) / (2 * a);
		double vy = Math.sqrt(maxGain * (gravity + 0.0013675090252708 * heightGain));
		double vh = vy / slope;
		int dx = to.getBlockX() - from.getBlockX();
		int dz = to.getBlockZ() - from.getBlockZ();
		double mag = Math.sqrt(dx * dx + dz * dz);
		double dirx = dx / mag;
		double dirz = dz / mag;
		double vx = vh * dirx;
		double vz = vh * dirz;
		if (Double.isNaN(vx)) vx=0.0D;
		if (Double.isNaN(vz)) vz=0.0D;
		return new Vector(vx, vy, vz);
	}

	public static Vector spread(Vector from, double yaw, double pitch) {
		Vector vec = from.clone();
		float cosyaw = (float)Math.cos(yaw);
		float cospitch = (float)Math.cos(pitch);
		float sinyaw = (float)Math.sin(yaw);
		float sinpitch = (float)Math.sin(pitch);
		float bX = (float) (vec.getY() * sinpitch + vec.getX() * cospitch);
		float bY = (float) (vec.getY() * cospitch - vec.getX() * sinpitch);
		return new Vector(bX * cosyaw - vec.getZ() * sinyaw, bY, bX * sinyaw + vec.getZ() * cosyaw);
	}

	public static Vector calculateVelocity(Double speed, Location originLocation, Location destination) {
		double g = 20;
		double v = speed;
		Vector relative = destination.clone().subtract(originLocation).toVector();
		double testAng = launchAngle(originLocation, destination.toVector(), v, relative.getY(), g);
		double hangTime = hangtime(testAng, v, relative.getY(), g);
		Vector to = destination.clone().add(originLocation.clone().multiply(hangTime)).toVector();
		relative = to.clone().subtract(originLocation.toVector());
		Double dist = Math.sqrt(relative.getX() * relative.getX() + relative.getZ() * relative.getZ());
		if (dist == 0) {
			dist = 0.1d;
		}
		testAng = launchAngle(originLocation, to, v, relative.getY(), g);
		relative.setY(Math.tan(testAng) * dist);
		relative = relative.normalize();
		v = v + (1.188 * Math.pow(hangTime, 2));
		return relative.multiply(v / 20.0d);
	}

	public static double launchAngle(Location from, Vector to, double v, double elev, double g) {
		Vector victor = from.toVector().subtract(to);
		Double dist = Math.sqrt(Math.pow(victor.getX(), 2) + Math.pow(victor.getZ(), 2));
		double v2 = Math.pow(v, 2);
		double v4 = Math.pow(v, 4);
		double derp = g * (g * Math.pow(dist, 2) + 2 * elev * v2);
		if (v4 < derp) {
			return Math.atan((2 * g * elev + v2) / (2 * g * elev + 2 * v2));
		}
		else {
			return Math.atan((v2 - Math.sqrt(v4 - derp)) / (g * dist));
		}
	}

	public static double hangtime(double launchAngle, double v, double elev, double g) {
		double a = v * Math.sin(launchAngle);
		double b = -2*g*elev;
		if(Math.pow(a, 2) + b < 0){
			return 0;
		}
		return (a + Math.sqrt(Math.pow(a, 2) + b)) / g;
	}

    public static double distance2D(Vector f, Vector t) {
        double dx = t.getBlockX() - f.getBlockX();
        double dz = t.getBlockZ() - f.getBlockZ();
        return dx * dx + dz * dz;
    }
    
    public static double distance3D(Vector f,Vector t) {
    	double dx=t.getBlockX()-f.getBlockX(),dy=t.getBlockY()-f.getBlockY(),dz=t.getBlockZ()-f.getBlockZ();
    	return dx*dx+dz*dz+dy*dy;
    }
    
    public static double distance3D(double x1,double y1,double z1,double x2,double y2,double z2) {
    	return Math.pow(x1-x2,2d)+Math.pow(y1-y2,2d)+Math.pow(z1-z2,2d);
    }
    public static double distance2D(double x1,double z1,double x2,double z2) {
    	return Math.pow(x1-x2,2d)+Math.pow(z1-z2,2d);
    }
    
    public static List<Player> getPlayersInRange(Location l,double distance){
    	List<Player>players=new ArrayList<Player>();
    	List<Player>list1=l.getWorld().getPlayers();
    	double x1=l.getBlockX(),y1=l.getBlockY(),z1=l.getBlockZ();
    	for(int i1=0;i1<list1.size();i1++) {
    		Player p=list1.get(i1);
    		Location l1=p.getLocation();
    		if(distance3D(x1,y1,z1,l1.getBlockX(),l1.getBlockY(),l1.getBlockZ())<=distance) players.add(p);
    	}
    	return players;
    }
    
    public static boolean isNumeric(String s) {
    	return s!=null?s.matches("[0-9]*"):false;
    }

	public static UUID isUUID(String data) {
		UUID uuid = null;
		try {
			uuid = UUID.fromString(data);
		} catch (IllegalArgumentException ex) {
			return null;
		}
		return uuid;
	}
	
	public static int randomRangeInt(String range) {
		ThreadLocalRandom r=ThreadLocalRandom.current();
		int amount=0;
		String[]split;
		int min,max;
		if (range.contains("to")) {
			split=range.split("to");
			min=Integer.parseInt(split[0]);
			max=Integer.parseInt(split[1]);
			if (max<min) max=min;
			amount=r.nextInt(min, max+1);
		} else amount=Integer.parseInt(range);
		return amount;
	}

	public static double randomRangeDouble(String range) {
		ThreadLocalRandom r=ThreadLocalRandom.current();
		double amount=0.0D;
		String[]split;
		double min,max;
		if (range.contains("to")) {
			split=range.split("to");
			min=Double.parseDouble(split[0]);
			max=Double.parseDouble(split[1]);
			if (max<min) max=min;
			amount=r.nextDouble(min,max);
		} else amount=Double.parseDouble(range);
		return amount;
	}

	public static byte encodeAngle(float angle) {
		return (byte)(angle*256f/360f);
	}

	public static int encodeVelocity(double v) {
		return (int)(v*8000D);
	}

	public static long encodePosition(double d) {
		return (long)(d*4096D);
	}
	
	public static String[] wrapStr(String s, int l) {
		String r="";
		String d="&&br&&";
		int ldp=0;
		for (String t:s.split(" ",-1)) {
			if (r.length()-ldp+t.length()>l) {
				r=r+d+t;
				ldp=r.length()+1;
			} else {
				r+=(r.isEmpty()?"":" ")+t;
			}
		}
		return r.split(d);
	}
	
    public static float normalise(float v,float s,float e) {
        float w=e-s,o=v-s;
        return (float)((o-(Math.floor(o/w)*w))+s);
    }

	public static void triggerShoot(Entity caster, Entity trigger) {
		final ActiveMob am=mobmanager.getMythicMobInstance(caster);
		if (am!=null) {
			new TriggeredSkillAP(SkillTrigger.SHOOT,am,am.getEntity().getTarget());
		}
	}
	
	public static String parseMobVariables(String s,SkillMetadata m,AbstractEntity c,AbstractEntity t,AbstractLocation l) {
		AbstractLocation l1=l!=null?l:t!=null?t.getLocation():null;
		s=SkillString.parseMobVariables(s,m.getCaster(),t,m.getTrigger());
		if (l1!=null&&s.contains("<target.l")) {
			s=s.replaceAll("<target.l.w>",l1.getWorld().getName());
			s=s.replaceAll("<target.l.x>",Double.toString(l1.getBlockX()));
			s=s.replaceAll("<target.l.y>",Double.toString(l1.getBlockY()));
			s=s.replaceAll("<target.l.z>",Double.toString(l1.getBlockZ()));
			if (s.contains("<target.l.d")) {
				s=s.replaceAll("<target.l.dx>",Double.toString(l1.getX()));
				s=s.replaceAll("<target.l.dy>",Double.toString(l1.getY()));
				s=s.replaceAll("<target.l.dz>",Double.toString(l1.getZ()));
			}
		}
		if (s.contains(".meta.")) s=parseMetaVar(s,c,t,l);
		return s;
	}
	
	private static String parseMetaVar(String s,AbstractEntity a1,AbstractEntity a2,AbstractLocation a3) {
		Entity e1=a1!=null?a1.getBukkitEntity():null;
		Entity e2=a2!=null?a2.getBukkitEntity():null;
		Location l1=a3!=null?BukkitAdapter.adapt(a3):null;
		if (s.contains("<target.meta")) {
			String[]p=s.split("<target.meta.");
			if (p.length>1) {
				String s1=p[1].split(">")[0];
				if (l1!=null&&l1.getWorld().getBlockAt(l1).hasMetadata(s1)) return s.replace("<target.meta."+s1+">"
						,l1.getWorld().getBlockAt(l1).getMetadata(s1).get(0).asString());
				if (e2!=null&&e2.hasMetadata(s1)) {
					return s.replaceAll("<target.meta."+s1+">",e2.getMetadata(s1).get(0).asString());
				}
			}
		} else if (s.contains("<mob.meta")) {
			String[]p=s.split("<mob.meta.");
			if (p.length>1) {
				String s1=p[1].split(">")[0];
				if (e1!=null&&e1.hasMetadata(s1)) return s.replaceAll("<mob.meta."+s1+">",e1.getMetadata(s1).get(0).asString());
			}
		}
		return s;
	}
	
	public static float getBowTension(Player p) {
		int i1=NMSUtils.getCurrentTick(Bukkit.getServer()),i2=-1;
        if (((HumanEntity)p).isHandRaised()&&p.hasMetadata(meta_BOWTICKSTART)) {
        	i2=p.getMetadata(meta_BOWTICKSTART).get(0).asInt();
        }
        if (i2==-1) return (float)i2;
        int i3=i1-i2;
        float f1=(float)i3/20.0f;
        if((f1=(f1*f1+f1*2.0f)/3.0f)>1.0f) f1=1.0f;
        return f1;
	}
	
	public static boolean isHeadingTo(Vector offset,Vector velocity) {
		double dbefore=offset.lengthSquared();
		if (dbefore<0.0001) {
			return true;
		}
		Vector clonedVelocity=velocity.clone();
		setVecLenSqrt(clonedVelocity,dbefore);
		return dbefore>clonedVelocity.subtract(offset).lengthSquared();
	}	
	
	public static void setVecLen(Vector vector,double length) {
		setVecLenSqrt(vector,Math.signum(length)*length*length);
	}

	public static void setVecLenSqrt(Vector vector, double lengthsquared) {
		double vlength=vector.lengthSquared();
		if (Math.abs(vlength)>0.0001) {
			if (lengthsquared<0) {
				vector.multiply(-Math.sqrt(-lengthsquared/vlength));
			} else {
				vector.multiply(Math.sqrt(lengthsquared/vlength));
			}
		}
	}
	
	public static int[] shuffleArray(int[]arr1) {
		int i1;
		Random r=Main.random;
		for (int i=arr1.length-1;i>0;i--) {
			i1=r.nextInt(i+1);
			if (i1!=i) {
				arr1[i1]^=arr1[i];
				arr1[i]^=arr1[i1];
				arr1[i1]^=arr1[i];
			}
		}
		return arr1;
	}
	
	public static Object cloneObject(Object obj) {
		try {
            Object clone=obj.getClass().newInstance();
            for (Field field:obj.getClass().getDeclaredFields()) {
            	field.setAccessible(true);
            	if(field.get(obj)==null||Modifier.isFinal(field.getModifiers())) continue;
            	if(field.getType().isPrimitive()
            			||field.getType().equals(String.class)
            			||field.getType().getSuperclass().equals(Number.class)
                        ||field.getType().equals(Boolean.class)) {
            		field.set(clone, field.get(obj));
            	} else {
            		Object childObj=field.get(obj);
            		if(childObj==obj) {
            			field.set(clone, clone);
            		} else {
            			field.set(clone,cloneObject(field.get(obj)));
            		}
            	}
            }
            return clone;
        } catch(Exception e) {
        	return null;
        }
	}
	
	public static boolean parseNBToutcome(String s1,String s2,int i1) {
		if (0<i1&&i1<7) {
			if (Character.isLetter(s1.charAt(s1.length()-1))) s1=s1.substring(0,s1.length()-1);
			if (Character.isLetter(s2.charAt(s2.length()-1))) s2=s2.substring(0,s2.length()-1);
			double d1=Double.parseDouble(s1.toString()),d2=Double.parseDouble(s2.toString());
			return d1==d2;
		} else if (i1==8) {
			if (s1.toLowerCase().substring(0,4).equals("\"rd:")) {
				s1=s1.substring(1,s1.length()-1);
				if (s2.startsWith("\"")&&s2.endsWith("\"")) s2=s2.substring(1,s2.length()-1);
				RangedDouble rd=new RangedDouble(s1.substring(3));
				if (Character.isLetter(s2.charAt(s2.length()-1))) s2=s2.substring(0,s2.length()-1);
				double d1;
				try {
					d1=Double.parseDouble(s2);
				} catch (Exception e) {
					return false;
				}
				return rd.equals(d1);
			}
			return s1.equals(s2);
		}
		return false;
	}

	public static boolean cmpLocByBlock(Location l1, Location l2) {
		return l1.getBlockX()==l2.getBlockX()&&l1.getBlockY()==l2.getBlockY()&&l1.getBlockZ()==l2.getBlockZ();
	}
	
	public static boolean playerInMotion(Player p) {
		Vec3D v3=Utils.pl.get(p.getUniqueId());
		return Math.abs(v3.getX())>0||Math.abs(v3.getY())>0||Math.abs(v3.getZ())>=0;
	}
	
	public static BlockFace getBlockFacing(float y,boolean bl1) {
		return bl1?radial[Math.round(y/45f)&0x7]:axis[Math.round(y/90f)&0x3];
	}
	
    public static Object[] bubble_sort(Double[]dist1,Object[]arr1,int n1) {
    	for (int i1=0;i1<n1;i1++) {
    		for (int j1=1;j1<(n1-i1);j1++) {
    			if (dist1[j1-1]>dist1[j1]) {
    				arr1=(Object[])swp_array_entry(j1,j1-1,arr1);
    				dist1=(Double[])swp_array_entry(j1,j1-1,dist1);
    			}
    		}
    	}
    	return arr1;
    }
    
    private static Object[] swp_array_entry(int i1,int j1,Object[]arr1) {
    	Object s1=arr1[i1];
    	arr1[i1]=arr1[j1];
    	arr1[j1]=s1;
    	return arr1;
    }
    
    public static Double[] add_to_array(Double[] arr1,double d2) {
    	int i1=arr1.length;
		Double[]arr2=new Double[]{d2};
		Double[]arr=new Double[i1+1];
		System.arraycopy(arr1,0,arr,0,i1);
		System.arraycopy(arr2,0,arr,i1,1);
		return arr;
    }
	

}
