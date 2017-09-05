package com.gmail.berndivader.volatilecode;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.gmail.berndivader.NMS.NMSUtil;
import com.gmail.berndivader.mmcustomskills26.CustomSkillStuff;
import com.gmail.berndivader.mmcustomskills26.Main;

import io.lumine.xikage.mythicmobs.MythicMobs;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftLivingEntity;

import net.minecraft.server.v1_10_R1.EntityCreature;
import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.PathfinderGoal;
import net.minecraft.server.v1_10_R1.PathfinderGoalFleeSun;
import net.minecraft.server.v1_10_R1.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_10_R1.PathfinderGoalSelector;
import net.minecraft.server.v1_10_R1.MathHelper;
import net.minecraft.server.v1_10_R1.PathEntity;
import net.minecraft.server.v1_10_R1.PathPoint;

public class Volatile_v1_10_R1 
implements VolatileHandler {
	
	public Volatile_v1_10_R1() {
	}
	
	@Override
	public void aiPathfinderGoal(LivingEntity entity, String uGoal, LivingEntity target) {
		World w = entity.getWorld();
        EntityInsentient e = (EntityInsentient)((CraftLivingEntity)entity).getHandle();
        EntityLiving tE = null;
        if (target!=null) {
        	tE = (EntityLiving)((CraftLivingEntity)entity).getHandle();
        }
        Field goalsField;
        int i=0;
        String goal=uGoal;
        String data=null;
        String data1=null;
        String[] parse = uGoal.split(" ");
        if (parse[0].matches("[0-9]*")) {
        	i = Integer.parseInt(parse[0]);
        	if (parse.length>1) {
        		goal = parse[1];
        		if (parse.length>2) {
        			data = parse[2];
        		}
        		if (parse.length>3) {
        			data1 = parse[3];
        		}
        	}
        }
		try {
			goalsField = EntityInsentient.class.getDeclaredField("goalSelector");
	        goalsField.setAccessible(true);
	        PathfinderGoalSelector goals = (PathfinderGoalSelector)goalsField.get((Object)e);
	        switch (goal) {
	        case "rangedmelee": {
	            if (e instanceof EntityCreature) {
	            	float range = 2.0f;
	            	if (CustomSkillStuff.isNumeric(data)) {
	            		range = Float.parseFloat(data);
	            	}
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalMeleeRangeAttack((EntityCreature)e, 1.0, true, range));
	            }
	        	break;
	        }
	        case "runfromsun": {
	        	if (e instanceof EntityCreature) {
	        		double speed = 1.0d;
	            	if (CustomSkillStuff.isNumeric(data)) {
	            		speed = Double.parseDouble(data);
	            	}
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalFleeSun((EntityCreature)e, speed));
	        	}
	        	break;
	        }
	        case "followentity": {
	        	UUID uuid=null;
	        	if (e instanceof EntityCreature) {
	        		double speed = 1.0d;
	        		if (CustomSkillStuff.isNumeric(data)) {
	        			speed = Double.parseDouble(data);
	        		}
	        		if (data1!=null && (uuid = CustomSkillStuff.isUUID(data1))!=null) {
	        			Main.getPlugin().getNMSUtils();
						Entity ee = NMSUtil.getEntity(w, uuid);
	        			if (ee instanceof LivingEntity) {
	        		        tE = (EntityLiving)((CraftLivingEntity)(LivingEntity)ee).getHandle();
	        			}
	        		}
	        		if (tE!=null && tE.isAlive()) {
		            	goals.a(i, (PathfinderGoal)new PathfinderGoalFollowEntity(e,tE, speed));
	        		}
	        	}
	        	break;
	        }
	        case "breakblocks": {
	        	if (e instanceof EntityCreature) {
	            	goals.a(i, (PathfinderGoal)new PathfinderGoalBreakBlocks(e,data));
	        	}
	        	break;
	        }
	        default: {
	        	List<String>gList=new ArrayList<String>();
	        	gList.add(uGoal);
	            MythicMobs.inst().getVolatileCodeHandler().aiGoalSelectorHandler(entity, gList);
	        }}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
    
	public class PathfinderGoalMeleeRangeAttack extends PathfinderGoalMeleeAttack {
		protected float range;

		public PathfinderGoalMeleeRangeAttack(EntityCreature entityCreature, double d, boolean b, float range) {
			super(entityCreature, d, b);
			this.range=range;
		}

		@Override
		protected double a(EntityLiving entity) {
		    return (double)(this.b.width * this.range * this.b.width * this.range + entity.width);
		}
	}
	
    public class PathfinderGoalFollowEntity extends PathfinderGoal {
        private double speed;
        private EntityInsentient entity;
        private EntityLiving entity1;

        public PathfinderGoalFollowEntity(EntityInsentient e, EntityLiving e1, double s) {
            this.entity = e;
            this.entity1 = e1;
            this.speed = s;
        }
        
        public boolean a() {
            try {
            	if (this.entity1==null 
            			|| !this.entity1.isAlive() 
            			|| !this.entity1.getWorld().equals(this.entity.getWorld())) {
            		return false;
            	}
            	World world = this.entity1.getWorld().getWorld();
            	Location dLoc = new Location(world,this.entity1.locX,this.entity1.locY,this.entity1.locZ);
            	Location sLoc = new Location(world,this.entity.locX,this.entity.locY,this.entity.locZ);
            	if (sLoc.distanceSquared(dLoc)>1024.0) {
            		this.entity.teleportTo(dLoc, false);
            		return true;
            	}
                if (sLoc.distanceSquared(dLoc)>this.speed) {
                    this.entity.getNavigation().a(dLoc.getX(), dLoc.getY(), dLoc.getZ(), this.speed);
                    return true;
                }
                return false;
            }
            catch (Exception ex) {
                ex.printStackTrace();
                return false;
            }
        }
    }
	public class PathfinderGoalBreakBlocks extends PathfinderGoal {
		protected EntityInsentient entity;
		protected boolean isBreaking;
		protected HashSet<Material>materials;

		public PathfinderGoalBreakBlocks(EntityInsentient entity, String mL) {
			this.isBreaking=false;
			this.entity=entity;
			this.materials=new HashSet<>();
			if (mL!=null) {
				String[]parse=mL.toUpperCase().split(",");
				for(int a=0;a<parse.length;a++) {
					try {
						this.materials.add(Material.valueOf(parse[a]));
					} catch (Exception ex) {
						continue;
					}
				}
			}
		}

		public boolean a() {
			return this.entity.isAlive();
		}
		
		public boolean b() {
			if (this.entity.getGoalTarget()!=null
					&& this.entity.getGoalTarget().isAlive()) {
				return true;
			}
			return false;
		}

		public void e() {
			if (!this.canContinue()) return;
			EntityLiving target=this.entity.getGoalTarget();
			Block[] blocks=new Block[2];
            blocks[0] = this.getBreakableTargetBlock(target);
            blocks[1] = blocks[0].getRelative(BlockFace.UP);
            for (int a=0;a<blocks.length;a++) {
            	if (this.materials.isEmpty() 
           			|| this.materials.contains(blocks[a].getType())) this.attemptBreakBlock(blocks[a]);
            }
		}
		
		private boolean canContinue() {
			EntityLiving target = this.entity.getGoalTarget();
			if (target !=null
					&& target.isAlive()
					&& !this.isBreaking
					&& !this.isReachable(target)) {
				return true;
			}
			return false;
		}
		
	    private Block getBreakableTargetBlock(EntityLiving target) {
	        Location direction = target.getBukkitEntity().getLocation().subtract(this.entity.getBukkitEntity().getLocation());
	        double dx=direction.getX();
	        double dz=direction.getY();
	        int bdx=0;
	        int bdz=0;
	        if (Math.abs(dx)>Math.abs(dz)) {
	            bdx=(dx>0)?1:-1;
	        } else {
	            bdz=(dx>0)?1:-1;
	        }
	        return this.entity.world.getWorld().getBlockAt((int) Math.floor(this.entity.locX + bdx),
	        		(int) Math.floor(this.entity.locY),
	        		(int) Math.floor(this.entity.locZ + bdz));
	    }
	    
	    @SuppressWarnings("deprecation")
		private void attemptBreakBlock(Block block) {
	        Material type = block.getType();
	        if (!this.isBreaking
	        		&& type!=Material.AIR 
	        		&& type.isSolid()) {
	            Location location = block.getLocation();
	            if (Main.random.nextInt(100) < 50) {
                    this.entity.world.getWorld().playEffect(location, Effect.ZOMBIE_DESTROY_DOOR, 0);
	            } else {
	            	this.isBreaking=true;
                    PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 20, 4, false, false);
                    ((LivingEntity)this.entity.getBukkitEntity()).addPotionEffect(effect);
	            	new BukkitRunnable() {
						@Override
						public void run() {
			                EntityChangeBlockEvent event = new EntityChangeBlockEvent(PathfinderGoalBreakBlocks.this.entity.getBukkitEntity(), block, Material.AIR, (byte) 0);
			                Main.getPlugin().getServer().getPluginManager().callEvent(event);
			                if (!event.isCancelled()) {
			                    PathfinderGoalBreakBlocks.this.entity.world.getWorld().playSound(location, Sound.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, Math.min(Main.random.nextFloat() + 0.2f, 1.0f), 1.0f);
		                        block.breakNaturally();
		                        PathfinderGoalBreakBlocks.this.isBreaking=false;
			                }
						}
					}.runTaskLaterAsynchronously(Main.getPlugin(), 20L);
	            }
	        }
	    }
	    private boolean isReachable(EntityLiving target) {
	    	if (this.entity.getEntitySenses().a(target)) return true;
	        PathEntity pe=this.entity.getNavigation().a(target);
	        if (pe==null) {
	            return false;
	        } else {
	            PathPoint pp=pe.c();
	            if (pp==null) {
	                return false;
	            } else {
	                int i=pp.a-MathHelper.floor(target.locX);
	                int j=pp.c-MathHelper.floor(target.locZ);
	                return (double)(i*i+j*j)<=2.25D;
	            }
	        }
	    }
	}

}
