package com.gmail.berndivader.mythicmobsext.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.block.Block;
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
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import com.gmail.berndivader.mythicmobsext.NMS.NMSUtils;
import com.gmail.berndivader.mythicmobsext.compatibility.nocheatplus.NoCheatPlusSupport;
import com.gmail.berndivader.mythicmobsext.compatibility.papi.Papi;
import com.gmail.berndivader.mythicmobsext.compatibilitylib.BukkitSerialization;
import com.gmail.berndivader.mythicmobsext.Main;
import com.gmail.berndivader.mythicmobsext.mechanics.NoDamageTicksMechanic;
import com.gmail.berndivader.mythicmobsext.mechanics.PlayerGoggleMechanic;
import com.gmail.berndivader.mythicmobsext.mechanics.PlayerSpinMechanic;

import fr.neatmonster.nocheatplus.checks.CheckType;
import fr.neatmonster.nocheatplus.hooks.NCPExemptionManager;
import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.mobs.MobManager;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob.ThreatTable;
import io.lumine.xikage.mythicmobs.skills.SkillCaster;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import io.lumine.xikage.mythicmobs.skills.SkillTargeter;
import io.lumine.xikage.mythicmobs.skills.SkillTrigger;
import io.lumine.xikage.mythicmobs.skills.TriggeredSkill;

import com.gmail.berndivader.mythicmobsext.utils.RangedDouble;
import com.gmail.berndivader.mythicmobsext.utils.math.MathUtils;

import think.rpgitems.item.ItemManager;
import think.rpgitems.item.RPGItem;

@SuppressWarnings("deprecation")
public
class 
Utils
implements
Listener
{
	public static MythicMobs mythicmobs;
	public static MobManager mobmanager;
	public static int serverV;
	public static int renderLength;
	public static HashMap<UUID,Vec3D>players;
	public static final String SERIALIZED_ITEM="_b64i";
	public static final String signal_AISHOOT="AISHOOT";
	public static final String signal_AIHIT="AIHIT";
	public static final String signal_CHUNKUNLOAD="CHUNKUNLOAD";
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
	public static final String meta_NOSUNBURN="MMENOSUN";
	public static final String meta_SLOTCHANGEDSTAMP="SLOTSTAMP";
	public static final String meta_BACKBACKTAG="BAG_POS_TAG";
	public static final String meta_TRAVELPOINTS="MME_TRAVEL_POINTS";
	public static final String meta_INVCLICKOLDCURSOR="mmeinvclickold";
	public static final String meta_INVCLICKNEWCURSOR="mmeinvclicknew";
	public static final String signal_GOAL_TRAVELEND="GOAL_TRAVELEND";
	public static final String signal_GOAL_TRAVELPOINT = "GOAL_TRAVELPOINT";
	public static final String meta_DISORIENTATION = "MMEDISORIENTATION";
	public static final String meta_CLICKEDSKILL="click_skill";
	public static final String meta_LASTCLICKEDSLOT = "lastclickedslot";
	public static final String signal_BACKBAGCLICK = "BAGCLICKED";
	public static final String meta_LASTCLICKEDBAG = "lastclickedbag";
	public static final String meta_LASTHEALAMOUNT="mmelastheal";
	public static final String meta_STUNNED="mmeStunned";
	public static String scripts;
	public static String str_PLUGINPATH;
	public static HashSet<Advancement>advancements;
	static Field threattable_field;
	public static Field action_var_field;
	
	static boolean papi_ispresent;
	
	static {
		mythicmobs=MythicMobs.inst();
		mobmanager=mythicmobs.getMobManager();
		renderLength=512;
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
		players=new HashMap<>();
		try {
			threattable_field=ThreatTable.class.getDeclaredField("threatTable");
			threattable_field.setAccessible(true);
			action_var_field=SkillCondition.class.getDeclaredField("actionVar");
			action_var_field.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			// Auto-generated catch block
		}
		papi_ispresent=Main.pluginmanager.getPlugin(Papi.str_PLUGINNAME)!=null;
	}
	
	public Utils() {
		Main.pluginmanager.registerEvents(new UndoBlockListener(),Main.getPlugin());
		Main.getPlugin().getServer().getPluginManager().registerEvents(this,Main.getPlugin());
		p();
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerClickInventory(InventoryClickEvent e) {
		ItemStack new_cursor=e.getCurrentItem();
		ItemStack old_cursor=e.getCursor();
		store_clicked_items(e.getWhoClicked(),new_cursor,old_cursor);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void playerDraggingItem(InventoryDragEvent e) {
		ItemStack new_cursor=e.getCursor();
		ItemStack old_cursor=e.getOldCursor();
		store_clicked_items(e.getWhoClicked(),new_cursor,old_cursor);
	}
	
	static void store_clicked_items(Entity clicker,ItemStack new_cursor,ItemStack old_cursor) {
		if(new_cursor==null) new_cursor=new ItemStack(Material.AIR);
		if(old_cursor==null) old_cursor=new ItemStack(Material.AIR);
		
		clicker.setMetadata(meta_INVCLICKNEWCURSOR,new FixedMetadataValue(Main.getPlugin(),SERIALIZED_ITEM+BukkitSerialization.itemStackToBase64(new_cursor)));
		clicker.setMetadata(meta_INVCLICKOLDCURSOR,new FixedMetadataValue(Main.getPlugin(),SERIALIZED_ITEM+BukkitSerialization.itemStackToBase64(old_cursor)));
	}
	
	@EventHandler
	public void playerHeldItem(PlayerItemHeldEvent e) {
		e.getPlayer().setMetadata(meta_SLOTCHANGEDSTAMP,new FixedMetadataValue(Main.getPlugin(),System.currentTimeMillis()));
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent e) {
		if(e.getChunk()==null) return;
		for(Entity entity:e.getChunk().getEntities()) {
			if(mobmanager.isActiveMob(entity.getUniqueId())) {
				mobmanager.getMythicMobInstance(entity).signalMob(BukkitAdapter.adapt(entity),signal_CHUNKUNLOAD);
			}
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
		if (p.getInventory().getItemInMainHand().getType()==Material.BOW||p.getInventory().getItemInOffHand().getType()==Material.BOW) {
			p.setMetadata(meta_BOWTICKSTART, new FixedMetadataValue(Main.getPlugin(),NMSUtils.getCurrentTick(Bukkit.getServer())));
			new BukkitRunnable() {
				float f1;
				@Override
				public void run() {
					if (p!=null&&p.isOnline()&&(f1=getBowTension(p))>-1) {
						p.setMetadata(meta_BOWTENSIONLAST, new FixedMetadataValue(Main.getPlugin(),f1));
					} else {
						this.cancel();
					}
				}
			}.runTaskTimer(Main.getPlugin(),0l,0l);
		}
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent e) {
		if (e.isCancelled()||!(e.getEntity().getShooter() instanceof Entity)) return;
		final Entity s=(Entity)e.getEntity().getShooter();
		final ActiveMob am=mobmanager.getMythicMobInstance(s);
		if (am!=null) {
			TriggeredSkill ts=new TriggeredSkill(SkillTrigger.SHOOT,am,am.getEntity().getTarget(),new Pair[0]);
			e.setCancelled(ts.getCancelled());
		}
	}
	
	@EventHandler
	public void RemoveFallingBlockProjectile(EntityChangeBlockEvent e) {
		if (e.getEntity().hasMetadata(Utils.mpNameVar)) {
			e.setCancelled(true);
		}
	}

	@EventHandler
	public void mmTriggerOnKill(EntityDeathEvent e) {
		EntityDamageEvent entityDamageEvent = e.getEntity().getLastDamageCause();
		if (entityDamageEvent != null && !entityDamageEvent.isCancelled()
				&& entityDamageEvent instanceof EntityDamageByEntityEvent) {
			LivingEntity damager = getAttacker(((EntityDamageByEntityEvent) entityDamageEvent).getDamager());
			if (damager!=null&&mobmanager.isActiveMob(damager.getUniqueId())) {
				new TriggeredSkill(SkillTrigger.KILL,mobmanager.getMythicMobInstance(damager),BukkitAdapter.adapt(e.getEntity()),new Pair[0]);
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
		TriggeredSkill ts;
		final Entity victim = e.getEntity();
		if (e instanceof EntityDamageByEntityEvent
				|| !(victim instanceof LivingEntity) 
				|| victim instanceof Player
				|| mobmanager.getVoidList().contains(victim.getUniqueId())) return;
		ActiveMob am = mobmanager.getMythicMobInstance(victim);
		if (am==null
				|| !am.getType().getConfig().getBoolean("onDamageForOtherCause")) return;
		ts = new TriggeredSkill(SkillTrigger.DAMAGED,am,null,new Pair[0]);
		if (ts.getCancelled()) e.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		Player p=e.getPlayer();
		UUID uuid;
		if(players.containsKey(uuid=p.getUniqueId())) players.remove(uuid);
		if (p.hasMetadata(NoDamageTicksMechanic.str)) e.getPlayer().removeMetadata(NoDamageTicksMechanic.str,Main.getPlugin());
		if (p.hasMetadata(meta_STUNNED)) {
			p.setGravity(true);
			p.removeMetadata(meta_STUNNED,Main.getPlugin());
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
		double df = e.getDamage(DamageModifier.BASE)!=0?MathUtils.round(md / e.getDamage(DamageModifier.BASE), 3):0.0d;
		if (debug) {
			Main.logger.info("Orignal BukkitDamage: " + Double.toString(e.getDamage(DamageModifier.BASE)));
			Main.logger.info("Custom MythicDamage.: " + Double.toString(md));
			Main.logger.info("DamageFactor: " + Double.toString(df));
			Main.logger.info("-----------------------------");
		}
		if (Double.isNaN(md)) md=0.001D;
		e.setDamage(DamageModifier.BASE,md);
		double damage=MathUtils.round(e.getDamage(DamageModifier.BASE),3);
		for (DamageModifier modifier:DamageModifier.values()) {
			if (!e.isApplicable(modifier)||modifier.equals(DamageModifier.BASE)) continue;
			double modF=df;
			if ((modifier.equals(DamageModifier.ARMOR)&&ignoreArmor)||(modifier.equals(DamageModifier.ABSORPTION)&&ignoreAbs)) modF=0D;
			modF=MathUtils.round(modF*e.getDamage(modifier),3);
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
			boolean preventKnockback, boolean preventImmunity,List<EntityType>ignores, boolean ignoreabs, boolean debug, DamageCause cause,boolean ncp,boolean strict) {
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
		MathUtils.round(damage,3);
		target.setMetadata("DamageAmount", new FixedMetadataValue(Main.getPlugin(), damage));
        target.damage(damage, source);
		if (preventImmunity) {
			if(!ignores.contains(target.getType())) target.setNoDamageTicks(0);
		}
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
		return MathUtils.round(damage, 3);
	}
	
	public static LivingEntity getTargetedEntity(Player player,int range) {
		BlockIterator bi;
		List<Entity>ne=player.getNearbyEntities(range, range, range);
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

    public static List<Player> getPlayersInRange(Location l,double distance){
    	List<Player>players=new ArrayList<Player>();
    	List<Player>list1=l.getWorld().getPlayers();
    	double x1=l.getBlockX(),y1=l.getBlockY(),z1=l.getBlockZ();
    	for(int i1=0;i1<list1.size();i1++) {
    		Player p=list1.get(i1);
    		Location l1=p.getLocation();
    		if(MathUtils.distance3D(x1,y1,z1,l1.getBlockX(),l1.getBlockY(),l1.getBlockZ())<=distance) players.add(p);
    	}
    	return players;
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
	
	public static void triggerShoot(Entity caster, Entity trigger) {
		final ActiveMob am=mobmanager.getMythicMobInstance(caster);
		if (am!=null) {
			new TriggeredSkill(SkillTrigger.SHOOT,am,am.getEntity().getTarget(),new Pair[0]);
		}
	}
	
	public static float getBowTension(Player p) {
		int i1=NMSUtils.getCurrentTick(Bukkit.getServer()),i2=-1;
        if (((HumanEntity)p).isHandRaised()&&p.hasMetadata(meta_BOWTICKSTART)) {
        	i2=p.getMetadata(meta_BOWTICKSTART).get(0).asInt();
        }
        if (i2==-1) return (float)i2;
        float f1=(float)(i1-i2)/20.0f;
        if((f1=(f1*f1+f1*2.0f)/3.0f)>1.0f) f1=1.0f;
        return f1;
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
	
    public static double getGravity(EntityType entityType) {
        switch (entityType) {
            case ARROW:
                return 0.118;
            case SNOWBALL:
                return 0.076;
            case THROWN_EXP_BOTTLE:
                return 0.157;
            case EGG:
                return 0.074;
            default:
                return 0.115;
        }
    }
    
	public static <E extends Enum<E>> E enum_lookup(Class<E> e,String id) {
		if(id==null) return null;
		E result;
		try {
			result=Enum.valueOf(e,id);
		} catch (IllegalArgumentException e1) {
			result=null;
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<AbstractEntity,Double> getActiveMobThreatTable(ActiveMob am) {
		Map<AbstractEntity,Double>threattable=new HashMap<>();
		if (am!=null&&am.hasThreatTable()) {
			try {
				threattable=(Map<AbstractEntity,Double>)threattable_field.get(am.getThreatTable());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return threattable;
	}
	
	private static void p() {
        try {
	        NMSUtils.setField("p",MythicMobs.class,null,true);
        } catch (Throwable e) {
        	//
		}
	}
	
	/**
	 * 
	 * @param targeter_string {@link String}
	 * @return skill_targeter {@link SkillTargeter}
	 */
	
	public static SkillTargeter parseSkillTargeter(String targeter_string) {
        String search = targeter_string.substring(1);
        MythicLineConfig mlc = new MythicLineConfig(search);
        String name = search.contains("{") ? search.substring(0, search.indexOf("{")) : search;
        return SkillTargeter.getMythicTargeter(name, mlc);
	}
	
}
