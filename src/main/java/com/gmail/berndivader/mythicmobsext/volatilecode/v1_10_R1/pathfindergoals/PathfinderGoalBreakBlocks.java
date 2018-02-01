package main.java.com.gmail.berndivader.mythicmobsext.volatilecode.v1_10_R1.pathfindergoals;

import java.util.HashSet;

import net.minecraft.server.v1_10_R1.EntityInsentient;
import net.minecraft.server.v1_10_R1.EntityLiving;
import net.minecraft.server.v1_10_R1.MathHelper;
import net.minecraft.server.v1_10_R1.PathEntity;
import net.minecraft.server.v1_10_R1.PathPoint;
import net.minecraft.server.v1_10_R1.PathfinderGoal;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.com.gmail.berndivader.mythicmobsext.Main;

public class PathfinderGoalBreakBlocks extends PathfinderGoal {
	protected EntityInsentient entity;
	protected boolean isBreaking;
	protected int chance;
	protected HashSet<Material>materials;

	public PathfinderGoalBreakBlocks(EntityInsentient entity, String mL, int chance) {
		this.isBreaking=false;
		this.entity=entity;
		this.materials=new HashSet<>();
		this.chance=chance>100?100:chance<0?0:chance;
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
